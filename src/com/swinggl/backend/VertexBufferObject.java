package com.swinggl.backend;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by tsh5949 on 12/14/2015.
 */
public class VertexBufferObject {

    public static int generateID() {
        return GL15.glGenBuffers();
    }

    public static void vertexBufferData(int id, FloatBuffer buffer) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public static void indexBufferData(int id, IntBuffer buffer) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public static void renderBufferData(int id, )
}
