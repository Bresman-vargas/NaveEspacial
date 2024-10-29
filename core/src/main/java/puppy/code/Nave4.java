package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 extends ObjetoEspacial implements Colisionable {
    
    private boolean destruida = false;
    private int vidas = 3;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(tx, x, y);
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
    }

    @Override
    public void mover() {
        // Movimiento de la nave
        if (!herido) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) xVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) xVel++;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) yVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) yVel++;

            float x = spr.getX() + xVel;
            float y = spr.getY() + yVel;
            // Control de límites de la nave
            if (x < 0 || x + spr.getWidth() > Gdx.graphics.getWidth()) {
                xVel *= -1; // Rebote horizontal
            }
            if (y < 0 || y + spr.getHeight() > Gdx.graphics.getHeight()) {
                yVel *= -1; // Rebote vertical
            }
            spr.setPosition(x, y);

            // Rotación de la nave hacia el cursor
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            float deltaX = mouseX - (spr.getX() + spr.getWidth() / 2);
            float deltaY = mouseY - (spr.getY() + spr.getHeight() / 2);
            float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees - 90;
            spr.setRotation(angle);
        } else {
            // Comportamiento cuando la nave está herida
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false; // Termina el estado herido
        }
    }

    public void draw(SpriteBatch batch, PantallaJuego juego) {
        mover();
        spr.draw(batch);
        
        // Disparo de balas
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            float angle = spr.getRotation();
            Bullet bala = new Bullet(
                spr.getX() + spr.getWidth() / 2 - 5,
                spr.getY() + spr.getHeight() - 5,
                angle,
                txBala
            );
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    // Implementación de la interfaz Colisionable
    @Override
    public boolean detectarColision(ObjetoEspacial otro) {
        if (otro instanceof Ball2) {
            Ball2 ball = (Ball2) otro;
            return ball.getArea().overlaps(spr.getBoundingRectangle());
        }
        return false;
    }

    @Override
    public void alColisionar(ObjetoEspacial otro) {
        if (otro instanceof Ball2) {
            // Solo se daña a la nave al colisionar
            vidas--;
            herido = true; // La nave se marca como herida
            tiempoHerido = tiempoHeridoMax; // Reinicia el temporizador para el estado herido
            sonidoHerido.play(); // Reproduce sonido de daño
            if (vidas <= 0) {
                destruida = true; // Si no hay vidas, la nave se destruye
            }
        }
    }

    public boolean estaDestruido() {
        return !herido && destruida; // Devuelve verdadero si la nave ha sido destruida
    }

    public boolean estaHerido() {
        return herido; // Devuelve verdadero si la nave está herida
    }

    public int getVidas() {
        return vidas; // Devuelve el número de vidas restantes
    }

    public void setVidas(int vidas2) {
        vidas = vidas2; // Establece el número de vidas (puedes usar esto para restaurar vidas)
    }
}
