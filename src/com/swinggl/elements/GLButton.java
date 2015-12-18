package com.swinggl.elements;

import com.swinggl.backend.Button;
import com.swinggl.backend.Mouse;
import org.lwjgl.opengl.GL11;

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
        GL11.glTexCoord2f(texCoords[state][0], texCoords[state][1]);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(texCoords[state][2], texCoords[state][3]);
        GL11.glVertex2f(x + w, y);
        GL11.glTexCoord2f(texCoords[state][4], texCoords[state][5]);
        GL11.glVertex2f(x + w, y + h);
        GL11.glTexCoord2f(texCoords[state][6], texCoords[state][7]);
        GL11.glVertex2f(x, y + h);
    }
}
