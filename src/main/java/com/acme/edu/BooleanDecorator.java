package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class BooleanDecorator extends Decorator {
    public BooleanDecorator() {
        super(Type.BOOLEAN_STATE);
    }

    @Override
    public String decorate(String messageForDecoration) {
        return "primitive: " + messageForDecoration;
    }
}
