package src.model;

import src.model.piattaforma.Sprite.Player;

/**
 * Contiene informazioni riguardanti i giochi e i player associati ad esso
 */
public class Gioco extends Model {

    private int framerate;
    private Player player;

    /**
     * Costruttore di base della classe
     *
     * @param nome      nome del gioco
     * @param framerate framerate del gioco
     * @param player    sprite del player associata al gioco
     */
    public Gioco(String nome, int framerate, Player player) {
        super();

        this.nome = nome;
        this.framerate = framerate;
        this.player = player;
    }

    public int getFramerate() {
        return framerate;
    }

    public void setFramerate(int framerate) {
        this.framerate = framerate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
