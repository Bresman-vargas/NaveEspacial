package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaGameOver implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private BitmapFont gameOverFont;
	private BitmapFont instructionFont;
        private DificultadStrategy dificultad;

	public PantallaGameOver(SpaceNavigation game, DificultadStrategy dificultad) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1200, 800);

		// Configuración de fuentes: una para el texto principal y otra para instrucciones
		gameOverFont = new BitmapFont();
		gameOverFont.setColor(Color.RED);
		gameOverFont.getData().setScale(4);  // Escala la fuente para hacerla más grande

		instructionFont = new BitmapFont();
		instructionFont.setColor(Color.LIGHT_GRAY);
		instructionFont.getData().setScale(1.5f);  // Escala la fuente para un tamaño menor en las instrucciones
	}

	@Override
	public void render(float delta) {
		// Fondo específico para la pantalla de Game Over
		ScreenUtils.clear(0.05f, 0.05f, 0.1f, 1); // Fondo negro azulado para el espacio 

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		// Renderiza el texto "Game Over"
		gameOverFont.draw(game.getBatch(), "Game Over !!!", 400, 450);

		// Renderiza el texto de instrucciones
		instructionFont.draw(game.getBatch(), "Haz clic en cualquier lugar o presiona cualquier tecla para reiniciar", 300, 300);

		game.getBatch().end();

		// Reinicia el juego al hacer clic o presionar cualquier tecla
		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaJuego(game,1,3,0,1,1, dificultad);
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
		// Libera los recursos de fuente cuando ya no sean necesarios
		gameOverFont.dispose();
		instructionFont.dispose();
	}
}
