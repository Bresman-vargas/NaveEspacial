package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Clase Ball2 que extiende ObjetoEspacial e implementa Colisionable
public class Ball2 extends ObjetoEspacial implements Colisionable {
    private int size;
    private boolean isDestroyed; // Estado que indica si el meteorito ha sido destruido

    public Ball2(int x, int y, int xSpeed, int ySpeed, Texture tx) {
        super(tx, x, y);
        this.xVel = xSpeed;
        this.yVel = ySpeed;
        this.isDestroyed = false; // Inicialmente, el meteorito no está destruido

        // Control de límites iniciales
        if (x - size < 0) spr.setX(x + size);
        if (x + size > Gdx.graphics.getWidth()) spr.setX(x - size);
        if (y - size < 0) spr.setY(y + size);
        if (y + size > Gdx.graphics.getHeight()) spr.setY(y - size);
    }

    @Override
    public void mover() {
        if (!isDestroyed) {
            update();
        }
    }

    public void update() {
        float velocidadReducida = 0.5f;
        float newX = spr.getX() + xVel * velocidadReducida;
        float newY = spr.getY() + yVel * velocidadReducida;

        // Rebote en los bordes de la pantalla
        if (newX < 0 || newX + spr.getWidth() > Gdx.graphics.getWidth()) {
            xVel *= -1;
        }
        if (newY < 0 || newY + spr.getHeight() > Gdx.graphics.getHeight()) {
            yVel *= -1;
        }

        spr.setPosition(spr.getX() + xVel * velocidadReducida, spr.getY() + yVel * velocidadReducida);
    }

    public void draw(SpriteBatch batch) {
        if (!isDestroyed) {
            spr.draw(batch);
        }
    }

    // Implementación de la interfaz Colisionable
    @Override
    public boolean detectarColision(ObjetoEspacial otro) {
        if (otro instanceof Nave4) { // Detectar colisión con la nave
            return spr.getBoundingRectangle().overlaps(otro.getArea());
        }
        return false;
    }

    @Override
    public void alColisionar(ObjetoEspacial otro) {
        if (otro instanceof Nave4) {
            // Aquí puedes manejar la colisión con la nave
            ((Nave4) otro).alColisionar(this); // Llama al método de colisión de la nave

            // Marca el meteorito como destruido
            destruir();
        }
    }

    public void destruir() {
        isDestroyed = true; // Marca el meteorito como destruido
        // Aquí puedes agregar lógica para efectos de sonido o visuales
        System.out.println("Meteorito destruido!");
    }

    public boolean isDestroyed() {
        return isDestroyed; // Devuelve el estado de destrucción del meteorito
    }

    public int getSize() {
        return size; // Devuelve el tamaño del meteorito
    }

    public float getXSpeed() {
        return xVel; // Devuelve la velocidad en X
    }

    public float getYSpeed() {
        return yVel; // Devuelve la velocidad en Y
    }

    public void setXSpeed(float xSpeed) {
        this.xVel = xSpeed; // Establece la velocidad en X
    }

    public void setYSpeed(float ySpeed) {
        this.yVel = ySpeed; // Establece la velocidad en Y
    }
}
