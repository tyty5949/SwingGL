package test.elements;

import com.swinggl.backend.GLAction;
import com.swinggl.backend.GLActionListener;
import com.swinggl.backend.Texture;
import com.swinggl.elements.GLButton;
import com.swinggl.elements.GLFrame;
import com.swinggl.elements.GLPanel;
import com.swinggl.util.GLColor;
import com.swinggl.util.SpriteSheet;
import org.lwjgl.opengl.GL11;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 12/17/2015.
 */
public class ButtonTest extends GLPanel {

    public static void main(String[] args) {
        GLFrame glFrame = new GLFrame(false);
        glFrame.setSize(1280, 720);
        glFrame.setBackgroundColor(GLColor.GRAY);
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
    public void init(final GLFrame frame) {
        tex = new Texture("res/test/backend/button.png");
        button = new GLButton(300, 100, 256, 256, new GLActionListener() {
            @Override
            public void actionPerformed(GLAction e) {
                System.out.println(e.action());
            }
        }, SpriteSheet.getGLButtonCoords(0f, 0f, 256f, 256f, tex));

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
