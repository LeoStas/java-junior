package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class ByteDecorator extends Decorator {
    public ByteDecorator() {
        super(Type.BYTE_STATE);
    }

    @Override
    public String decorate(String messageForDecoration) {
        return "primitive: " + messageForDecoration;
    }
}
