package com.swinggl.elements;

import com.swinggl.backend.Button;
import com.swinggl.backend.GLAction;
import com.swinggl.backend.GLActionListener;
import com.swinggl.backend.Mouse;
import com.swinggl.util.RenderUtil;

/**
 * Created on 12/17/2015.
 */
public class GLButton extends Button {

    public static final int BUTTON_PRESSED = 0;

    private int state;
    private float[][] texCoords;
    private GLActionListener listener;

    public GLButton(float x, float y, float w, float h, GLActionListener listener, float[][] texCoords) {
        super(x, y, w, h);
        this.texCoords = texCoords;
        state = 0;
        this.listener = listener;
    }

    public void update() {
        if (contains(Mouse.getX(), Mouse.getY())) {
            state = 1;
            if (Mouse.isButtonDown(0)) {
                state = 2;
                if (listener != null)
                    listener.actionPerformed(new GLAction(BUTTON_PRESSED));
            }
        } else
            state = 0;
    }

    public void renderButton() {
        RenderUtil.drawImmediateTexture(x, y, w, h, texCoords[state]);
    }

    public void renderButtonState(int state) {
        RenderUtil.drawImmediateTexture(x, y, w, h, texCoords[state]);
    }
}
