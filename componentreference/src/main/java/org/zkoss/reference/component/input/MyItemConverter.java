package org.zkoss.reference.component.input;

import org.zkoss.util.Converter;

import java.util.Collection;
import java.util.stream.Collectors;

public class MyItemConverter implements Converter<Collection<?>, String> {

    @Override
    public String convert(Collection<?> obj) {
        return obj.stream().map(Object::toString)
                .collect(Collectors.joining(">"));
    }
}
