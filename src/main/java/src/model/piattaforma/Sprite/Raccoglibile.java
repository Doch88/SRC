package src.model.piattaforma.Sprite;


import src.model.piattaforma.Collisione;
import src.model.piattaforma.Piattaforma;
import src.utils.Settings;

import java.awt.*;

/**
 * Indica un oggetto che può essere raccolto, può essere un collezionabile, della salute o proiettili
 */
public class Raccoglibile extends Sprite {

    /**
     * Valore che fornirà alla raccolta dell'oggetto da parte del player
     */
    private int valore;

    /**
     * Tipologia di raccoglibile, che può essere salute, proiettili o collezionabile
     */
    private String tipo;

    private int spawningTime = 0;

    private final Piattaforma piattaforma = Piattaforma.getInstance();

    public Raccoglibile(String nome, Image immagine, String tipo, int valore, int punteggio) {
        super(nome, immagine);
        this.tipo = tipo;
        this.valore = valore;
        this.onGround = true;
        this.punteggioFornito = punteggio;
        spawningTime = 0;
        priorita = Settings.PRIORITA_RACCOGLIBILE;
    }

    @Override
    public void onCollision(Collisione collisione) {
        if (spawningTime >= 5 && collisione.getPrimoSprite() instanceof Player)
            this.setDead();
    }

    /**
     * Il raccoglibile non ha movimenti da gestire
     */
    @Override
    public void muovi() {
    }

    @Override
    public void update() {
        if (spawningTime < 5)
            spawningTime++;
    }

    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public void onDeath() {
        switch (tipo) {
            case "health":       //Se è un oggetto che ripristina salute allora cambia la vita del player
                piattaforma.getPlayer().changeLife(valore);
                break;
            case "collectable":  //Se è un collezionabile allora aggiunge il valore alla chiave corrispondente per il player
                piattaforma.addCollezionabile(this.getNome(), this.getValore());
                break;
            case "bullets":      //Se è un mucchio di proiettili allora aggiunge dei proiettili alla quantità disponibile del giocatore
                piattaforma.getPlayer().addProiettili(this.getValore());
                break;
        }
    }
}
