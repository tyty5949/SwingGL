package test.elements;

import com.swinggl.backend.Texture;
import com.swinggl.backend.TrueTypeFont;
import com.swinggl.elements.GLFrame;
import com.swinggl.elements.GLPanel;

/**
 * Created on 1/6/2016.
 */
public class TextBoxTest extends GLPanel {

    public static void main(String[] args) {
        GLFrame glFrame = new GLFrame(false);
        glFrame.setSize(1280, 720);
        glFrame.setTitle("This is a test title");
        glFrame.setPosition(GLFrame.WINDOW_CENTERED);
        glFrame.setPanel(new TextBoxTest());
        glFrame.setResizable(true);
        glFrame.setMultisampling(16);
        glFrame.enableDebugging();
        glFrame.run();
    }

    private Texture tex;
    private TrueTypeFont font;

    @Override
    public void init(GLFrame frame) {

        initialized = true;
    }

    @Override
    public void update(GLFrame frame, float delta) {

    }

    @Override
    public void render(GLFrame frame, float delta) {

    }

    @Override
    public void dispose() {

    }
}