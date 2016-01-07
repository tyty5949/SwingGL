package com.swinggl.elements;

import com.swinggl.backend.Button;
import com.swinggl.backend.Mouse;
import com.swinggl.util.RenderUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 12/17/2015.
 */
public class GLCheckBox extends Button {

    private boolean mouseCooldown;
    private float[][] texCoords;
    private ActionListener listener;
    private boolean checked;
    private int state;

    public GLCheckBox(float x, float y, float w, float h, boolean checked, ActionListener listener, float[][] texCoords) {
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
                listener.actionPerformed(new ActionEvent(this, 0, "pressed"));
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
