package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class IntDecorator extends Decorator {
    public IntDecorator() {
        super(Type.INT_STATE);
    }

    @Override

    public String decorate(String messageForDecoration) {
        return "primitive: " + messageForDecoration;
    }
}
