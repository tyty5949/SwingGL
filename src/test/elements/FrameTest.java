package test.elements;

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
import com.swinggl.elements.GLFrame;
import com.swinggl.elements.GLPanel;
import com.swinggl.util.IOUtil;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * Created on 12/13/2015.
 */
public class FrameTest extends GLPanel {

    public static void main(String[] args) {
        GLFrame glFrame = new GLFrame(false);
        glFrame.setSize(1280, 720);
        glFrame.setTitle("This is a test title");
        glFrame.setPanel(new FrameTest());
        glFrame.run();
    }

    @Override
    public void init(GLFrame frame) {
        for(int i = 0; i < 10; i++) {
            long startTime = System.nanoTime();
            try{
                IOUtil.createBufferFromFile("res/test/backend/file.jpg",(16*1024));
            }catch (IOException e){
                throw new RuntimeException(e);
            }
            long endTime = System.nanoTime();
            Debug.println("" + ((endTime - startTime) / 1000000000.0));
        }

        initialized = true;
    }

    @Override
    public void update(GLFrame frame, float delta) {

    }

    @Override
    public void render(GLFrame frame, float delta) {
        GL11.glColor3f(0f, 1f, 1f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0f, 0f);
        GL11.glVertex2f(100f, 0f);
        GL11.glVertex2f(100f, 100f);
        GL11.glVertex2f(0f, 100f);
        GL11.glEnd();
    }

    @Override
    public void dispose() {

    }
}
