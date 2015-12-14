package com.swinggl.backend;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created on 12/13/2015.
 */
public class Keyboard extends GLFWKeyCallback {
    private static boolean[] keys = new boolean[348];
    private static boolean[] keyTyped = new boolean[348];
    private static int recentKey = -1;

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (action != GLFW.GLFW_RELEASE)
            keyTyped[key] = true;
        keys[key] = action != GLFW.GLFW_RELEASE;
        if (action != GLFW.GLFW_RELEASE)
            recentKey = key;
    }

    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

    public static boolean isKeyTyped(int keycode) {
        if (keyTyped[keycode]) {
            keyTyped[keycode] = false;
            return true;
        }
        return false;
    }

    public static int getRecentKey() {
        return recentKey;
    }

    public char toAscii(int key) {
        return (char) key;
    }
}
