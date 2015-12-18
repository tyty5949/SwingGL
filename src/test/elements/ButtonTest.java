package test.elements;

import com.swinggl.backend.Texture;
import com.swinggl.elements.GLButton;
import com.swinggl.elements.GLFrame;
import com.swinggl.elements.GLPanel;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Created on 12/17/2015.
 */
public class ButtonTest extends GLPanel {

    public static void main(String[] args) {
        GLFrame glFrame = new GLFrame(false);
        glFrame.setSize(1280, 720);
        glFrame.setBackgroundColor(Color.GRAY);
        glFrame.setTitle("This is a test title");
        glFrame.setPosition(GLFrame.WINDOW_CENTERED);
        glFrame.setPanel(new ButtonTest());
        glFrame.setResizable(true);
        glFrame.setMultisampling(16);
        glFrame.enableDebugging();
        glFrame.run();
    }

    private Texture tex;
    private GLButton button;

    @Override
    public void init(GLFrame frame) {
        tex = new Texture("res/test/backend/button.png");
        button = new GLButton(300, 100, 256, 256, new float[][]{
                {0f, 0f, .25f, 0f, .25f, 1f, 0f, 1f},
                {.25f, 0f, .5f, 0f, .5f, 1f, .25f, 1f},
                {.5f, 0f, .75f, 0f, .75f, 1f, .5f, 1f}});

        initialized = true;
    }

    @Override
    public void update(GLFrame frame, float delta) {
        button.update();
    }

    @Override
    public void render(GLFrame frame, float delta) {
        tex.bind();
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glBegin(GL11.GL_QUADS);
        button.renderButton();
        GL11.glEnd();
    }

    @Override
    public void dispose() {

    }
}
