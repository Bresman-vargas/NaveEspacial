package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class MeteoritoFactory {

    // Método estático para crear meteoritos según la dificultad
    public static Ball2 crearMeteorito(String tipo, int velX, int velY) {
        Texture texture;

        // Determinar las características del meteorito basándonos en el tipo
        switch (tipo.toLowerCase()) {
            case "pequeño":
                texture = new Texture(Gdx.files.internal("aGreyMedium4.png"));
                break;
            case "grande":
                texture = new Texture(Gdx.files.internal("aGreyLarge.png"));
                break;
            default:
                throw new IllegalArgumentException("Tipo de meteorito desconocido: " + tipo);
        }

        // Generar una posición aleatoria en la pantalla
        int x = (int)(Math.random() * Gdx.graphics.getWidth());
        int y = (int)(Math.random() * Gdx.graphics.getHeight());

        // Crear y retornar el meteorito con las características correspondientes
        return new Ball2(x, y, velX, velY, texture);
    }
}
