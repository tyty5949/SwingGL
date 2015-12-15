package test.elements;

import com.swinggl.elements.GLFrame;
import com.swinggl.elements.GLPanel;
import org.lwjgl.opengl.GL11;

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
