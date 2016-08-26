package com.acme.edu;



/**
 * Created by Java_12 on 25.08.2016.
 */
public abstract class Decorator {
    private Type type;

    public Decorator(Type type) {
        this.type = type;
    }

    public abstract String decorate(String messageForDecoration);

    public Type getType() {
        return type;
    }

}
