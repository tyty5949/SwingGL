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
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created on 12/13/2015.
 * http://www.glfw.org/docs/latest/index.html
 * <p/>
 * This elements houses everything necessary to make a complete window in LWJGL using GLFW. It doesn't simplify much and knowledge of LWJGL and GLFW are still
 * required. This can be thought of as a wrapper of LWJGL which wraps OpenGL and GLFW.
 * <p/>
 * ------------------------------------------
 * GLFW TODO
 * [X] Callbacks
 * [X] Cursors
 * [ ] Cursor Object (Cursor look)
 * [X] Contexts
 * [ ] Monitors
 * [X] Windows
 * [X] Input
 * [X] Joysticks
 * [N] Oculus Rift (Only for 3D)
 */
public class GLFrame {

    public static final String VERSION = "1.0.0a";

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
    private long secondWindowHandle;
    private boolean running = false;
    private float updateDelta = 0.0f;
    private float renderDelta = 0.0f;

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

    /**
     * Constructor that builds a default GLFrame
     */
    public GLFrame() {
        this(false, NULL);
    }

    /**
     * Constructor that builds a default GLFrame which links graphics data with another GLFrame
     *
     * @param secondWindowHandle - The other window handle that this GLFrame will be linked to.
     */
    public GLFrame(long secondWindowHandle) {
        this(false, secondWindowHandle);
    }

    /**
     * Constructor that builds a default fullscreen GLFrame
     *
     * @param fullscreen - Sets whether or not the frame is fullscreen
     */
    public GLFrame(boolean fullscreen) {
        this(fullscreen, NULL);
    }

