package src.model.piattaforma.Sprite;

import src.model.piattaforma.Collisione;
import src.utils.Settings;
import src.utils.Utils;

import java.awt.*;

/**
 * Un portale è un AlertPoint che ha come funzione aggiuntiva quella di trasportare altri sprite.
 * Essendo un AlertPoint avrà un messaggio che sarà visualizzato alla collisione con il player.
 * Tale messaggio è sempre uguale ed è quello che indica il tasto che deve premere il player per teletrasportarsi
 */
public class Portale extends AlertPoint {

    protected Portale associato;

    protected int IDPortale;

    /**
     * Inizializza un portale e gli assegna un ID, utilizzato poi per l'associazione.
     *
     * @param nome      il nome del portale
     * @param IDPortale l'ID del portale, per associarlo con un altro
     * @param immagine  immagine statica del portale
     */
    public Portale(String nome, int IDPortale, Image immagine) {
        super(nome, immagine, Utils.getText("portal_text"));
        this.IDPortale = IDPortale;
        this.onGround = true;
        priorita = Settings.PRIORITA_PORTALE;
    }

    @Override
    public void onCollision(Collisione collisione) {
        if (collisione.getPrimoSprite() instanceof Player && associato != null)
            this.setTextVisible(true);
    }

    /**
     * Trasporta lo sprite nella posizione del portale associato
     *
     * @param spr sprite da trasportare
     */
    public void teletrasporta(Sprite spr) {
        if (associato == null) return;
        if (spr instanceof Player)
            spr.setPosition(associato.getX() - spr.getWidth() / 2 + associato.getWidth() / 2, associato.getY() - spr.getHeight() / 2 + associato.getHeight() / 2);
        else
            spr.setPosition(associato.getX(), associato.getY());
    }

    public Portale getAssociato() {
        return associato;
    }

    public void setAssociato(Portale associato) {
        this.associato = associato;
        if (associato.getAssociato() != this)
            associato.setAssociato(this);
    }

    public int getIDPortale() {
        return IDPortale;
    }
}
