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
    private int score;
    private int ronda;
    private int velXAsteroides; 
    private int velYAsteroides; 
    private int cantAsteroides;

    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    // Constructor de la clase PantallaJuego
    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
                         int velXAsteroides, int velYAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;

        // Calcular la cantidad de asteroides según la ronda
        this.cantAsteroides = calcularCantidadAsteroides(ronda);
        
        batch = game.getBatch();
        camera = new OrthographicCamera();    
        camera.setToOrtho(false, 800, 640);
        
        // Inicializar assets; música de fondo y efectos de sonido
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); 
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();
        
        // Cargar imagen de la nave, 64x64   
        nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30, 
                         new Texture(Gdx.files.internal("MainShip3.png")),
                         Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                         new Texture(Gdx.files.internal("Rocket2.png")), 
                         Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        
        // Crear asteroides
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = new Ball2(r.nextInt((int) Gdx.graphics.getWidth()),
                                  50 + r.nextInt((int) Gdx.graphics.getHeight() - 50),
                                  20 + r.nextInt(10), velXAsteroides + r.nextInt(4), 
                                  velYAsteroides + r.nextInt(4), 
                                  new Texture(Gdx.files.internal("aGreyMedium4.png")));     
            balls1.add(bb);
            balls2.add(bb);
        }
    }
    
    // Método para calcular la cantidad de asteroides según la ronda
    private int calcularCantidadAsteroides(int ronda) {
        return 1 + (ronda - 1) * 2; // 5 asteroides
    }

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score: " + this.score, Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore: " + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        // Limpiar la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        // Dibujar el encabezado
        dibujaEncabezado();

        // Comprobar si la nave no está herida
        if (!nave.estaHerido()) {
            // Actualizar y manejar colisiones de balas
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update(); // Actualizar posición y estado de la bala

                // Verificar colisiones entre balas y asteroides
                for (int j = 0; j < balls1.size(); j++) {
                    if (b.checkCollision(balls1.get(j))) {
                        explosionSound.play();
                        balls1.remove(j);
                        balls2.remove(j);
                        j--; // Decrementar j para evitar error de índice
                        score += 10; // Incrementar el puntaje
                    }
                }

                // Verificar si la bala está destruida
                if (b.isDestroyed()) {
                    balas.remove(i);
                    i--; // Decrementar i para evitar saltarse un elemento
                }
            }

            // Actualizar el movimiento de los asteroides
            for (Ball2 ball : balls1) {
                ball.update(); // Actualizar posición y estado de cada asteroide
            }

            // Manejar colisiones entre asteroides
            for (int i = 0; i < balls1.size(); i++) {
                Ball2 ball1 = balls1.get(i);
                for (int j = i + 1; j < balls2.size(); j++) { // j comienza en i + 1 para evitar comparar el mismo par
                    Ball2 ball2 = balls2.get(j);
                    ball1.checkCollision(ball2); // Verificar colisión entre asteroides
                }
            }
        }

        // Dibujar balas en pantalla
        for (Bullet b : balas) {
            b.draw(batch);
        }

        // Dibujar la nave
        nave.draw(batch, this);

        // Dibujar asteroides y verificar colisiones con la nave
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            b.draw(batch); // Dibujar el asteroide

            // Verificar colisión entre la nave y el asteroide
            if (nave.checkCollision(b)) {
                // Si hay colisión, se destruye el asteroide
                balls1.remove(i);
                balls2.remove(i);
                i--; // Decrementar i para evitar saltarse un elemento
            }
        }

        // Verificar si la nave ha sido destruida
        if (nave.estaDestruido()) {
            if (score > game.getHighScore())
                game.setHighScore(score); // Actualizar puntaje más alto

            // Cambiar a la pantalla de Game Over
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose(); // Liberar recursos
        }

        batch.end(); // Finalizar la batch de dibujo

        // Comprobar si el nivel ha sido completado
        if (balls1.size() == 0) {
            // Cambiar a la siguiente pantalla de juego con la nueva cantidad de asteroides
            Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                    velXAsteroides + 1, velYAsteroides + 1); // Aquí no se pasa cantAsteroides
            ss.resize(1200, 800);
            game.setScreen(ss); // Cambiar a la siguiente pantalla de juego
            dispose(); // Liberar recursos
        }
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
        // No implementado
    }

    @Override
    public void pause() {
        // No implementado
    }

    @Override
    public void resume() {
        // No implementado
    }

    @Override
    public void hide() {
        // No implementado
    }

    @Override
    public void dispose() {
        this.explosionSound.dispose();
        this.gameMusic.dispose();
    }
}
