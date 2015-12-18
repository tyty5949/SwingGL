package com.swinggl.backend;

import com.swinggl.util.IOUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Tyler Hunt on 12/16/2015.
 * <p/>
 * This class is designed to hold all of the data for a TrueTypeFont and includes commonly used methods such as drawing text.
 */
public class TrueTypeFont {

    private final int BITMAP_W = 512;
    private final int BITMAP_H = 512;

    private int textureID;

    private int fontHeight;
    private STBTTBakedChar.Buffer cdata;
    private FloatBuffer xbuf;
    private FloatBuffer ybuf;
    private STBTTAlignedQuad quad;

    /**
     * The constructor that creates the font from the given file at the given height.
     *
     * @param filePath   - The path to file including the file type
     * @param fontHeight - The height (size) of the font
     */
    public TrueTypeFont(String filePath, int fontHeight) {
        this.fontHeight = fontHeight;

        long startTime = 0L;
        if (Debug.enabled)
            startTime = System.nanoTime();

        textureID = glGenTextures();
        cdata = STBTTBakedChar.malloc(96);

        try {
            ByteBuffer ttf = IOUtil.ioResourceToByteBuffer(filePath, 160 * 1024);

            ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
            STBTruetype.stbtt_BakeFontBitmap(ttf, fontHeight, bitmap, BITMAP_W, BITMAP_H, 32, cdata);

            glBindTexture(GL_TEXTURE_2D, textureID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        xbuf = BufferUtils.createFloatBuffer(1);
        ybuf = BufferUtils.createFloatBuffer(1);
        quad = STBTTAlignedQuad.malloc();

        if (Debug.enabled) {
            long endTime = System.nanoTime();
            Debug.println(" Loaded font: " + filePath + "\n\tFont height: " + fontHeight + "px" + "\n\tLoad time: " + Debug.noNotation.format((endTime -
                    startTime) / 1000000000.0) + "s", Debug.ANSI_CYAN);
        }
    }

    /**
     * Used to get the width of a string of text in the given font. It is rather inefficient so it is better to generate widths as few times as possible.
     *
     * @param text - The text
     * @return - The width
     */
    public float getHeight(String text) {
        float height = 0;
        xbuf.put(0, 0.0f);
        ybuf.put(0, 0.0f);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                ybuf.put(0, ybuf.get(0) + fontHeight);
                xbuf.put(0, 0.0f);
                continue;
            } else if (c < 32 || 128 <= c)
                continue;

            STBTruetype.stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - 32, xbuf, ybuf, quad, 1);

            if (quad.y1() - quad.y0() > height)
                height = quad.y1() - quad.y0();
        }
        return height;
    }

    /**
     * Used to get the width of a string of text in the given font. It is rather inefficient so it is better to generate widths as few times as possible.
     *
     * @param text - The text
     * @return - The width
     */
    public float getWidth(String text) {
        float width = 0;
        xbuf.put(0, 0.0f);
        ybuf.put(0, 0.0f);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                ybuf.put(0, ybuf.get(0) + fontHeight);
                xbuf.put(0, 0.0f);
                continue;
            } else if (c < 32 || 128 <= c)
                continue;

            STBTruetype.stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - 32, xbuf, ybuf, quad, 1);

            width += quad.x1() - quad.x0() + 1.5f;
        }
        return width;
    }

    /**
     * Used to draw a string of text on the current GLPanel
     *
     * @param text  - The text
     * @param x     - The x coordinate
     * @param y     - The y coordinate
     * @param color - The color of the text
     */
    public void drawString(String text, float x, float y, Color color) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glBindTexture(GL_TEXTURE_2D, textureID);
        glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        glPushMatrix();
        glTranslatef(x, y + (fontHeight * .5f), 0f);

        xbuf.put(0, 0.0f);
        ybuf.put(0, 0.0f);
        glBegin(GL_QUADS);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                ybuf.put(0, ybuf.get(0) + fontHeight);
                xbuf.put(0, 0.0f);
                continue;
            } else if (c < 32 || 128 <= c)
                continue;

            STBTruetype.stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - 32, xbuf, ybuf, quad, 1);

            glTexCoord2f(quad.s0(), quad.t0());
            glVertex2f(quad.x0(), quad.y0());

            glTexCoord2f(quad.s1(), quad.t0());
            glVertex2f(quad.x1(), quad.y0());

            glTexCoord2f(quad.s1(), quad.t1());
            glVertex2f(quad.x1(), quad.y1());

            glTexCoord2f(quad.s0(), quad.t1());
            glVertex2f(quad.x0(), quad.y1());
        }
        glEnd();

        glPopMatrix();
        glDisable(GL_BLEND);
    }
}
