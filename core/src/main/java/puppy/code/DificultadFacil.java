package puppy.code;
public class DificultadFacil implements DificultadStrategy {
    @Override
    public int calcularCantidadAsteroides(int ronda) {
        return ronda;
    }

    @Override
    public int calcularCantidadAsteroidesGrande(int ronda) {
        return Math.min(ronda, 5); // Solo hasta 5 asteroides grandes
    }

    @Override
    public int calcularVelocidadAsteroides(int ronda) {
        return 2; // Velocidad baja en dificultad fácil
    }
}