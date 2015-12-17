package com.swinggl.backend;

import com.swinggl.util.IOUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * Created by jas5973 on 12/16/2015.
 */
public class Texture {

    private int id;
    private int width;
    private int height;
    private int comp;
    private boolean hdr;

    public Texture(String imagePath) {
        this(imagePath, GL_LINEAR, GL13.GL_CLAMP_TO_BORDER);
    }

    public Texture(String imagePath, int filter, int clampMethod) {
        long startTime = 0L;
        if (Debug.enabled)
            startTime = System.nanoTime();
        ByteBuffer imageBuffer;
        try {
            imageBuffer = IOUtil.ioResourceToByteBuffer(imagePath, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer compBuffer = BufferUtils.createIntBuffer(1);

        if (stbi_info_from_memory(imageBuffer, widthBuffer, heightBuffer, compBuffer) == 0)
            throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());

        ByteBuffer image = stbi_load_from_memory(imageBuffer, widthBuffer, heightBuffer, compBuffer, 0);
        if (image == null)
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

        width = widthBuffer.get(0);
        height = heightBuffer.get(0);
        comp = compBuffer.get(0);
        hdr = stbi_is_hdr_from_memory(imageBuffer) == 1;

        id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);
        if (this.comp == 3)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        else
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, clampMethod);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, clampMethod);

        if (Debug.enabled) {
            long endTime = System.nanoTime();
            Debug.println(" Loaded texture: " + imagePath + "\n\tTexture size: [" + width + "px, " + height + "px]\n\tTexture comp: " + comp + "\n\tHDR: "
                    + hdr + "\n\tLoad time: " + Debug.noNotation.format((endTime - startTime) / 1000000000.0) + "s", Debug.ANSI_CYAN);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getComp() {
        return comp;
    }

    public boolean isHDR() {
        return hdr;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void delete() {
        nstbi_image_free(id);
    }
}
