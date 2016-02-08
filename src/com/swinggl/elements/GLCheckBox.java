package com.swinggl.elements;

import com.swinggl.backend.Button;
import com.swinggl.backend.GLAction;
import com.swinggl.backend.GLActionListener;
import com.swinggl.backend.Mouse;
import com.swinggl.util.RenderUtil;

/**
 * Created on 12/17/2015.
 */
public class GLCheckBox extends Button {

    public static int PRESSED = 0;

    private boolean mouseCooldown;
    private float[][] texCoords;
    private GLActionListener listener;
    private boolean checked;
    private int state;

    public GLCheckBox(float x, float y, float w, float h, boolean checked, GLActionListener listener, float[][] texCoords) {
        super(x, y, w, h);
        this.texCoords = texCoords;
        this.checked = checked;
        this.listener = listener;
        state = 0;
    }

    public void update() {
        if (contains(Mouse.getX(), Mouse.getY())) {
            state = 1;
            if (Mouse.isButtonDown(0)) {
                state = 2;
                listener.actionPerformed(new GLAction(PRESSED));
                if (!mouseCooldown)
                    checked = !checked;
            }
        } else
            state = 0;
        mouseCooldown = Mouse.isButtonDown(0);
    }

    public void render() {
        RenderUtil.drawImmediateTexture(x, y, w, h, texCoords[state]);
        if (checked) {
            RenderUtil.drawImmediateTexture(x, y, w, h, texCoords[3]);
        }
    }
}
