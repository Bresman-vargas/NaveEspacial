package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class MeteoritoFactory {

    // Método estático para crear meteoritos según la dificultad
    public static Ball2 crearMeteorito(String tipo, int xSpeed, int ySpeed) {
    Texture tx;

    // Determinar las características del meteorito basándonos en el tipo
    switch (tipo.toLowerCase()) {
        case "pequeño":
            tx = new Texture(Gdx.files.internal("aGreyMedium4.png"));
            break;
        case "grande":
            tx = new Texture(Gdx.files.internal("aGreyLarge.png"));
            break;
        default:
            throw new IllegalArgumentException("Tipo de meteorito desconocido: " + tipo);
    }
    

    // Generar una posición aleatoria en la pantalla
    int x = (int)(Math.random() * Gdx.graphics.getWidth());
    int y = (int)(Math.random() * Gdx.graphics.getHeight());
    int size = tipo.equalsIgnoreCase("pequeño") ? 20 : 50; // Tamaño según el tipo

    // Crear y retornar el meteorito con las características correspondientes
    return new Ball2(x, y, size, xSpeed, ySpeed, tx);
}

}