package com.swinggl.elements;

import com.swinggl.backend.Button;
import com.swinggl.backend.Mouse;
import org.lwjgl.opengl.GL11;

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

    public GLCheckBox(float x, float y, float w, float h, boolean checked, float[][] texCoords) {
        super(x, y, w, h);
        this.texCoords = texCoords;
        this.checked = checked;
        state = 0;
    }

    public void update() {
        if (contains(Mouse.getX(), Mouse.getY())) {
            state = 1;
            if (Mouse.isButtonDown(0)) {
                state = 2;
                if (!mouseCooldown)
                    checked = !checked;
            }
        } else
            state = 0;
        mouseCooldown = Mouse.isButtonDown(0);
    }

    public void render() {
        GL11.glTexCoord2f(texCoords[state][0], texCoords[state][1]);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(texCoords[state][2], texCoords[state][3]);
        GL11.glVertex2f(x + w, y);
        GL11.glTexCoord2f(texCoords[state][4], texCoords[state][5]);
        GL11.glVertex2f(x + w, y + h);
        GL11.glTexCoord2f(texCoords[state][6], texCoords[state][7]);
        GL11.glVertex2f(x, y + h);
        if (checked) {
            GL11.glTexCoord2f(texCoords[3][0], texCoords[3][1]);
            GL11.glVertex2f(x, y);
            GL11.glTexCoord2f(texCoords[3][2], texCoords[3][3]);
            GL11.glVertex2f(x + w, y);
            GL11.glTexCoord2f(texCoords[3][4], texCoords[3][5]);
            GL11.glVertex2f(x + w, y + h);
            GL11.glTexCoord2f(texCoords[3][6], texCoords[3][7]);
            GL11.glVertex2f(x, y + h);
        }
    }
}
