package org.zkoss.reference.developer.i18n;

import jakarta.servlet.ServletContext;
import org.zkoss.util.resource.LabelLocator;

import java.net.URL;
import java.util.Locale;

public class MyLableLocator implements LabelLocator {
    private ServletContext context;

    public MyLableLocator(ServletContext context){
        this.context = context;
    }

    @Override
    public URL locate(Locale locale) throws Exception {

        return context.getResource("/resource.properties");
    }
}
