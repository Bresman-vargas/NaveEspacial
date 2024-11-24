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
        private BitmapFont difficultyFont;
        
	private float timeElapsed;
        private String difficulty = "No seleccionada";  // Dificultad predeterminada
        private DificultadStrategy dificultad;
        private boolean difficultySelected = false;

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
                
                difficultyFont = new BitmapFont();
                difficultyFont.setColor(Color.LIGHT_GRAY);
                difficultyFont.getData().setScale(1.5f);
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

                
                // Instrucciones para seleccionar dificultad
                difficultyFont.draw(game.getBatch(), "Selecciona la dificultad: 1. Fácil / 2. Media / 3. Difícil", 200, 350);
                difficultyFont.draw(game.getBatch(), "Dificultad seleccionada: " + difficulty, 200, 300);
                
                // Instrucciones con parpadeo suave
                instructionFont.setColor(instructionColor); // Aplica el color de parpadeo
                instructionFont.draw(game.getBatch(), "Pincha ENTER para comenzar ...", 200, 200);

		game.getBatch().end();

		// Detecta la selección de dificultad (1 o 3)
                if (!difficultySelected) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                        dificultad = new DificultadFacil();
                        difficulty = "Fácil";
                        difficultySelected = true;
                        
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                        dificultad = new DificultadMedia();
                        difficulty = "Media";
                        difficultySelected = true;
                        
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                        dificultad = new DificultadDificil();
                        difficulty = "Difícil";
                        difficultySelected = true;
                    }
                }

                // Si se presiona ENTER, asigna dificultad por defecto si no se ha seleccionado
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    if (dificultad == null) {
                        // Asume "Fácil" si no se ha seleccionado ninguna dificultad
                        dificultad = new DificultadFacil();
                    }
                    int ronda = 1; // Ronda inicial
                    int vidas = 3; // Número de vidas iniciales
                    int score = 0; // Puntuación inicial
                    int velXAsteroides = dificultad.calcularVelocidadAsteroides(ronda);
                    int velYAsteroides = dificultad.calcularVelocidadAsteroides(ronda);

                    Screen ss = new PantallaJuego(game, ronda, vidas, score, velXAsteroides, velYAsteroides, dificultad);
              
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
