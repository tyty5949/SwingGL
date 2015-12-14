package com.swinggl.backend;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * Created on 12/11/2015.
 */
public class Texture {

    private int texId;
    private int tWidth;
    private int tHeight;

    public Texture(File filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = createBuffer(img);
        createTexture(buffer);
    }

    public Texture(BufferedImage image) {
        ByteBuffer buffer = createBuffer(image);
        createTexture(buffer);
    }

    public Texture(int w, int h, ByteBuffer b) {
        tWidth = w;
        tHeight = h;
        createTexture(b);
    }

    public ByteBuffer createBuffer(BufferedImage image) {
        tWidth = image.getWidth();
        tHeight = image.getHeight();
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip();
        return buffer;
    }

    public void createTexture(ByteBuffer buffer) {
        // Create a new texture object in memory and bind it
        texId = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        // Generate mip maps (for scaling) and upload texture data
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        //GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        //GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 8);

        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR_MIPMAP_LINEAR);
    }

    public void bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
    }

    public int getWidth() {
        return tWidth;
    }

    public int getHeight() {
        return tHeight;
    }

    public void destroy() {
        GL11.glDeleteTextures(texId);
    }
}
