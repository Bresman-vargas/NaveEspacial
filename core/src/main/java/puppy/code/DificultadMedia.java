package puppy.code;
public class DificultadMedia implements DificultadStrategy {

    @Override
    public int calcularCantidadAsteroides(int ronda) {
        // Aumenta la cantidad de asteroides de forma progresiva
        return 3 + (ronda - 1) * 2; // Siempre al menos 3 asteroides
    }

    @Override
    public int calcularCantidadAsteroidesGrande(int ronda) {
        // Escala gradualmente los asteroides grandes, con un límite de 7
        return Math.min((ronda + 1) / 2, 7);
    }

    @Override
    public int calcularVelocidadAsteroides(int ronda) {
        // Incrementa ligeramente la velocidad en cada ronda
        return 4 + (ronda - 1) / 3; // Velocidad moderada y progresiva
    }
}