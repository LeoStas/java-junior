package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class NoDecorator extends Decorator {
    public NoDecorator() {
        super(Type.NO_STATE);
    }

    @Override
    public String decorate(String messageForDecoration) {
        return "";
    }
}
