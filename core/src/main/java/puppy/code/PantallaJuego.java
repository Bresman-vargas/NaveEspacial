package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJuego extends PantallaBase {

    private Sound explosionSound;
    private Music gameMusic;

    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private int cantAsteroidesGrandes;

    private Nave4 nave;
    private ArrayList<Ball2> balls = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    private DificultadStrategy dificultad;

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score, int velXAsteroides, int velYAsteroides, DificultadStrategy dificultad) {
        super(game);
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.dificultad = dificultad;

        GameManager gm = GameManager.getInstance();
        this.cantAsteroides = dificultad.calcularCantidadAsteroides(gm.getRound());
        this.cantAsteroidesGrandes = dificultad.calcularCantidadAsteroidesGrande(gm.getRound());

        nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30,
                         new Texture(Gdx.files.internal("MainShip3.png")),
                         Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                         new Texture(Gdx.files.internal("Rocket2.png")),
                         Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        nave.setVidas(vidas);
        
                // Crear meteoritos pequeños y grandes según las cantidades determinadas por la dificultad
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = MeteoritoFactory.crearMeteorito("pequeño", velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4));
            balls.add(bb);
        }

        for (int i = 0; i < cantAsteroidesGrandes; i++) {
            Ball2 bb = MeteoritoFactory.crearMeteorito("grande", velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4));
            balls.add(bb);
        }

        inicializarAsteroides();
        inicializarSonidos();
    }
    

    @Override
    protected void inicializar() {
        // Configuraciones iniciales si son necesarias
    }


    @Override
    protected void actualizar(float delta) {
        if (!nave.estaHerido()) {
            actualizarBalas();
            actualizarAsteroides();
            verificarColisionesNaveAsteroides();
        }

        if (nave.estaDestruido()) {
            manejarGameOver();
        }

        if (balls.isEmpty()) {
            iniciarSiguienteRonda();
        }
    }

    @Override
    protected void dibujar() {
        SpriteBatch batch = game.getBatch();
        batch.begin();

        dibujaEncabezado();

        for (Bullet b : balas) {
            b.draw(batch);
        }

        nave.draw(batch, this);

        for (Ball2 b : balls) {
            b.draw(batch);
        }

        batch.end();
    }
    
    private void dibujaEncabezado() {
        GameManager gm = GameManager.getInstance();
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + gm.getRound();
        game.getFont().getData().setScale(2f);
        game.getFont().draw(game.getBatch(), str, 10, 30);
        game.getFont().draw(game.getBatch(), "Score: " + gm.getScore(), Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(game.getBatch(), "HighScore: " + gm.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    private void inicializarAsteroides() {
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            balls.add(crearAsteroide(r, "aGreyMedium4.png"));
        }
        for (int i = 0; i < cantAsteroidesGrandes; i++) {
            balls.add(crearAsteroide(r, "aGreyLarge.png"));
        }
    }

    private Ball2 crearAsteroide(Random r, String textura) {
        return new Ball2(
            r.nextInt(Gdx.graphics.getWidth()),
            50 + r.nextInt(Gdx.graphics.getHeight() - 50),
            20 + r.nextInt(10), velXAsteroides + r.nextInt(4),
            velYAsteroides + r.nextInt(4),
            new Texture(Gdx.files.internal(textura))
        );
    }

    private void inicializarSonidos() {
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();
    }

    private void actualizarBalas() {
        for (int i = 0; i < balas.size(); i++) {
            Bullet b = balas.get(i);
            b.update();

            for (int j = 0; j < balls.size(); j++) {
                Ball2 asteroide = balls.get(j);
                if (b.detectarColision(asteroide)) {
                    explosionSound.play();
                    asteroide.alColisionar(b);
                    balls.remove(j);
                    GameManager.getInstance().incrementScore(10);
                    b.alColisionar(asteroide);
                }
            }

            if (b.isDestroyed()) {
                balas.remove(i);
                i--;
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
                nave.alColisionar(asteroide);
                balls.remove(i);
                i--;
            }
        }
    }
    

    private void manejarGameOver() {
        GameManager gm = GameManager.getInstance();
        if (gm.getScore() > gm.getHighScore()) {
            gm.setHighScore(gm.getScore());
        }
        gm.setScore(0);
        gm.setRound(0);
        nave.setVidas(3);
        game.setScreen(new PantallaGameOver(game, dificultad));
        dispose();
    }

    private void iniciarSiguienteRonda() {
        GameManager gm = GameManager.getInstance();
        if (gm.getScore() > gm.getHighScore()) {
            gm.setHighScore(gm.getScore());
        }
        gm.setRound(gm.getRound() + 1);
        game.setScreen(new PantallaJuego(game, gm.getRound(), nave.getVidas(), gm.getScore(), velXAsteroides + 1, velYAsteroides + 1, dificultad));
        dispose();
    }

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    public void dispose() {
        explosionSound.dispose();
        gameMusic.dispose();
    }
}