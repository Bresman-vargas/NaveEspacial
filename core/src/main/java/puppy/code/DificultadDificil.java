package puppy.code;
public class DificultadDificil implements DificultadStrategy {
    @Override
    public int calcularCantidadAsteroides(int ronda) {
        return 1 + (ronda - 1) * 2;
    }

    @Override
    public int calcularCantidadAsteroidesGrande(int ronda) {
        return Math.min(ronda, 5); // Solo hasta 5 asteroides grandes
    }

    @Override
    public int calcularVelocidadAsteroides(int ronda) {
        return 20; // Velocidad baja en dificultad fácil
    }
}