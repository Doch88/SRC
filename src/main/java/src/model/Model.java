package src.model;

/**
 * Classe astratta contenente le informazioni di base di un generico model
 */
public abstract class Model {

    protected String nome;

    /**
     * @return il nome dell'oggetto specifico
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome il nome dell'oggetto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

}