    /**
     * Constructor that builds a default fullscrene GLFrame that links graphics data with another GLFrame
     *
     * @param fullscreen         - Sets whether or not the frame is fullscreen
     * @param secondWindowHandle - The other window handle that this GLFrame will be linked to.
     */
    public GLFrame(boolean fullscreen, long secondWindowHandle) {
        System.setProperty("java.awt.headless", "true");
        System.setProperty("org.lwjgl.util.Debug", "true");
        Debug.enabled = false;
        Thread.currentThread().setName("SwingGL | render");
        this.fullscreen = fullscreen;
        this.secondWindowHandle = secondWindowHandle;
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

    /**
     * This method starts the both the render and update loops. The thread that this is called on will become the render loop and occupy that thread entirely.
     * All glfwWindowHint's must be called before this is called while all other window attributes can be altered while the loop is running including changing
     * the FPS and UPS.
     */
    public void run() {
        if (fullscreen) {
            window = glfwCreateWindow(windowWidth, windowHeight, title, glfwGetPrimaryMonitor(), secondWindowHandle);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (!(windowWidth == vidmode.width() && windowHeight == vidmode.height())) {
                Debug.println("GLFWVidMode [" + windowWidth + ", " + windowHeight + "] not available, switching to GLFWVidMode [" + vidmode.width() + ", "
                        + vidmode.height() + "]", Debug.ANSI_YELLOW);
                windowWidth = vidmode.width();
                windowHeight = vidmode.height();
            }
        } else
            window = glfwCreateWindow(windowWidth, windowHeight, title, NULL, secondWindowHandle);
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

        Debug.initialize();

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
        Debug.renderDelta = delta;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        long startTime = 0L;
        if(Debug.enabled)
            startTime = System.nanoTime();

        if (currentGameState != null) {
            if (currentGameState.initialized)
                currentGameState.render(this, delta);
            else
                currentGameState.init(this);
        }

        if(Debug.enabled)
            Debug.renderTime = System.nanoTime() - startTime;

        Debug.render(this);

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

    /**
     * Disables the debugging views and interfaces
     */
    public void disableDebugging() {
        Debug.enabled = false;
    }

    /**
     * Locks the mouse to the center of the screen and make the cursor invisible
     */
    public void disableMouse() {
        mouseDisabled = true;
        if (window != 0L)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    /**
     * Makes the cursor invisible
     */
    public void hideMouse() {
        mouseHidden = true;
        if (window != 0L)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }

    /**
     * Makes the mouse visible again (sets the mouse to its default state)
     */
    public void showMouse() {
        mouseHidden = false;
        if (window != 0L)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }

    /**
     * Makes the mouse visible again (sets the mouse to its default state)
     */
    public void enableMouse() {
        mouseDisabled = false;
        if (window != 0L)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    /**
     * Enables the debugging views and interfaces
     */
    public void enableDebugging() {
        Debug.enabled = true;
    }

    /**
     * Closes the GLFrame and cleans up resources
     */
    public void exit() {
        glfwSetWindowShouldClose(window, GL_TRUE);
    }

    /**
     * Minimizes the window
     */
    public void iconify() {
        if (window != 0L)
            glfwIconifyWindow(window);
    }

    /**
     * Tells whether or not the window is fullscreen
     *
     * @return - Whether or not the window is fullscreen
     */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /**
     * Tells whether or not the window is minimized
     *
     * @return - Whether or not the window is minimized
     */
    public int isIconified() {
        return glfwGetWindowAttrib(window, GLFW_ICONIFIED);
    }

    /**
     * Tells whether or not the window is in focus. A window is in focus if it was the last window that recieved input.
     *
     * @return - GLFW_TRUE if the window is in focus and GLFW_FALSE otherwise
     */
    public int isInFocus() {
        return glfwGetWindowAttrib(window, GLFW_FOCUSED);
    }


    /**
     * Checks if a joystick button or axis is present
     *
     * @param joystick - The joystick to check
     * @return - GLFW_TRUE if the the joystick exists and GLFW_FALSE otherwise
     */
    public int isJoystickPresent(int joystick) {
        if (window != 0L)
            return glfwJoystickPresent(joystick);
        else
            Debug.println("GLFW Window must be initialized first!", Debug.ANSI_YELLOW);
        return GLFW_FALSE;
    }

    /**
     * Tells whether or not the mouse is disabled
     *
     * @return - true if mouse is disabled and false otherwise
     */
    public boolean isMouseDisabled() {
        return mouseDisabled;
    }

    /**
     * Tells whether or not the mouse is hidden
     *
     * @return - true if mouse is hidden and false otherwise
     */
    public boolean isMouseHidden() {
        return mouseHidden;
    }

    /**
     * Tells whether or not the window is visible
     *
     * @return - true if window is visible and false otherwise
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Gives the background color as a java.awt.Color
     *
     * @return - Gives the background color
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Gets the text that is currently stored in the clipboard
     *
     * @return - The text in the clipboard
     */
    public String getClipboardString() {
        if (window != 0L)
            return glfwGetClipboardString(window);
        else
            Debug.println("GLFW Window must be initialized first!", Debug.ANSI_YELLOW);
        return "";
    }

    /**
     * Gets the data about the position of the axis of the selected joystick
     *
     * @param joystick - The joystick
     * @return - The data the axis gives about it's position
     */
    public FloatBuffer getJoystickAxes(int joystick) {
        if (window != 0L)
            glfwGetJoystickAxes(joystick);
        else
            Debug.println("GLFW Window must be initialized first!", Debug.ANSI_YELLOW);
        return null;
    }

    /**
     * Gets the data about the selected joystick button
     *
     * @param joystick - The joystick
     * @return - The data the button gives about it's position
     */
    public ByteBuffer getJoystickButtons(int joystick) {
        if (window != 0L)
            return glfwGetJoystickButtons(joystick);
        else
            Debug.println("GLFW Window must be initialized first!", Debug.ANSI_YELLOW);
        return null;
    }

    /**
     * Gets the physical name of the joystick axis or button so that it can be identified on the device
     *
     * @param joystick - The joystick
     * @return - The name of the selected joystick
     */
    public String getJoystickName(int joystick) {
        if (window != 0L)
            return glfwGetJoystickName(joystick);
        else
            Debug.println("GLFW Window must be initialized first!", Debug.ANSI_YELLOW);
        return "";
    }

    /**
     * Gives the GLPanel that is currently be rendered and updated by the frame
     *
     * @return - The panel
     */
    public GLPanel getPanel() {
        return currentGameState;
    }

    /**
     * Gives the position of the top-left corner of the window as a java.awt.Point
     *
     * @return - The window position
     */
    public Point getPosition() {
        return new Point(windowX, windowY);
    }

    /**
     * Gives the size fo the window from frame edge to edge as a java.awt.Dimension
     *
     * @return - The size of the window
     */
    public Dimension getSize() {
        return new Dimension(windowWidth, windowHeight);
    }

    /**
     * Gives the title of the window as it appears to the OS
     *
     * @return - The title of the window
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the background color of the window
     *
     * @param color - The background color
     */
    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        if (window != 0L)
            GL11.glClearColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    /**
     * Sets the text data that is saved in the clipboard
     *
     * @param clipboardString - The text to be saved
     */
    public void setClipboardString(String clipboardString) {
        if (window != 0L)
            glfwSetClipboardString(window, clipboardString);
        else
            Debug.println("GLFW Window must be initialized first!", Debug.ANSI_YELLOW);
    }

    /**
     * Sets the major version of OpenGL that is to be used by the window
     *
     * @param majorVersion - The major version
     */
    public void setContextVersionMajor(int majorVersion) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot set majorVersion");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, majorVersion);
    }

    /**
     * Sets the minor version of OpenGL that is to be used by the window
     *
     * @param minorVersion - The minor version
     */
    public void setContextVersionMinor(int minorVersion) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot set minorVersion");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, minorVersion);
    }

    /**
     * Sets the GLFWCursorEnterCallback for the window
     *
     * @param cursorEnterCallback - The callback
     */
    public void setCursorEnterCallback(GLFWCursorEnterCallback cursorEnterCallback) {
        this.cursorEnterCallback = cursorEnterCallback;
        if (window != 0)
            glfwSetCursorEnterCallback(window, cursorEnterCallback);
    }

    /**
     * Sets the GLFWCursorPosCallback for the window (default com.swinggl.backend.Mouse)
     *
     * @param cursorPosCallback - The callback
     */
    public void setCursorPosCallback(GLFWCursorPosCallback cursorPosCallback) {
        this.cursorPosCallback = cursorPosCallback;
        if (window != 0L)
            glfwSetCursorPosCallback(window, cursorPosCallback);
    }

    /**
     * Whether or not the window is to be decorated. A decorated window has a frame, icon, title, and close buttons on it, a undecorated frame does not.
     *
     * @param decorated - Whether or not the window will be decorated
     */
    public void setDecorated(boolean decorated) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot set decorated");
        if (decorated)
            glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
    }

    /**
     * Sets the GLFWDropCallback for the window. The dropcallback is invoked when a file is dropped onto the window
     *
     * @param dropCallback - The callback
     */
    public void setDropCallback(GLFWDropCallback dropCallback) {
        if (window != 0)
            glfwSetDropCallback(window, dropCallback);
    }

    /**
     * Sets the GLFWKeyCallback for the window
     *
     * @param keyCallback - The callback
     */
    public void setKeyCallback(GLFWKeyCallback keyCallback) {
        this.keyCallback = keyCallback;
        if (window != 0L)
            glfwSetKeyCallback(window, keyCallback);
    }

    /**
     * Sets the GLFWMonitorCallback for the window
     *
     * @param monitorCallback - The callback
     */
    public void setMonitorCallback(GLFWMonitorCallback monitorCallback) {
        glfwSetMonitorCallback(monitorCallback);
    }

    /**
     * Sets the GLFWMouseButtonCallback for the window
     *
     * @param mouseButtonCallback - The callback
     */
    public void setMouseButtonCallback(GLFWMouseButtonCallback mouseButtonCallback) {
        this.mouseButtonCallback = mouseButtonCallback;
        if (window != 0L)
            glfwSetMouseButtonCallback(window, mouseButtonCallback);
    }

    /**
     * Sets the level of multisampling the window uses for rendering
     *
     * @param samples - The number of samples
     */
    public void setMultisampling(int samples) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot send new samples");
        glfwWindowHint(GLFW_SAMPLES, samples);
    }

