package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class StringDecorator extends Decorator {
    public StringDecorator() {
        super(Type.STRING_STATE);
    }

    @Override
    public String decorate(String messageForDecoration) {
        return "string: " + messageForDecoration;
    }
}
