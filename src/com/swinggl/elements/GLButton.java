package com.swinggl.elements;

import com.swinggl.backend.Button;
import com.swinggl.backend.Mouse;
import com.swinggl.util.RenderUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 12/17/2015.
 */
public class GLButton extends Button {

    private int state;
    private float[][] texCoords;
    private ActionListener listener;

    public GLButton(float x, float y, float w, float h, ActionListener listener, float[][] texCoords) {
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
                listener.actionPerformed(new ActionEvent(this, 0, "pressed"));
            }
        } else
            state = 0;
    }

    public void renderButton() {
        RenderUtil.drawImmediateTexture(x, y, w, h, texCoords[state]);
    }
}
