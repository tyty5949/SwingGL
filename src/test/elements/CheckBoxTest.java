package test.elements;

import com.swinggl.backend.Texture;
import com.swinggl.elements.GLCheckBox;
import com.swinggl.elements.GLFrame;
import com.swinggl.elements.GLPanel;
import com.swinggl.util.SpriteSheet;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created on 12/17/2015.
 */
public class CheckBoxTest extends GLPanel {
    public static void main(String[] args) {
        GLFrame glFrame = new GLFrame(false);
        glFrame.setSize(1280, 720);
        glFrame.setBackgroundColor(Color.GRAY);
        glFrame.setTitle("This is a test title");
        glFrame.setPosition(GLFrame.WINDOW_CENTERED);
        glFrame.setPanel(new CheckBoxTest());
        glFrame.setResizable(true);
        glFrame.setMultisampling(16);
        glFrame.enableDebugging();
        glFrame.run();
    }

    private Texture tex;
    private GLCheckBox checkBox;

    @Override
    public void init(final GLFrame frame) {
        tex = new Texture("res/test/backend/checkbox.png");
        checkBox = new GLCheckBox(100f, 100f, 32f, 32f, false, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
            }
        }, SpriteSheet.getGLCheckBoxCoords(0f, 0f, 32f, 32f, tex));

        initialized = true;
    }

    @Override
    public void update(GLFrame frame, float delta) {
        checkBox.update();
    }

    @Override
    public void render(GLFrame frame, float delta) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        tex.bind();
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glBegin(GL11.GL_QUADS);
        checkBox.render();
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void dispose() {

    }
}
