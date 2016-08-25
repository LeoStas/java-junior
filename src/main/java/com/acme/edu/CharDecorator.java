package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class CharDecorator extends Decorator {
    public CharDecorator() {
        super(Type.CHAR_STATE);
    }

    @Override
    public String decorate(String messageForDecoration) {
        return "char: " + messageForDecoration;
    }
}
