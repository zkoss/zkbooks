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
import java.util.Optional;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.bouncycastle.util.encoders.Hex;
import org.zkoss.zk.ui.sys.DigestUtilsHelper;

/**
 * @author jumperchen
 */
public class ZkCspFilterStrictDynamic implements Filter {
	
	private Logger logger = Logger.getLogger(ZkCspFilterStrictDynamic.class.getName());
	
	private static final SecureRandom PRNG = new SecureRandom();
	private static String cspHeader;
	private MessageDigest _digest;

	public void init(FilterConfig filterConfig) throws ServletException {
		// we can pass init parameters from web.xml here by the filterConfig object.
		logger.log(Level.INFO, "Initialized CSP filter");
		cspHeader = Optional.of(filterConfig.getInitParameter("csp-header")).orElse("script-src 'strict-dynamic' 'nonce-%s' 'unsafe-inline' 'unsafe-eval';object-src 'none';base-uri 'none';");
		_digest = DigestUtilsHelper.getDigest(Optional.of(filterConfig.getInitParameter("digest-algorithm")).orElse("SHA-1"));
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String hex = Hex.toHexString(_digest.digest(Integer.toString(PRNG.nextInt()).getBytes()));
		logger.log(Level.INFO, "filtered " + request + " \nwith nonce: " + hex);
		CapturingResponseWrapper capturingResponseWrapper = new CapturingResponseWrapper((HttpServletResponse) response);
		chain.doFilter(request, capturingResponseWrapper);
		String content = capturingResponseWrapper.getCaptureAsString();
		String replacedContent = content.replaceAll("(?i)<script(\\s)*","<script nonce=\"" + hex + "\"");
		response.getWriter().write(replacedContent);
		

		((HttpServletResponse) response).addHeader("Content-Security-Policy", String.format(cspHeader, hex));
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
