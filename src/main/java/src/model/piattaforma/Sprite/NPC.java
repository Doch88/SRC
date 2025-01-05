package src.model.piattaforma.Sprite;


import src.model.DAO.PiattaformaDAO;
import src.model.piattaforma.Animazione;
import src.model.piattaforma.Collisione;
import src.model.piattaforma.Piattaforma;
import src.utils.Settings;

import java.awt.*;
import java.nio.file.Paths;

/**
 * Classe che rappresenta gli NPC ostili al player con attacco, velocit&agrave; e salute propria.
 * Gli NPC saranno solitamente movibili, cioè si muoveranno fra due waypoint prefissati, e potranno sparare
 * proiettili ad una certa frequenza prefissata anch'essa.
 * <p>
 * Normalmente avrà una animazione basiliare sempre attiva, quella del movimento, e in caso essa non sia presente avremo
 * una sola immagine attiva.
 */
public class NPC extends MovableSprite {

    private int attacco;
    private final int saluteMassima;
    private int saluteAttuale;

    private int riposoAttualeSparo;
    private final int periodoDiRiposoSparo;

    private final Animazione animazioneDannoSubito;

    private String nomeProiettile = "proiettile";
    private Proiettile proiettileSprite = null;
    private final String pathHitImage;

    public NPC(String nome, Image immagine, String proiettile, int attacco, int velocita, int salute, int riposoSparo, int punteggio) {
        super(nome, immagine, velocita);
        this.saluteAttuale = this.saluteMassima = salute;
        this.attacco = attacco;
        pathHitImage = Paths.get(percorso, "colpito").toString();
        animazioneMovimento = new Animazione(nome, percorso, immagine);
        animazioneDannoSubito = new Animazione(nome + "_colpito", pathHitImage);
        animazioneDannoSubito.setContinuous(false);
        this.punteggioFornito = punteggio;
        this.riposoAttualeSparo = 0;
        this.periodoDiRiposoSparo = riposoSparo;
        this.nomeProiettile = proiettile;

        priorita = Settings.PRIORITA_NPC;
    }

    @Override
    public void onCollision(Collisione collisione) {
        Sprite other = collisione.getPrimoSprite();
        if (other instanceof Player && collisione.getDirection() != Direction.DOWN) {//Quando collide con il player non in alto allora lo ferisce
            ((Player) other).changeLife(-attacco);
        } else if (other instanceof Player && !((Player) other).inRiposo()) {//Quando collide con il player in alto allora viene ferito
            this.changeLife(-((Player) other).getAttacco());
            ((Player) (other)).salta();
        } else if (other instanceof Proiettile && ((Proiettile) other).getProprietario() instanceof Player) {
            this.changeLife(-((Proiettile) other).getAttacco());
            other.setDead();
        }
    }

    @Override
    public void update() {
        //spara proiettile
        if (proiettileSprite == null && nomeProiettile != null && !nomeProiettile.equals("null"))
            proiettileSprite = PiattaformaDAO.getInstance().getProiettile(nomeProiettile, this);
        if (nomeProiettile != null && direzione == Direction.LEFT) {
            if (periodoDiRiposoSparo > -1 && periodoDiRiposoSparo <= riposoAttualeSparo) {
                Proiettile sparato = (Proiettile) proiettileSprite.clone();
                boolean dir = direzione == Direction.RIGHT;
                sparato.setPosition(this.getX() + (dir ? this.getWidth() : 0), this.getY() + this.getHeight() / 2);
                Piattaforma.getInstance().aggiungiSprite(sparato);
                sparato.iniziaMovimento();
                this.riposoAttualeSparo = 0;
            } else
                this.riposoAttualeSparo += 1;
        }
    }

    /**
     * L'animazione di un NPC sarà sempre quella di movimento, poiché si suppone che un NPC sia mobile.
     * Verrà quindi sempre restituito un elemento dell'ArrayList di animazioneMovimento.
     */
    @Override
    public Image getImmagine() {
        Image immagine = this.immagine;
        if ((this.animazioneDannoSubito == null || !this.animazioneDannoSubito.isRunning()) && !this.animazioneMovimento.isEmpty()) {
            immagine = this.animazioneMovimento.getImage(direzione);
            animazioneStatica.stopAnimazione();
        } else if (this.animazioneDannoSubito != null && this.animazioneDannoSubito.isRunning()) {
            immagine = this.animazioneDannoSubito.getImage(direzione);
            animazioneMovimento.stopAnimazione();
            animazioneStatica.stopAnimazione();
        }
        return immagine;
    }

    public void changeLife(int value) {
        if (value < 0) animazioneDannoSubito.avviaAnimazione();
        if (this.saluteAttuale + value <= this.saluteMassima)
            this.saluteAttuale += value;
        else if (this.saluteAttuale + value > this.saluteMassima)
            this.saluteAttuale = saluteMassima;
        if (saluteAttuale < 0) {
            this.setDead();
        }
    }

    @Override
    public void onDeath() {
        super.onDeath();
        animazioneDannoSubito.stopAnimazione();
    }

    public int getAttacco() {
        return attacco;
    }

    public void setAttacco(int attacco) {
        this.attacco = attacco;
    }

    public Direction getDirezione() {
        return direzione;
    }

    public void setDirezione(Direction direzione) {
        this.direzione = direzione;
    }


}
