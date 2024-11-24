package puppy.code;
public interface Colisionable {
    boolean detectarColision(ObjetoEspacial otro);
    void alColisionar(ObjetoEspacial otro);
}