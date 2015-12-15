package com.swinggl.elements;

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

import com.swinggl.backend.Debug;
import com.swinggl.backend.Keyboard;
import com.swinggl.backend.Mouse;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created on 12/13/2015.
 * http://www.glfw.org/docs/latest/index.html
 * ------------------------------------------
 * GLFW
 * [X] Callbacks
 * [X] Cursors
 * [ ] Cursor Object (Cursor look)
 * [ ] Contexts
 * [ ] Monitors
 * [X] Windows
 * [X] Input
 * [ ] Joysticks
 * [ ] Oculus Rift
 * [ ] Standards
 * [ ] Build in references (Joystick buttons, Mouse cursors, etc)
 */
public class GLFrame {

    public static final int WINDOW_CENTERED = 0;
    public static final int WINDOW_TOP_LEFT = 1;
    public static final int WINDOW_TOP_RIGHT = 2;
    public static final int WINDOW_BOTTOM_LEFT = 3;
    public static final int WINDOW_BOTTOM_RIGHT = 4;
    private static final int WINDOW_POSITION_CUSTOM = -1;

    // Callbacks
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWCursorEnterCallback cursorEnterCallback = null;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;
    private GLFWWindowPosCallback windowPosCallback = null;
    private GLFWWindowSizeCallback windowSizeCallback = null;
    private GLFWWindowCloseCallback windowCloseCallback = null;
    private GLFWWindowRefreshCallback windowRefreshCallback = null;
    private GLFWWindowFocusCallback windowFocusCallback = null;
    private GLFWWindowIconifyCallback windowIconifyCallback = null;
    private GLFWDropCallback dropCallback = null;

    private long window = 0L;
    private boolean running = false;
    private float updateDelta = 0.0f;
    private float renderDelta = 0.0f;
    private boolean debug;

    // Window Attributes
    private String title = "SwingGL - GLFrame";
    private Color backgroundColor = Color.WHITE;
    private int windowWidth = 600;
    private int windowHeight = 400;
    private int windowX = 0;
    private int windowY = 0;
    private int windowPosition = WINDOW_CENTERED;
    private boolean fullscreen;
    private double targetFPS = 60.0;
    private double targetUPS = 60.0;
    private double renderNS = 1000000000.0 / targetFPS;
    private double updateNS = 1000000000.0 / targetUPS;
    private boolean visible = true;
    private boolean mouseDisabled = false;
    private boolean mouseHidden = false;

    private GLPanel currentGameState;

    public GLFrame() {
        this(false);
    }

