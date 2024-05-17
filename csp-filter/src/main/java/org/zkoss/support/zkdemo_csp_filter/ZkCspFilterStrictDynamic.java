/* ZkCspFilterStrictDynamic.java

	Purpose:
		
	Description:
		
	History:
		5:03 PM 2023/7/31, Created by jumperchen

Copyright (C) 2023 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.support.zkdemo_csp_filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.zkoss.web.servlet.http.Https;
import org.zkoss.zk.ui.sys.DigestUtilsHelper;

/**
 * using strict-dynamic then 'unsafe-inline' will be ignored.
 * @author jumperchen, Hawk Chen
 */
public class ZkCspFilterStrictDynamic implements Filter {

	public static final String DEFAULT_CSP = "script-src " +
			" 'unsafe-eval' " +
			" 'strict-dynamic' 'nonce-%s' " + //https://content-security-policy.com/strict-dynamic/
			" 'unsafe-hashes' " + //https://content-security-policy.com/unsafe-hashes/
			" 'sha256-lfXlPY3+MCPOPb4mrw1Y961+745U3WlDQVcOXdchSQc=';" + // allow <a href="javascript:;">
			"object-src 'none';base-uri 'none';";
	private Logger logger = Logger.getLogger(ZkCspFilterStrictDynamic.class.getName());
	
	private static final SecureRandom RNG = new SecureRandom();
	private String cspHeader;
	private boolean compress; 
	private MessageDigest _digest;

	public void init(FilterConfig filterConfig) throws ServletException {
		// we can pass init parameters from web.xml here by the filterConfig object.
		logger.log(Level.INFO, "Initialized CSP filter");
		cspHeader = StringUtils.defaultIfEmpty(filterConfig.getInitParameter("csp-header"), DEFAULT_CSP);
		_digest = DigestUtilsHelper.getDigest(StringUtils.defaultIfEmpty(filterConfig.getInitParameter("digest-algorithm"),"SHA-1"));
		compress = !"false".equals(filterConfig.getInitParameter("compress"));
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String hex = Hex.toHexString(_digest.digest(Integer.toString(RNG.nextInt()).getBytes()));
		((HttpServletResponse) response).addHeader("Content-Security-Policy", String.format(cspHeader, hex));

		CapturingResponseWrapper capturingResponseWrapper = new CapturingResponseWrapper((HttpServletResponse) response);
		chain.doFilter(request, capturingResponseWrapper);

		String content = capturingResponseWrapper.getCaptureAsString();
		String replacedContent = content.replaceAll("(?i)<script(\\s)*","<script nonce=\"" + hex + "\" ");

		handleCompress((HttpServletRequest) request, response, replacedContent);
		logger.log(Level.FINE, "filtered " + request + " \nwith nonce: " + hex);
	}

	protected void handleCompress(HttpServletRequest request, ServletResponse response, String replacedContent) throws IOException {
		if(compress) {
			// Do gzip after CSP rewriting
			byte[] data = replacedContent.getBytes(response.getCharacterEncoding());
			if (data.length > 200) {
				byte[] bs = Https.gzip(request, (HttpServletResponse) response, null, data);
				if (bs != null)
					data = bs;
			}

			response.setContentLength(data.length);
			response.getOutputStream().write(data);
			response.flushBuffer();
		}else {
			response.getWriter().write(replacedContent);
		}
	}

	public void destroy() {
	}

	private static class CapturingResponseWrapper
			extends HttpServletResponseWrapper {
		private final ByteArrayOutputStream capture;
		private ServletOutputStream output;
		private PrintWriter writer;

		public CapturingResponseWrapper(HttpServletResponse response) {
			super(response);
			capture = new ByteArrayOutputStream(response.getBufferSize());
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			if (writer != null) {
				throw new IllegalStateException(
						"getWriter() has already been called on this response.");
			}

			final ServletOutputStream outputStream = super.getOutputStream();
			if (output == null) {
				output = new ServletOutputStream() {
					public boolean isReady() {
						return outputStream.isReady();
					}

					public void setWriteListener(WriteListener writeListener) {
						outputStream.setWriteListener(writeListener);
					}

					@Override
					public void write(int b) throws IOException {
						capture.write(b);
					}

					@Override
					public void flush() throws IOException {
						capture.flush();
					}

					@Override
					public void close() throws IOException {
						capture.close();
					}
				};
			}

			return output;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			if (output != null) {
				throw new IllegalStateException(
						"getOutputStream() has already been called on this response.");
			}

			if (writer == null) {
				writer = new PrintWriter(new OutputStreamWriter(capture,
						getCharacterEncoding()));
			}

			return writer;
		}

		@Override
		public void flushBuffer() throws IOException {
			super.flushBuffer();

			if (writer != null) {
				writer.flush();
			} else if (output != null) {
				output.flush();
			}
		}

		public byte[] getCaptureAsBytes() throws IOException {
			if (writer != null) {
				writer.close();
			} else if (output != null) {
				output.close();
			}

			return capture.toByteArray();
		}

		public String getCaptureAsString() throws IOException {
			return new String(getCaptureAsBytes(), getCharacterEncoding());
		}

	}
}
