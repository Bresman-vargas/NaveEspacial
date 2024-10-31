package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private BitmapFont titleFont;
	private BitmapFont instructionFont;
	private float timeElapsed;

	public PantallaMenu(SpaceNavigation game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1200, 800);

		// Configuración de fuentes
		titleFont = new BitmapFont();
		titleFont.setColor(Color.SKY);
		titleFont.getData().setScale(3); // Aumenta el tamaño de la fuente del título

		instructionFont = new BitmapFont();
		instructionFont.getData().setScale(1.5f);
	}

	@Override
	public void render(float delta) {
		// Fondo de pantalla de color oscuro
		ScreenUtils.clear(0.05f, 0.05f, 0.1f, 1); // Fondo negro azulado para el espacio

		// Control del parpadeo
		timeElapsed += delta;
		Color instructionColor = Color.LIGHT_GRAY.cpy();
		instructionColor.a = 0.5f + 0.5f * (float) Math.sin(timeElapsed * 2); // Ajusta el parpadeo de la opacidad

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();

		// Título "Bienvenido a Space Navigation!"
		titleFont.draw(game.getBatch(), "Bienvenido a Space Navigation!", 200, 500);

		// Instrucciones con parpadeo suave
		instructionFont.setColor(instructionColor); // Aplica el color de parpadeo
		instructionFont.draw(game.getBatch(), "Pincha en cualquier lado o presiona cualquier tecla para comenzar ...", 200, 300);

		game.getBatch().end();

		// Inicio del juego al presionar cualquier tecla o hacer clic
		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaJuego(game,1,3,0,1,1);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		}
	}

	@Override
	public void show() {}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		// Libera recursos de las fuentes
		titleFont.dispose();
		instructionFont.dispose();
	}
}
