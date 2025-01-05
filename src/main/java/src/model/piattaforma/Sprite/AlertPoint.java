package src.model.piattaforma.Sprite;


import src.model.piattaforma.Collisione;
import src.utils.Settings;

import java.awt.*;

/**
 * Un alert point è un particolare sprite che ha come unica funzione quella di far visualizzare un messaggio
 * di testo a schermo al passaggio del player.
 */
public class AlertPoint extends Sprite {

    private final String text;
    private boolean textVisible = false;

    /**
     * Inizializza l'AlertPoint con le informazioni di base di uno sprite, più il testo da visualizzare alla collisione
     *
     * @param nome     il nome dell'AlertPoint
     * @param immagine l'immagine statica dell'AlertPoint
     * @param text     il testo da visualizzare
     */
    public AlertPoint(String nome, Image immagine, String text) {
        super(nome, immagine);
        this.text = text;
        priorita = Settings.PRIORITA_ALERTPOINT;
    }

    public void muovi() {
    }

    @Override
    public void onCollision(Collisione collisione) {
        if (collisione.getPrimoSprite() instanceof Player)
            this.setTextVisible(true);
    }

    public void update() {
        this.setTextVisible(false);
    }

    /**
     * Ritorna la variabile textVisible, se true allora viene stampato il testo sulla schermata
     */
    public boolean isTextVisible() {
        return textVisible;
    }

    /**
     * @param textVisible impostando questo parametro a true allora il testo relativo all'alert point verrà visualizzato sullo schermo
     */
    public void setTextVisible(boolean textVisible) {
        this.textVisible = textVisible;
    }

    public String getText() {
        return text;
    }
}
