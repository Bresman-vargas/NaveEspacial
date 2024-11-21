package puppy.code;
public class GameManager {
    // Instancia Singleton
    private static GameManager instance;

    // Variables globales
    private int score;
    private int highScore;
    private int round;
    private int vidas;

    // Constructor privado para evitar instanciación externa
    private GameManager() {
        this.score = 0;
        this.highScore = 0;
        this.round = 1;
        this.vidas = 4;
    }

    // Obtener la instancia del Singleton
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // Getters y setters para las variables
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    // Método para incrementar el puntaje (útil para actualizar el puntaje durante el juego)
    public void incrementScore(int increment) {
        this.score += increment;
    }

    // Método para reiniciar el juego (se puede usar para reseteo de puntaje y vidas)
    public void resetGame() {
        this.score = 0;
        this.round = 1;
        this.vidas = 4;
    }
}
