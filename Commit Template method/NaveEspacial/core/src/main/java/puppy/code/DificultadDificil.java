package puppy.code;
public class DificultadDificil implements DificultadStrategy {

    @Override
    public int calcularCantidadAsteroides(int ronda) {
        // Aumenta rápidamente la cantidad de asteroides
        return 5 + (ronda - 1) * 3; // Comienza con 5 y añade 3 por ronda
    }

    @Override
    public int calcularCantidadAsteroidesGrande(int ronda) {
        // Escala agresivamente el número de asteroides grandes
        return Math.min(ronda, 10); // Hasta un máximo de 10 asteroides grandes
    }

    @Override
    public int calcularVelocidadAsteroides(int ronda) {
        // Alta velocidad desde el inicio, aumentando ligeramente
        return 6 + (ronda - 1) / 2; // Velocidad inicial alta que escala rápidamente
    }
}