    public GLFrame(boolean fullscreen) {
        System.setProperty("java.awt.headless", "true");
        Thread.currentThread().setName("SwingGL | render");
        this.fullscreen = fullscreen;
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        if (glfwInit() != GL11.GL_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        glfwWindowHint(GLFW_DECORATED, GL_TRUE);

        // Configure callbacks
        keyCallback = new Keyboard();
        cursorPosCallback = new Mouse.CursorPos();
        mouseButtonCallback = new Mouse.MouseButton();
        scrollCallback = new Mouse.Scroll();
    }

    public void run() {
        if (fullscreen) {
            window = glfwCreateWindow(windowWidth, windowHeight, title, glfwGetPrimaryMonitor(), NULL);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (!(windowWidth == vidmode.width() && windowHeight == vidmode.height())) {
                Debug.println("GLFWVidMode [" + windowWidth + ", " + windowHeight + "] not available, switching to GLFWVidMode [" + vidmode.width() + ", "
                        + vidmode.height() + "]", Debug.ANSI_YELLOW);
                windowWidth = vidmode.width();
                windowHeight = vidmode.height();
            }
        } else
            window = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        if (windowPosition == WINDOW_POSITION_CUSTOM && !fullscreen)
            glfwSetWindowPos(window, windowX, windowY);
        else if (!fullscreen)
            updateWindowPosition();

        glfwSetKeyCallback(window, keyCallback);
        glfwSetCursorPosCallback(window, cursorPosCallback);
        glfwSetCursorEnterCallback(window, cursorEnterCallback);
        glfwSetMouseButtonCallback(window, mouseButtonCallback);
        glfwSetScrollCallback(window, scrollCallback);
        glfwSetWindowPosCallback(window, windowPosCallback);
        glfwSetWindowSizeCallback(window, windowSizeCallback);
        glfwSetWindowCloseCallback(window, windowCloseCallback);
        glfwSetWindowRefreshCallback(window, windowRefreshCallback);
        glfwSetWindowFocusCallback(window, windowFocusCallback);
        glfwSetWindowIconifyCallback(window, windowIconifyCallback);
        glfwSetDropCallback(window, dropCallback);

        if (mouseDisabled)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        else if (mouseHidden)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        else
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        GL.createCapabilities();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, windowWidth, windowHeight, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClearColor(backgroundColor.getRed() / 255f, backgroundColor.getGreen() / 255f, backgroundColor.getBlue() / 255f,
                backgroundColor.getAlpha() / 255f);

        if (visible)
            glfwShowWindow(window);

        new Thread(new Update(), "SwingGL | update").start();
        long now = System.nanoTime();
        long lastTime = now;
        double deltaR = 0.0;
        long lastRender = now;

        running = true;

        while (running) {
            if (glfwWindowShouldClose(window) == GL_TRUE)
                running = false;

            now = System.nanoTime();
            deltaR += (now - lastTime) / renderNS;
            lastTime = now;

            if (deltaR >= 1.0) {
                renderDelta = (now - lastRender) / 1000000000.0f;
                render(renderDelta);
                lastRender = now;
                deltaR--;
            }
        }

        if (currentGameState != null)
            currentGameState.dispose();

        try {
            glfwDestroyWindow(window);
            keyCallback.release();
            cursorPosCallback.release();
            mouseButtonCallback.release();
            scrollCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void render(float delta) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (currentGameState != null) {
            if (currentGameState.initialized)
                currentGameState.render(this, delta);
            else
                currentGameState.init(this);
        }

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    private void updateWindowPosition() {
        if (!(windowPosition == WINDOW_CENTERED || windowPosition == WINDOW_TOP_LEFT || windowPosition == WINDOW_TOP_RIGHT || windowPosition ==
                WINDOW_BOTTOM_LEFT || windowPosition == WINDOW_BOTTOM_RIGHT))
            throw new RuntimeException("Window position: " + windowPosition + " is not a valid SwingGL window position");
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (windowPosition == WINDOW_CENTERED)
            glfwSetWindowPos(window, windowX = ((vidmode.width() / 2) - (windowWidth / 2)), windowY = ((vidmode.height() / 2) - (windowHeight / 2)));
        else if (windowPosition == WINDOW_TOP_LEFT)
            glfwSetWindowPos(window, windowX = 0, windowY = 0);
        else if (windowPosition == WINDOW_TOP_RIGHT)
            glfwSetWindowPos(window, windowX = (vidmode.width() - windowWidth), windowY = 0);
        else if (windowPosition == WINDOW_BOTTOM_LEFT)
            glfwSetWindowPos(window, windowX = 0, windowY = (vidmode.height() - windowHeight));
        else if (windowPosition == WINDOW_BOTTOM_RIGHT)
            glfwSetWindowPos(window, windowX = (vidmode.width() - windowWidth), windowY = (vidmode.height() - windowHeight));
    }

    private GLFrame getGLFrame() {
        return this;
    }

    public void disableMouse() {
        mouseDisabled = true;
        if (window != 0L)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void hideMouse() {
        mouseHidden = true;
        if (window != 0L)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }

    public void showMouse() {
        mouseHidden = false;
        if (window != 0L)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }

    public void enableMouse() {
        mouseDisabled = false;
        if (window != 0L)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public void exit() {
        glfwSetWindowShouldClose(window, GL_TRUE);
    }

    public void iconify() {
        if (window != 0L)
            glfwIconifyWindow(window);
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public int isIconified() {
        return glfwGetWindowAttrib(window, GLFW_ICONIFIED);
    }

    public int isInFocus() {
        return glfwGetWindowAttrib(window, GLFW_FOCUSED);
    }

    public boolean isMouseDisabled() {
        return mouseDisabled;
    }

    public boolean isMouseHidden() {
        return mouseHidden;
    }

    public boolean isVisible() {
        return visible;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public String getClipboardString() {
        if (window != 0L)
            return glfwGetClipboardString(window);
        else
            Debug.println("GLFW Window must be initialized first!", Debug.ANSI_YELLOW);
        return "";
    }

    public Point getPosition() {
        return new Point(windowX, windowY);
    }

    public Dimension getSize() {
        return new Dimension(windowWidth, windowHeight);
    }

    public String getTitle() {
        return title;
    }

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        if (window != 0L)
            GL11.glClearColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public void setClipboardString(String clipboardString) {
        if (window != 0L)
            glfwSetClipboardString(window, clipboardString);
        else
            Debug.println("GLFW Window must be initialized first!", Debug.ANSI_YELLOW);
    }

    public void setContextVersionMajor(int majorVersion) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot set majorVersion");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, majorVersion);
    }

    public void setContextVersionMinor(int minorVersion) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot set minorVersion");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, minorVersion);
    }

    public void setCursorEnterCallback(GLFWCursorEnterCallback cursorEnterCallback) {
        this.cursorEnterCallback = cursorEnterCallback;
        if (window != 0)
            glfwSetCursorEnterCallback(window, cursorEnterCallback);
    }

    public void setCursorPosCallback(GLFWCursorPosCallback cursorPosCallback) {
        this.cursorPosCallback = cursorPosCallback;
        if (window != 0L)
            glfwSetCursorPosCallback(window, cursorPosCallback);
    }

    public void setDecorated(boolean decorated) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot set decorated");
        if (decorated)
            glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
    }

    public void setDropCallback(GLFWDropCallback dropCallback) {
        if (window != 0)
            glfwSetDropCallback(window, dropCallback);
    }

    public void setKeyCallback(GLFWKeyCallback keyCallback) {
        this.keyCallback = keyCallback;
        if (window != 0L)
            glfwSetKeyCallback(window, keyCallback);
    }

    public void setMonitorCallback(GLFWMonitorCallback monitorCallback) {
        glfwSetMonitorCallback(monitorCallback);
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallback mouseButtonCallback) {
        this.mouseButtonCallback = mouseButtonCallback;
        if (window != 0L)
            glfwSetMouseButtonCallback(window, mouseButtonCallback);
    }

    public void setMultisampling(int samples) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot send new samples");
        glfwWindowHint(GLFW_SAMPLES, samples);
    }

    public void setPanel(GLPanel panel) {
        if (currentGameState != null)
            currentGameState.dispose();
        currentGameState = panel;
    }

    public void setPosition(int position) {
        this.windowPosition = position;
        if (window != 0L && !fullscreen)
            updateWindowPosition();
    }

    public void setPosition(int windowX, int windowY) {
        this.windowX = windowX;
        this.windowY = windowY;
        windowPosition = WINDOW_POSITION_CUSTOM;
        if (window != 0L)
            glfwSetWindowPos(window, windowX, windowY);
    }

    public void setRefreshRate(int refreshRate) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot send new refreshRate");
        glfwWindowHint(GLFW_REFRESH_RATE, refreshRate);
    }

    public void setResizable(boolean resizable) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot send new resizable");
        if (resizable)
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
    }

    public void setScrollCallback(GLFWScrollCallback scrollCallback) {
        this.scrollCallback = scrollCallback;
        if (window != 0L)
            glfwSetScrollCallback(window, scrollCallback);
    }

    public void setSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
        if (fullscreen) {
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (!(windowWidth == vidmode.width() && windowHeight == vidmode.height())) {
                Debug.println("GLFWVidMode [" + windowWidth + ", " + windowHeight + "] not available, switching to GLFWVidMode [" + vidmode.width() + ", "
                        + vidmode.height() + "]", Debug.ANSI_YELLOW);
                windowWidth = vidmode.width();
                windowHeight = vidmode.height();
            }
        }

        if (window != 0L) {
            glfwSetWindowSize(window, windowWidth, windowHeight);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, windowWidth, windowHeight, 0, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }
    }

