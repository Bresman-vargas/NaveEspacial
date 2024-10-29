package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 extends ObjetoEspacial {
    
    private boolean destruida = false;
    private int vidas = 3;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(tx, x, y); // Llama al constructor de ObjetoEspacial
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
    }

    @Override
    public void mover() {
        // Movimiento controlado por teclado
        if (!herido) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) xVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) xVel++;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) yVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) yVel++;

            // Mantener dentro de los bordes de la ventana
            float x = spr.getX() + xVel;
            float y = spr.getY() + yVel;
            if (x < 0 || x + spr.getWidth() > Gdx.graphics.getWidth()) {
                xVel *= -1;
            }
            if (y < 0 || y + spr.getHeight() > Gdx.graphics.getHeight()) {
                yVel *= -1;
            }
            spr.setPosition(x, y);

            // Obtener la posición del mouse y calcular el ángulo de rotación hacia el mouse
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invertir la posición Y del mouse

            // Calcular el ángulo hacia el puntero del mouse
            float deltaX = mouseX - (spr.getX() + spr.getWidth() / 2);
            float deltaY = mouseY - (spr.getY() + spr.getHeight() / 2);
            float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees - 90;

            // Aplicar la rotación del ángulo al sprite
            spr.setRotation(angle);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
    }


    public void draw(SpriteBatch batch, PantallaJuego juego) {
        mover(); // Llama al método mover en cada dibujado
        spr.draw(batch);
        
        // Disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            float angle = spr.getRotation(); // Obtener el ángulo de rotación de la nave
            Bullet bala = new Bullet(
                spr.getX() + spr.getWidth() / 2 - 5, // Posición X de la bala
                spr.getY() + spr.getHeight() - 5,    // Posición Y de la bala
                angle,                               // Ángulo en el que se disparará la bala
                txBala                               // Textura de la bala
            );
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            // Rebote
            if (xVel == 0) xVel += b.getXSpeed() / 2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) xVel / 2);
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getySpeed() / 2;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int) yVel / 2);
            yVel = -yVel;
            b.setySpeed(-b.getySpeed());

            // Actualizar vidas y herir
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0) destruida = true;
            return true;
        }
        return false;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public boolean estaHerido() {
        return herido;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas2) {
        vidas = vidas2;
    }
}