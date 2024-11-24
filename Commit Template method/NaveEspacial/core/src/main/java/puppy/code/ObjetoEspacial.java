package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

// Clase abstracta que representa un objeto espacial
public abstract class ObjetoEspacial {
    protected Sprite spr;
    protected float xVel; 
    protected float yVel; 

    // Constructor que permite inicializar la textura y la posición del objeto
    public ObjetoEspacial(Texture texture, int x, int y) {
        this.spr = new Sprite(texture);
        this.spr.setPosition(x, y);
        // Opcional: se puede ajustar el tamaño según sea necesario
        this.spr.setBounds(x, y, texture.getWidth(), texture.getHeight());
    }

    // Método abstracto que debe ser implementado para mover el objeto
    public abstract void mover();

    // Método para actualizar la posición del sprite
    public void actualizarPosicion() {
        spr.setX(spr.getX() + xVel);
        spr.setY(spr.getY() + yVel);
    }

    // Método para verificar si hay colisión con otro objeto
    public boolean verificarColision(ObjetoEspacial otro) {
        return this.getArea().overlaps(otro.getArea());
    }

    public Sprite getSprite() {
        return spr;
    }

    public float getX() {
        return spr.getX();
    }

    public float getY() {
        return spr.getY();
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void setXVel(float xVel) {
        this.xVel = xVel;
    }

    public void setYVel(float yVel) {
        this.yVel = yVel;
    }

    public float getXVel() {
        return xVel;
    }

    public float getYVel() {
        return yVel;
    }
}
