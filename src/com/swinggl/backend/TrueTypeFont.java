package com.swinggl.backend;


import com.swinggl.util.IOUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;

/**
 * Created on 12/14/15
 */
public class TrueTypeFont {

    private final int BITMAP_W = 512;
    private final int BITMAP_H = 512;

    private float fontHeight;

    private FloatBuffer bufX;
    private FloatBuffer bufY;
    private STBTTAlignedQuad stbQuad;
    private STBTTBakedChar.Buffer cdata;
    private Texture texture;

    public TrueTypeFont(String file, float fontHeight) {
        this.fontHeight = fontHeight;

        cdata = STBTTBakedChar.mallocBuffer(96);

        ByteBuffer ttf = IOUtil.ioResourceToByteBuffer(file, 160 * 1024);

        ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
        stbtt_BakeFontBitmap(ttf, fontHeight, bitmap, BITMAP_W, BITMAP_H, 32, cdata);

        bufX = BufferUtils.createFloatBuffer(1);
        bufY = BufferUtils.createFloatBuffer(1);
        stbQuad = STBTTAlignedQuad.malloc();

        texture = new Texture(BITMAP_W, BITMAP_H, bitmap);

    }

    public void drawString(String text, float xPos, float yPos, Color col) {
        // Color

        glColor3f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f);

        glPushMatrix();
        // Scale
        //glScalef(scaleFactor, scaleFactor, 1f);
        // Scroll
        glTranslatef(xPos, yPos, 0f);

        bufX.put(0, 0.0f);
        bufY.put(0, 0.0f);
        glBegin(GL_QUADS);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                bufY.put(0, bufY.get(0) + fontHeight);
                bufX.put(0, 0.0f);
                continue;
            } else if (c < 32 || 128 <= c)
                continue;

            stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - 32, bufX, bufY, stbQuad, 1);

            glTexCoord2f(stbQuad.s0(), stbQuad.t0());
            glVertex2f(stbQuad.x0(), stbQuad.y0());

            glTexCoord2f(stbQuad.s1(), stbQuad.t0());
            glVertex2f(stbQuad.x1(), stbQuad.y0());

            glTexCoord2f(stbQuad.s1(), stbQuad.t1());
            glVertex2f(stbQuad.x1(), stbQuad.y1());

            glTexCoord2f(stbQuad.s0(), stbQuad.t1());
            glVertex2f(stbQuad.x0(), stbQuad.y1());
        }
        glEnd();

        glPopMatrix();
    }
}