    /**
     * Sets the GLPanel that the window is currently rendering and updating. The render thread will call initialize() the frame after it is added.
     *
     * @param panel - The new GLPanel
     */
    public void setPanel(GLPanel panel) {
        if (currentGameState != null)
            currentGameState.dispose();
        currentGameState = panel;
    }

    /**
     * Sets the window to a position on the screen. Use the GLFrame constants for this method (WINDOW_CENTERED, WINDOW_BOTTOM_LEFT, etc)
     *
     * @param position - The GLFrame constant for the position
     */
    public void setPosition(int position) {
        this.windowPosition = position;
        if (window != 0L && !fullscreen)
            updateWindowPosition();
    }

    /**
     * Sets a manual position of the top-left corner of the window.
     *
     * @param windowX - The x position of the window
     * @param windowY - The y position of the window
     */
    public void setPosition(int windowX, int windowY) {
        this.windowX = windowX;
        this.windowY = windowY;
        windowPosition = WINDOW_POSITION_CUSTOM;
        if (window != 0L)
            glfwSetWindowPos(window, windowX, windowY);
    }

    /**
     * Sets the refresh rate of the window. Refresh rates will be limited to the physical capabilities of the monitor.
     *
     * @param refreshRate - The refresh rate
     */
    public void setRefreshRate(int refreshRate) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot send new refreshRate");
        glfwWindowHint(GLFW_REFRESH_RATE, refreshRate);
    }

    /**
     * Sets whether or not the window will be resizable
     *
     * @param resizable - Whether or not the window is resizable
     */
    public void setResizable(boolean resizable) {
        if (window != 0L)
            throw new RuntimeException("GLFW window already initialized, cannot send new resizable");
        if (resizable)
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        else
            glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
    }

    /**
     * Sets the GLFWScrollCallback for the window. (default com.swinggl.backend.Mouse)
     *
     * @param scrollCallback - The callback
     */
    public void setScrollCallback(GLFWScrollCallback scrollCallback) {
        this.scrollCallback = scrollCallback;
        if (window != 0L)
            glfwSetScrollCallback(window, scrollCallback);
    }

    /**
     * Sets the size of the window from frame edge to edge. If the window is fullscreen some sizes are limited by graphical capabilities.
     *
     * @param width  - The width of the window
     * @param height - The height of the window
     */
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

    /**
     * Sets the FPS target for the render loop
     *
     * @param targetFPS - The target FPS
     */
    public void setTargetFPS(int targetFPS) {
        this.targetFPS = targetFPS;
        renderNS = 1000000000.0 / targetFPS;
    }

    /**
     * Sets the UPS target for the update loop
     *
     * @param targetUPS - The target UPS
     */
    public void setTargetUPS(int targetUPS) {
        this.targetUPS = targetUPS;
        updateNS = 1000000000.0 / targetUPS;
    }

    /**
     * Sets the title shown by the window.
     *
     * @param title - The window title
     */
    public void setTitle(String title) {
        this.title = title;
        if (window != 0L)
            glfwSetWindowTitle(window, title);
    }

    /**
     * Sets whether or notthe window to be visible on the screen (the icon will still be visible on task bars when not visible)
     *
     * @param visible - Whether or not the window is to be visible
     */
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

    /**
     * Sets the GLFWWindowCloseCallback for the window
     *
     * @param windowCloseCallback - The callback
     */
    public void setWindowCloseCallback(GLFWWindowCloseCallback windowCloseCallback) {
        this.windowCloseCallback = windowCloseCallback;
        if (window != 0L)
            glfwSetWindowCloseCallback(window, windowCloseCallback);
    }

    /**
     * Sets the GLFWWindowFocusCallback for the window
     *
     * @param windowFocusCallback - The callback
     */
    public void setWindowFocusCallback(GLFWWindowFocusCallback windowFocusCallback) {
        this.windowFocusCallback = windowFocusCallback;
        if (window != 0L)
            glfwSetWindowFocusCallback(window, windowFocusCallback);
    }

    /**
     * Sets the GLFWWindowIconifyCallback for the window
     *
     * @param windowIconifyCallback - The callback
     */
    public void setWindowIconifyCallback(GLFWWindowIconifyCallback windowIconifyCallback) {
        this.windowIconifyCallback = windowIconifyCallback;
        if (window != 0L)
            glfwSetWindowIconifyCallback(window, windowIconifyCallback);
    }

    /**
     * Sets the GLFWWindowPosCallback for the window
     *
     * @param windowPosCallback - The callback
     */
    public void setWindowPosCallback(GLFWWindowPosCallback windowPosCallback) {
        this.windowPosCallback = windowPosCallback;
        if (window != 0L)
            glfwSetWindowPosCallback(window, windowPosCallback);
    }

    /**
     * Sets the GLFWWindowRefreshCallback for the window
     *
     * @param windowRefreshCallback - The callback
     */
    public void setWindowRefreshCallback(GLFWWindowRefreshCallback windowRefreshCallback) {
        this.windowRefreshCallback = windowRefreshCallback;
        if (window != 0L)
            glfwSetWindowRefreshCallback(window, windowRefreshCallback);
    }

    /**
     * Sets the GLFWWindowSizeCallback for the window
     *
     * @param windowSizeCallback - The callback
     */
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
            Debug.updateDelta = delta;

            if (window != 0L) {
                if (currentGameState != null) {
                    if (currentGameState.initialized)
                        currentGameState.update(getGLFrame(), delta);
                }
            }
        }
    }
}