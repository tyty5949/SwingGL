package com.swinggl.util;

import org.lwjgl.opengl.GL11;

/**
 * Created by tyty5949 on 12/17/15 at 5:20 PM.
 * Project: SwingGL
 */
public class RenderUtil {

    public static void drawImmediateTexture(float x, float y, float w, float h, float[] texCoords) {
        GL11.glTexCoord2f(texCoords[0], texCoords[1]);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(texCoords[2], texCoords[3]);
        GL11.glVertex2f(x + w, y);
        GL11.glTexCoord2f(texCoords[4], texCoords[5]);
        GL11.glVertex2f(x + w, y + h);
        GL11.glTexCoord2f(texCoords[6], texCoords[7]);
        GL11.glVertex2f(x, y + h);
    }

    public static void enableTransparency() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void disableTransparency() {
        GL11.glDisable(GL11.GL_BLEND);
    }
}