    public void setTargetFPS(int targetFPS) {
        this.targetFPS = targetFPS;
        renderNS = 1000000000.0 / targetFPS;
    }

    public void setTargetUPS(int targetUPS) {
        this.targetUPS = targetUPS;
        updateNS = 1000000000.0 / targetUPS;
    }

    public void setTitle(String title) {
        this.title = title;
        if (window != 0L)
            glfwSetWindowTitle(window, title);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (window != 0L) {
            if (visible)
                glfwShowWindow(window);
            else
                glfwHideWindow(window);
        } else {
            if (visible)
                glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
            else
                glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        }
    }

    public void setWindowCloseCallback(GLFWWindowCloseCallback windowCloseCallback) {
        this.windowCloseCallback = windowCloseCallback;
        if (window != 0L)
            glfwSetWindowCloseCallback(window, windowCloseCallback);
    }

    public void setWindowFocusCallback(GLFWWindowFocusCallback windowFocusCallback) {
        this.windowFocusCallback = windowFocusCallback;
        if (window != 0L)
            glfwSetWindowFocusCallback(window, windowFocusCallback);
    }

    public void setWindowIconifyCallback(GLFWWindowIconifyCallback windowIconifyCallback) {
        this.windowIconifyCallback = windowIconifyCallback;
        if (window != 0L)
            glfwSetWindowIconifyCallback(window, windowIconifyCallback);
    }

    public void setWindowPosCallback(GLFWWindowPosCallback windowPosCallback) {
        this.windowPosCallback = windowPosCallback;
        if (window != 0L)
            glfwSetWindowPosCallback(window, windowPosCallback);
    }

    public void setWindowRefreshCallback(GLFWWindowRefreshCallback windowRefreshCallback) {
        this.windowRefreshCallback = windowRefreshCallback;
        if (window != 0L)
            glfwSetWindowRefreshCallback(window, windowRefreshCallback);
    }

    public void setWindowSizeCallback(GLFWWindowSizeCallback windowSizeCallback) {
        this.windowSizeCallback = windowSizeCallback;
        if (window != 0L)
            glfwSetWindowSizeCallback(window, windowSizeCallback);
    }

    private class Update implements Runnable {

        @Override
        public void run() {
            long now = System.nanoTime();
            long lastTime = now;
            double deltaT = 0.0;
            long lastUpdate = now;

            while (running) {
                now = System.nanoTime();
                deltaT += (now - lastTime) / updateNS;
                lastTime = now;

                if (deltaT >= 1.0) {
                    update(updateDelta);
                    updateDelta = (now - lastUpdate) / 1000000000.0f;
                    lastUpdate = now;
                    deltaT = 0.0;
                }
            }
        }

        private void update(float delta) {
            if (window != 0L) {
                if (currentGameState != null) {
                    if (currentGameState.initialized)
                        currentGameState.update(getGLFrame(), delta);
                }
            }
        }
    }
}