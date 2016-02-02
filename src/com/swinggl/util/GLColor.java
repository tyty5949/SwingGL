package com.swinggl.util;

/**
 * Created by tyty5949 on 2/2/16 at 2:16 PM.
 * Project: SwingGL
 */
public class GLColor {

    public static final GLColor BLACK = new GLColor(0f, 0f, 0f, 1f);
    public static final GLColor BLUE = new GLColor(0f, 0f, 225f, 1f);
    public static final GLColor CYAN = new GLColor(0f, 225f, 225f, 1f);
    public static final GLColor DARK_GRAY = new GLColor(96f, 96f, 96f, 1f);
    public static final GLColor GRAY = new GLColor(169f, 169f, 169f, 1f);
    public static final GLColor GREEN = new GLColor(0f, 255f, 0f, 1f);
    public static final GLColor YELLOW = new GLColor(255f, 255f, 0f, 1f);
    public static final GLColor LIGHT_GRAY = new GLColor(215f, 215f, 215f, 1f);
    public static final GLColor MAGENTA = new GLColor(255f, 0f, 255f, 1f);
    public static final GLColor ORANGE = new GLColor(255f, 165f, 0f, 1f);
    public static final GLColor PINK = new GLColor(255f, 192f, 203f, 1f);
    public static final GLColor RED = new GLColor(255f, 0f, 0f, 1f);
    public static final GLColor WHITE = new GLColor(255f, 255f, 255f, 1f);

    private float red;
    private float green;
    private float blue;
    private float alpha;

    public GLColor(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    public GLColor(float r, float g, float b, float a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }
}
