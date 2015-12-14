package test.backend;

import com.swinggl.backend.Debug;
import com.swinggl.backend.TrueTypeFont;
import com.swinggl.elements.GLFrame;
import com.swinggl.elements.GLPanel;

import java.awt.*;

/**
 * Created by tyty5949 on 12/14/15 at 4:06 PM.
 * Project: SwingGL
 */
public class TrueTypeFontTest extends GLPanel{

    public static void main(String[] args) {
        GLFrame glFrame = new GLFrame();
        glFrame.setTitle("Frame Test");
        glFrame.setSize(640f, 640f);
        glFrame.setWindowLocation(GLFrame.WINDOW_CENTERED);
        glFrame.setResizable(false);
        glFrame.bindKeyboard();
        glFrame.bindMouse();
        glFrame.enableDebugging(true);
        glFrame.setPanel(new TrueTypeFontTest());
        glFrame.run();
    }

    private TrueTypeFont font;

    @Override
    public void initialize(GLFrame frame) {
        font = new TrueTypeFont("res/test/font.ttf", 24);

        initialized = true;
        Debug.println("Finished Initializations");
    }

    @Override
    public void update(GLFrame frame, double delta) {

    }

    @Override
    public void render(GLFrame frame, double delta) {
        font.drawString("Test String - abcdefghijklmnopqrstuvwxyz", 10, 10, Color.GREEN);
    }
}
