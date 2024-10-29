package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Bullet extends ObjetoEspacial implements Colisionable {
    private float xSpeed;
    private float ySpeed;
    private boolean destroyed = false;

    public Bullet(float x, float y, float angle, Texture tx) {
        super(tx, (int)x, (int)y); // Conversión de float a int si es necesario
        spr.setRotation(angle);

        float speed = 5f; // Ajusta esta velocidad según lo que necesites
        xSpeed = speed * MathUtils.cosDeg(angle + 90); // +90 para ajustar la dirección
        ySpeed = speed * MathUtils.sinDeg(angle + 90);
    }

    @Override
    public void mover() {
        // Implementación del movimiento para la bala
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);

        // Verifica si la bala salió de la pantalla
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth() ||
            spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
            destroyed = true; // Marca como destruida si sale de la pantalla
        }
    }

    // Método que debe ser llamado en actualizarBalas()
    public void update() {
        mover(); // Llama al método mover() para actualizar la posición
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    // Implementación de la interfaz Colisionable
    @Override
    public boolean detectarColision(ObjetoEspacial otro) {
        if (otro instanceof Ball2) {
            Ball2 asteroide = (Ball2) otro;
            return spr.getBoundingRectangle().overlaps(asteroide.getArea());
        }
        return false;
    }

    @Override
    public void alColisionar(ObjetoEspacial otro) {
        if (otro instanceof Ball2) {
            destroyed = true; // La bala se destruye al colisionar con Ball2
        }
    }

    public boolean isDestroyed() {
        return destroyed; // Devuelve verdadero si la bala ha sido destruida
    }
}
