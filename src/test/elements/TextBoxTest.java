package test.elements;

import com.swinggl.backend.GLAction;
import com.swinggl.backend.GLActionListener;
import com.swinggl.backend.Texture;
import com.swinggl.backend.TrueTypeFont;
import com.swinggl.elements.GLFrame;
import com.swinggl.elements.GLPanel;
import com.swinggl.elements.GLTextBox;
import com.swinggl.util.SpriteSheet;
import org.lwjgl.opengl.GL11;

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
    private GLTextBox glTextBox;

    @Override
    public void init(GLFrame frame) {
        tex = new Texture("res/test/backend/textbox.png");
        font = new TrueTypeFont("res/fonts/swinggl.ttf", 64);
        glTextBox = new GLTextBox(300, 100, 200, 50, "Test", font, new GLActionListener() {
            @Override
            public void actionPerformed(GLAction e) {
                System.out.println(e.action());
            }
        }, SpriteSheet.getGLTextBoxCoords(0, 0, 400, 100, tex));

        initialized = true;
    }

    @Override
    public void update(GLFrame frame, float delta) {
        glTextBox.update();
    }

    @Override
    public void render(GLFrame frame, float delta) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        tex.bind();
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glBegin(GL11.GL_QUADS);
        glTextBox.render();
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void dispose() {

    }
}