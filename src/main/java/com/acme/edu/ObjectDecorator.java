package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class ObjectDecorator extends Decorator {
    public ObjectDecorator() {
        super(Type.OBJECT_STATE);
    }

    @Override
    public String decorate(String messageForDecoration) {
        return "reference: " + messageForDecoration;
    }
}
