package puppy.code;

/**
 *
 * @author javie
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public abstract class PantallaBase implements Screen {
    protected SpaceNavigation game;
    protected OrthographicCamera camera;
 
    public PantallaBase(SpaceNavigation game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1200, 800);
        inicializar(); // Método "gancho" para inicialización adicional
    }

    @Override
    public void render(float delta) {
        limpiarPantalla();
        actualizar(delta);// Método abstracto a implementar por las subclases
        dibujar();   // Método abstracto a implementar por las subclases
    }

    // Método común para limpiar la pantalla
    private void limpiarPantalla() {
        ScreenUtils.clear(0.05f, 0.05f, 0.1f, 1);
    }

    // Método abstracto para inicializar componentes específicos
    protected abstract void inicializar();

    // Método abstracto para actualizar la lógica de la pantalla
    protected abstract void actualizar(float delta);

    // Método abstracto para dibujar elementos específicos
    protected abstract void dibujar();

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
    
}