package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;


public class PantallaMenu extends PantallaBase {

    private BitmapFont titleFont;
    private BitmapFont instructionFont;
    private BitmapFont difficultyFont;

    private float timeElapsed;
    private String difficulty = "No seleccionada";
    private DificultadStrategy dificultad;
    private boolean difficultySelected = false;

    public PantallaMenu(SpaceNavigation game) {
        super(game);
    }

    
    @Override
    protected void inicializar() {
        titleFont = new BitmapFont();
        titleFont.setColor(Color.SKY);
        titleFont.getData().setScale(3);

        instructionFont = new BitmapFont();
        instructionFont.getData().setScale(1.5f);

        difficultyFont = new BitmapFont();
        difficultyFont.setColor(Color.LIGHT_GRAY);
        difficultyFont.getData().setScale(1.5f);
    }

    @Override
    protected void actualizar(float delta) {
        timeElapsed += delta;

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (dificultad == null) {
                dificultad = new DificultadFacil();
            }
            game.setScreen(new PantallaJuego(game, 1, 3, 0, dificultad.calcularVelocidadAsteroides(1),
                    dificultad.calcularVelocidadAsteroides(1), dificultad));
            dispose();
        }
    }

    @Override
    protected void dibujar() {
        game.getBatch().begin();

        titleFont.draw(game.getBatch(), "Bienvenido a Space Navigation!", 200, 500);
        difficultyFont.draw(game.getBatch(), "Selecciona la dificultad: 1. Fácil / 2. Media / 3. Difícil", 200, 350);
        difficultyFont.draw(game.getBatch(), "Dificultad seleccionada: " + difficulty, 200, 300);

        Color instructionColor = Color.LIGHT_GRAY.cpy();
        instructionColor.a = 0.5f + 0.5f * (float) Math.sin(timeElapsed * 2);
        instructionFont.setColor(instructionColor);
        instructionFont.draw(game.getBatch(), "Presiona ENTER para comenzar ...", 200, 200);

        game.getBatch().end();
    }

    @Override
    public void dispose() {
        titleFont.dispose();
        instructionFont.dispose();
        difficultyFont.dispose();
    }
}
