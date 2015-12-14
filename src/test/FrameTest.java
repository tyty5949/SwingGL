package test;

import com.swinggl.elements.GLFrame;

/**
 * Created on 12/13/2015.
 */
public class FrameTest {

    public static void main(String[] args) {
        GLFrame glFrame = new GLFrame();
        glFrame.setTitle("Frame Test");
        glFrame.setSize(640f, 640f);
        glFrame.setWindowLocation(GLFrame.WINDOW_CENTERED);
        glFrame.setResizable(false);
        glFrame.bindKeyboard();
        glFrame.bindMouse();
        glFrame.enableDebugging(true);
        glFrame.initialize();
    }
}
