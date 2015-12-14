package com.swinggl.elements;

import com.swinggl.backend.Debug;
import com.swinggl.backend.Keyboard;
import com.swinggl.backend.Mouse;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Calendar;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created on 12/13/2015.
 */
public class GLFrame {

    public static final int EXIT_ON_CLOSE = 0;
    public static final int WINDOW_CENTERED = 0;
    public static final int WINDOW_TOP_LEFT = 1;
    public static final int WINDOW_TOP_RIGHT = 2;
    public static final int WINDOW_BOTTOM_LEFT = 3;
    public static final int WINDOW_BOTTOM_RIGHT = 4;

    private static GLFWErrorCallback errorCallback;
    private static GLFWKeyCallback keyCallback;
    private static GLFWCursorPosCallback cursorPosCallback;
    private static GLFWMouseButtonCallback mouseButtonCallback;
    private static GLFWScrollCallback scrollCallback;
    private boolean debugging;
    private long windowHandle;
    private String title;
    private float x, y, w, h;
    private boolean resizable;
    private boolean visible;
    private int targetFPS;
    private int targetUPS;
    private double fns;
    private double uns;

    private volatile GLPanel panel;

    public GLFrame() {
        System.setProperty("java.awt.headless", "true");
        Thread.currentThread().setName(title + " | render");
        debugging = false;

        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if (glfwInit() != GLFW_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        title = "Sample Title";
        x = 0.0f;
        y = 0.0f;
        w = 720.0f;
        h = 480.0f;
        resizable = true;
        visible = true;
        setTargetFPS(60);
        setTargetUPS(60);

        setPanel(null);

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        windowHandle = glfwCreateWindow((int) w, (int) h, "Hello World!", NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
    }

    public void run() {
        new Thread(new UpdateThread(), title + " | update").start();

        setResizable(resizable);
        setWindowLocation(WINDOW_CENTERED);

        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);

        glfwShowWindow(windowHandle);

        GL.createCapabilities();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, w, h, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        long now = System.nanoTime();
        long lastTime = now;
        double deltaR = 0.0;
        long lastRender = now;
        double renderDelta;
        boolean running = true;

        if (debugging)
            Debug.println("Began game loop @ " + Calendar.getInstance().getTime(), Debug.ANSI_BLUE);

        while (running) {
            if (glfwWindowShouldClose(windowHandle) == GL_TRUE)
                running = false;

            now = System.nanoTime();
            deltaR += (now - lastTime) / fns;
            lastTime = now;

            if (deltaR >= 1.0) {
                renderDelta = (now - lastRender) / 1000000000.0f;
                render(renderDelta);
                lastRender = now;
                deltaR--;
            }
        }

        try {
            glfwDestroyWindow(windowHandle);
            keyCallback.release();
            mouseButtonCallback.release();
            scrollCallback.release();
            cursorPosCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
        if (debugging)
            Debug.println("Closed game loop @ " + Calendar.getInstance().getTime(), Debug.ANSI_BLUE);
    }

    private void render(double delta) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();

        if (panel != null) {
            if (!panel.initialized)
                panel.initialize(this);
            else
                panel.render(this, delta);
        }
    }

    public void bindKeyboard() {
        glfwSetKeyCallback(windowHandle, keyCallback = new Keyboard());
    }

    public void bindMouse() {
        glfwSetCursorPosCallback(windowHandle, cursorPosCallback = new Mouse.CursorPos());
        glfwSetMouseButtonCallback(windowHandle, mouseButtonCallback = new Mouse.MouseButton());
        glfwSetScrollCallback(windowHandle, scrollCallback = new Mouse.Scroll());
    }

    public void enableDebugging(boolean debugging) {
        this.debugging = debugging;
    }

    private GLFrame getSuperClass() {
        return this;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public void setPanel(GLPanel panel) {
        this.panel = panel;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        if (resizable)
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    }

    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
        glfwSetWindowSize(windowHandle, (int) w, (int) h);
    }

    public void setTargetFPS(int targetFPS) {
        this.targetFPS = targetFPS;
        fns = 1000000000.0f / targetFPS;
    }

    public void setTargetUPS(int targetUPS) {
        this.targetUPS = targetUPS;
        uns = 1000000000.0f / targetUPS;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(windowHandle, title);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible)
            glfwShowWindow(windowHandle);
        else
            glfwHideWindow(windowHandle);
    }

    public void setWindowLocation(int loc) {
        if (loc == WINDOW_CENTERED) {
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            setWindowLocation((vidmode.width() - (int) w) / 2, (vidmode.height() - (int) h) / 2);
        } else if (loc == WINDOW_TOP_LEFT) {
            setWindowLocation(0, 0);
        } else if (loc == WINDOW_TOP_RIGHT) {
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            setWindowLocation((vidmode.width() - (int) w), 0);
        } else if (loc == WINDOW_BOTTOM_LEFT) {
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            setWindowLocation(0, (vidmode.height() - (int) h));
        } else if (loc == WINDOW_BOTTOM_RIGHT) {
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            setWindowLocation((vidmode.width() - (int) w), (vidmode.height() - (int) h));
        }
    }

    public void setWindowLocation(float x, float y) {
        this.x = x;
        this.y = y;
        glfwSetWindowPos(windowHandle, (int) x, (int) y);
    }

    private class UpdateThread implements Runnable {

        @Override
        public void run() {
            long now = System.nanoTime();
            long lastTime = now;
            double deltaU = 0.0;
            long lastUpdate = now;
            boolean running = true;

            while (running) {
                if (glfwWindowShouldClose(windowHandle) == GL_TRUE)
                    running = false;

                now = System.nanoTime();
                deltaU += (now - lastTime) / uns;
                lastTime = now;

                if (deltaU >= 1.0) {
                    update((now - lastUpdate) / 1000000000.0f);
                    lastUpdate = now;
                    deltaU--;
                }
            }
        }

        private void update(double delta) {
            if (panel != null) {
                if (panel.initialized)
                    panel.update(getSuperClass(), delta);
            }
        }
    }
}
