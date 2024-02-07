package org.zkoss.mvvm.databinding;

import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.zkoss.util.Locales;

/**
 * Interpolate a message parameter upon zk current locale. It can load a Validation.properties based on current locale.
 * a message parameter is: <code>@Range(min=0, message="{no-negative}")</code>
 * The reason we need this:
 * ResourceBundleMessageInterpolator is a class used by Hibernate Validator to handle locale-aware message interpolation. However, this class uses the default locale specified at startup and cannot change dynamically at runtime. This is an issue when we want to change the language of the error messages dynamically based on the current user's preferences or settings.
 * See: https://docs.jboss.org/hibernate/validator/5.1/reference/en-US/html/chapter-message-interpolation.html#section-message-interpolation
 * Usage:
 * apply by specifying this class in validation.xml:
 * <message-interpolator>org.zkoss.mvvm.databinding.CurrentLocaleMessageInterpolator</message-interpolator>
 */
public class CurrentLocaleMessageInterpolator extends ResourceBundleMessageInterpolator {

    @Override
    public String interpolate(String message, Context context) {
        String interpolatedMessage = message;
        try {
            interpolatedMessage = interpolate( message, context, Locales.getCurrent());
        }
        catch (MessageDescriptorFormatException e) {
            e.printStackTrace();
        }
        return interpolatedMessage;
    }
}
