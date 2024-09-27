package org.zkoss.reference.developer.customization;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.util.RequestInterceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * You can process a request. If you throw an exception, it
 */
public class MyReqInterceptor implements RequestInterceptor {
    @Override
    public void request(Session sess, Object request, Object response) {
        System.out.println("RequestInterceptor: " + request);
        //convert to HttpServletRequest and check its parameters contains onClick or not
        if (request instanceof HttpServletRequest){
            blockOnUpload((HttpServletRequest) request);
        }
    }

    private void blockOnUpload(HttpServletRequest request) {
        //check if any parameter's value contains onClick
        request.getParameterNames().asIterator().forEachRemaining(name -> {
            if (request.getParameter(name).contains("onUpload")){
                throw new UiException("RequestInterceptor: reject onClick");
            }
        });
    }
}
