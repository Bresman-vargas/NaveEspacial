package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Bullet {

    private float xSpeed;
    private float ySpeed;
    private boolean destroyed = false;
    private Sprite spr;

    // Constructor modificado para aceptar el ángulo
    public Bullet(float x, float y, float angle, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setRotation(angle); // Aplicamos la rotación del ángulo al sprite

        // Velocidad de la bala y dirección calculadas usando el ángulo
        float speed = 5f; // Ajusta esta velocidad según lo que necesites
        xSpeed = speed * MathUtils.cosDeg(angle + 90); // +90 para ajustar la dirección
        ySpeed = speed * MathUtils.sinDeg(angle + 90);
    }

    public void update() {
        // Actualiza la posición de la bala en la dirección especificada
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);

        // Si la bala sale de los límites de la pantalla, la marcamos como destruida
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) {
            destroyed = true;
        }
        if (spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
            destroyed = true;
        }
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public boolean checkCollision(Ball2 b2) {
        if (spr.getBoundingRectangle().overlaps(b2.getArea())) {
            // Se destruye la bala en caso de colisión
            destroyed = true;
            return true;
        }
        return false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
