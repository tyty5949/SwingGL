package com.swinggl.backend;

/*
 * (C) Copyright 2015 Tyler Hunt

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 */

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

    public static char toAscii(int key) {
        return (char) key;
    }
}
