package com.swinggl.elements;

/**
 * Created on 12/13/2015.
 */
public abstract class GLPanel {

    public boolean initialized = false;

    public abstract void initialize(GLFrame frame);

    public abstract void update(GLFrame frame, double delta);

    public abstract void render(GLFrame frame, double delta);
}
