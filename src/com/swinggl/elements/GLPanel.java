package com.swinggl.elements;

import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Created on 12/13/2015.
 */
public abstract class GLPanel {

    protected Color background;
    public boolean initialized = false;

    public GLPanel() {
        background = Color.BLACK;
    }

    public GLPanel(Color background) {
        super();
        this.background = background;
    }

    public abstract void initialize(GLFrame frame);

    public abstract void update(GLFrame frame, double delta);

    public abstract void render(GLFrame frame, double delta);
}
