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

/**
 * Created on 12/13/2015.
 * This class functions as a "gamestate" that allows any class to extend it and have methods ready for gameloop integration
 */
public abstract class GLPanel {

    /**
     * Whether or not the panel is initialized (must be set to true after initializations in init() are done)
     **/
    public boolean initialized = false;

    /**
     * Called when the GLPanel is first detected by the loop from the render thread which contains the OpenGL context (all initializations go here)
     *
     * @param frame - The frame which the GLPanel is on
     */
    public abstract void init(GLFrame frame);

    /**
     * Called from the update frame so updates are separate from renders
     *
     * @param frame - The frame which the GLPanel is on
     * @param delta - The time since the last update call
     */
    public abstract void update(GLFrame frame, float delta);

    /**
     * Called from the render thread which contains the OpenGL context
     *
     * @param frame - The frame which the GLPanel is on
     * @param delta - The time since the last render call
     */
    public abstract void render(GLFrame frame, float delta);

    /**
     * Called from the render thread when the GLFrame is closing
     */
    public abstract void dispose();
}
