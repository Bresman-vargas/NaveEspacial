package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private int cantAsteroidesGrandes;

    private Nave4 nave;
    private ArrayList<Ball2> balls = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score, int velXAsteroides, int velYAsteroides) {
        this.game = game;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        GameManager gm = GameManager.getInstance();
        this.cantAsteroides = calcularCantidadAsteroides(ronda);
        this.cantAsteroidesGrandes = calcularCantidadAsteroidesGrandes(ronda);
        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 640);

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();

        nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30,
                         new Texture(Gdx.files.internal("MainShip3.png")),
                         Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                         new Texture(Gdx.files.internal("Rocket2.png")),
                         Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        nave.setVidas(vidas);

        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = new Ball2(r.nextInt((int) Gdx.graphics.getWidth()),
                                  50 + r.nextInt((int) Gdx.graphics.getHeight() - 50),
                                  20 + r.nextInt(10), velXAsteroides + r.nextInt(4),
                                  velYAsteroides + r.nextInt(4),
                                  new Texture(Gdx.files.internal("aGreyMedium4.png")));
            balls.add(bb);
        }
        for (int i = 0; i < cantAsteroidesGrandes; i++) {
            Ball2 bb = new Ball2(r.nextInt((int) Gdx.graphics.getWidth()),
                                  50 + r.nextInt((int) Gdx.graphics.getHeight() - 50),
                                  20 + r.nextInt(10), velXAsteroides + r.nextInt(4),
                                  velYAsteroides + r.nextInt(4),
                                  new Texture(Gdx.files.internal("aGreyLarge.png")));
            balls.add(bb);
        }
    }

    private int calcularCantidadAsteroides(int ronda) {
        return 1 + (ronda - 1) * 2;
    }
    
    private int calcularCantidadAsteroidesGrandes(int ronda) {
        return 1 + (ronda - 1);
    }

    public void dibujaEncabezado() {
        GameManager gm = GameManager.getInstance();
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + gm.getRound();
        game.getFont().getData().setScale(2f);
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score: " + gm.getScore(), Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore: " + gm.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        dibujaEncabezado();

        if (!nave.estaHerido()) {
            actualizarBalas();
            actualizarAsteroides();
            verificarColisionesNaveAsteroides();
        }

        for (Bullet b : balas) {
            b.draw(batch);
        }

        nave.draw(batch, this);

        for (Ball2 b : balls) {
            b.draw(batch);
        }

        if (nave.estaDestruido()) {
            manejarGameOver();
        }

        batch.end();

        if (balls.isEmpty()) {
            iniciarSiguienteRonda();
        }
    }

    private void actualizarBalas() {
        for (int i = 0; i < balas.size(); i++) {
            Bullet b = balas.get(i);
            b.update(); // Actualiza la posición de la bala

            // Verifica colisiones con meteoritos
            for (int j = 0; j < balls.size(); j++) {
                Ball2 asteroide = balls.get(j);
                if (b.detectarColision(asteroide)) {
                    explosionSound.play(); // Reproduce el sonido de explosión
                    asteroide.alColisionar(b); // Llama al método de colisión en el asteroide
                    balls.remove(j); // Elimina el asteroide
                    j--; // Ajusta el índice después de eliminar
                    GameManager.getInstance().incrementScore(10); // Incrementa el puntaje
                    b.alColisionar(asteroide); // Marca la bala como destruida al colisionar
                }
            }

            // Elimina la bala si ha sido destruida
            if (b.isDestroyed()) {
                balas.remove(i); // Elimina la bala
                i--; // Ajusta el índice después de eliminar
            }
        }
    }


    private void actualizarAsteroides() {
        for (Ball2 ball : balls) {
            ball.update();
        }
    }

    private void verificarColisionesNaveAsteroides() {
        for (int i = 0; i < balls.size(); i++) {
            Ball2 asteroide = balls.get(i);
            if (nave.detectarColision(asteroide)) {
                // Maneja la colisión de la nave con el meteorito
                nave.alColisionar(asteroide); // Esto aplica daño a la nave
                balls.remove(i); // Elimina el meteorito de la lista
                i--; // Ajusta el índice después de eliminar
            }
        }
    }

    private void manejarGameOver() {
        GameManager gm = GameManager.getInstance();
        
        // Redirige a la pantalla de "Game Over"
        game.setScreen(new PantallaGameOver(game));

        // Verifica si el puntaje actual es mayor que el puntaje más alto registrado
        if (gm.getScore() > gm.getHighScore()) {
            gm.setHighScore(gm.getScore()); // Actualiza el high score
        }

        // Restablece el puntaje a 0
        gm.setScore(0);
        
        // Actualizar la ronda
        gm.setRound(0);

        // Restablece las vidas a 3
        nave.setVidas(3);
        dispose();
    }
        

    private void iniciarSiguienteRonda() {
        GameManager gm = GameManager.getInstance();
        
        // Verifica si el puntaje actual es mayor que el puntaje más alto registrado
        if (gm.getScore() > gm.getHighScore()) {
            gm.setHighScore(gm.getScore()); // Actualiza el high score
        }

        // Incrementa la ronda
        gm.setRound(gm.getRound() + 1);

        // Obtén el puntaje actual del GameManager (no se reinicia)
        int currentScore = gm.getScore();

        // Cambia a la siguiente pantalla con los valores actualizados
        game.setScreen(new PantallaJuego(game, gm.getRound(), nave.getVidas(), currentScore, velXAsteroides + 1, velYAsteroides + 1));
        dispose();
}

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        explosionSound.dispose();
        gameMusic.dispose();
    }
}
