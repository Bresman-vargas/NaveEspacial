package puppy.code;
public class DificultadMedia implements DificultadStrategy {
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
        return 15; // Velocidad baja en dificultad fácil
    }
}