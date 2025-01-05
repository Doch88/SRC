package src.model.piattaforma.Sprite;

import src.model.piattaforma.Collisione;
import src.utils.Settings;
import src.utils.Utils;

import java.awt.*;

/**
 * Uno sprite sparato da altri sprite.
 * E' caratterizzato da un proprietario, che quindi decide anche qual è l'avversario.
 */
public class Proiettile extends Sprite {

    protected Sprite proprietario;
    protected Direction direzione;
    protected int attacco;

    public void setAttacco(int attacco) {
        this.attacco = attacco;
    }

    public int getVelocita() {
        return velocita;
    }

    public void setVelocita(int velocita) {
        this.velocita = velocita;
    }

    protected int velocita;

    private final int xMax;

    public Proiettile(String nome, Image immagine, Sprite proprietario, int velocita, int attacco, int xMax) {
        super(nome, immagine);
        this.proprietario = proprietario;
        this.velocita = velocita;
        this.attacco = attacco;
        this.xMax = xMax;
        priorita = Settings.PRIORITA_PROIETTILE;
    }

    public Sprite getProprietario() {
        return proprietario;
    }

    /**
     * Inizia il movimento del proiettile nella direzione del suo proprietario, ovvero di chi l'ha lanciato
     */
    public void iniziaMovimento() {
        if (proprietario instanceof Player)
            direzione = ((Player) proprietario).getDirezione();
        else
            direzione = ((NPC) proprietario).getDirezione();
        this.setVelocita(direzione, velocita, -velocita, velocita);
    }

    @Override
    public Image getImmagine() {
        Image immagine = this.immagine;
        Direction directionOpposta = direzione == Direction.LEFT ? Direction.RIGHT : Direction.LEFT;
        if (!this.animazioneMovimento.isEmpty()) {
            immagine = this.animazioneMovimento.getImage(directionOpposta);
            animazioneStatica.stopAnimazione();
        } else if (direzione == Direction.RIGHT) {
            immagine = Utils.mirrorImage(immagine);
        }
        return immagine;
    }

    @Override
    public void update() {
        if (getX() > xMax || getX() < 0)
            this.setDead();
    }

    @Override
    public void muovi() {
        if (Collisione.rilevaCollisioni(this, horizontalVelocity, 0).toIgnore())
            this.xPosition += horizontalVelocity;
        else
            this.setDead();
    }

    @Override
    public void onCollision(Collisione collisione) {
    }

    public int getAttacco() {
        return attacco;
    }

}
