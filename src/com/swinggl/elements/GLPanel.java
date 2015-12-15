package com.swinggl.elements;

/**
 * Created on 12/13/2015.
 */
public abstract class GLPanel {

    public boolean initialized = false;

    public abstract void init(GLFrame frame);

    public abstract void update(GLFrame frame, float delta);

    public abstract void render(GLFrame frame, float delta);

    public abstract void dispose();
}
