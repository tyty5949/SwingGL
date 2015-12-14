package com.swinggl.backend;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * Created on 12/13/2015.
 */
public class Mouse {

    private static double x;
    private static double y;
    private static boolean[] buttons = new boolean[8];
    private static int scrollDX;
    private static int scrollDY;

    public static double getX() {
        return x;
    }

    public static double getY() {
        return y;
    }

    public static boolean isButtonDown(int button) {
        return buttons[button];
    }

    public static int getScrollDX() {
        int temp = scrollDX;
        scrollDX = 0;
        return temp;
    }

    public static int getScrollDY() {
        int temp = scrollDY;
        scrollDY = 0;
        return temp;
    }

    public static class CursorPos extends GLFWCursorPosCallback {

        @Override
        public void invoke(long window, double xpos, double ypos) {
            x = xpos;
            y = ypos;
        }
    }

    public static class MouseButton extends GLFWMouseButtonCallback {

        @Override
        public void invoke(long window, int button, int action, int mods) {
            buttons[button] = action != GLFW.GLFW_RELEASE;
        }
    }

    public static class Scroll extends GLFWScrollCallback {

        @Override
        public void invoke(long window, double xOffset, double yOffset) {
            scrollDX += xOffset;
            scrollDY += yOffset;
        }
    }

}
