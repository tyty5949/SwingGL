package com.swinggl.backend;

/**
 * Created on 12/17/2015.
 */
public class Button {

    protected float x, y, w, h;

    public Button(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean contains(float x1, float y1) {
        return (x <= x1 && x + w >= x1 && y <= y1 && y + h >= y1);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }

    public void setPostition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }
}
