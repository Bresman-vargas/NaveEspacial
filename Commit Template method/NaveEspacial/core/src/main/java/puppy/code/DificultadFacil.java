package puppy.code;
public class DificultadFacil implements DificultadStrategy {

    @Override
    public int calcularCantidadAsteroides(int ronda) {
        // Inicia con pocos asteroides y aumenta lentamente
        return Math.max(1, ronda); // Siempre al menos un asteroide
    }

    @Override
    public int calcularCantidadAsteroidesGrande(int ronda) {
        // Asteroides grandes se limitan a un máximo de 3 en las primeras rondas
        return Math.min((ronda + 1) / 2, 3);
    }

    @Override
    public int calcularVelocidadAsteroides(int ronda) {
        // Velocidad constante y baja para hacerlo más sencillo
        return 2;
    }
}