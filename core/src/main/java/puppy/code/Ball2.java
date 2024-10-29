package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Clase Ball2 que extiende ObjetoEspacial
public class Ball2 extends ObjetoEspacial {
    private int size;
    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        super(tx, x, y); // Llama al constructor de ObjetoEspacial
        this.size = size;
        this.xVel = xSpeed; // Asigna velocidad en X
        this.yVel = ySpeed; // Asigna velocidad en Y

        // Validar que el borde de la esfera no quede fuera
        if (x - size < 0) spr.setX(x + size);
        if (x + size > Gdx.graphics.getWidth()) spr.setX(x - size);
        if (y - size < 0) spr.setY(y + size);
        if (y + size > Gdx.graphics.getHeight()) spr.setY(y - size);
    }
    
    @Override
    public void mover() {
        update();
    }

    // Método para actualizar la posición y verificar colisiones
    public void update() {
        // Actualiza la posición
        float newX = spr.getX() + xVel;
        float newY = spr.getY() + yVel;

        // Verifica colisiones con los bordes de la ventana
        if (newX < 0 || newX + spr.getWidth() > Gdx.graphics.getWidth()) {
            xVel *= -1; // Rebota en el eje X
        }
        if (newY < 0 || newY + spr.getHeight() > Gdx.graphics.getHeight()) {
            yVel *= -1; // Rebota en el eje Y
        }

        // Actualiza la posición del sprite
        spr.setPosition(spr.getX() + xVel, spr.getY() + yVel);
    }

    // Método para dibujar el sprite
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void checkCollision(Ball2 other) {
        if (spr.getBoundingRectangle().overlaps(other.getArea())) {
            // Rebote en caso de colisión
            float tempXVel = xVel; // Guarda la velocidad en X temporalmente
            float tempYVel = yVel; // Guarda la velocidad en Y temporalmente

            xVel = -other.getXSpeed(); // Cambia la dirección de la bola actual
            other.setXSpeed(-tempXVel); // Cambia la dirección de la otra bola

            yVel = -other.getySpeed(); // Cambia la dirección de la bola actual
            other.setySpeed(-tempYVel); // Cambia la dirección de la otra bola
        }
    }

    public int getXSpeed() {
        return (int) xVel; // Convierte a int al retornar la velocidad
    }

    public int getySpeed() {
        return (int) yVel; // Convierte a int al retornar la velocidad
    }

    public void setXSpeed(float xSpeed) {
        this.xVel = xSpeed; // Asigna la velocidad en X
    }

    public void setySpeed(float ySpeed) {
        this.yVel = ySpeed; // Asigna la velocidad en Y
    }
}
