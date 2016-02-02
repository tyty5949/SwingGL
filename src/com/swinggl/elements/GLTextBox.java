package com.swinggl.elements;

import com.swinggl.backend.Button;
import com.swinggl.backend.Keyboard;
import com.swinggl.backend.Mouse;
import com.swinggl.backend.TrueTypeFont;
import com.swinggl.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 1/6/2016.
 */
public class GLTextBox extends Button {

    private float x;
    private float y;
    private float w;
    private float h;
    private String text;
    private TrueTypeFont font;
    private ActionListener listener;
    private float[][] texCoords;
    private int state = 0;
    private boolean mouseCooldown = false;
    private int lastKey;

    public GLTextBox(float x, float y, float w, float h, String startText, TrueTypeFont font, ActionListener listener, float[][] texCoords) {
        super(x, y, w, h);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.text = startText;
        this.font = font;
        this.listener = listener;
        this.texCoords = texCoords;
    }

    public void update() {
        if (Mouse.isButtonDown(0) && !mouseCooldown) {
            if (contains(Mouse.getX(), Mouse.getY())) {
                state = 1;
                listener.actionPerformed(new ActionEvent(this, 0, "pressed"));
            } else
                state = 0;
        }

        mouseCooldown = Mouse.isButtonDown(0);

        if (state == 1) {
            if (Keyboard.getRecentKey() != lastKey) {
                lastKey = Keyboard.getRecentKey();
                text += Keyboard.toAscii(lastKey);
            }
        }
    }

    public void render() {
        RenderUtil.drawImmediateTexture(x, y, w, h, texCoords[state]);
        GL11.glEnd();
        font.drawString(text, x, y, Color.BLACK);
        GL11.glBegin(GL11.GL_QUADS);
    }
}
