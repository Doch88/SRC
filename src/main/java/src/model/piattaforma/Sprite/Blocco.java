package src.model.piattaforma.Sprite;

import src.model.piattaforma.Collisione;
import src.model.piattaforma.ListaCollisioni;
import src.utils.Settings;

import java.awt.*;

/**
 * Un blocco è uno sprite che, appunto, blocca il movimento degli altri sprite.
 * Può essere movibile, dunque estende MovableSprite, e quindi può muoversi fra due waypoints scelti
 * Se in movimento un blocco trascina il player che passa sopra di esso
 */
public class Blocco extends MovableSprite {

    private final int attacco;
    private final int salute;
    private int saluteAttuale;
    private boolean attraversato = false;
    private boolean piattaforma = false;

    /**
     * Inizializza il blocco con i dati base che lo caratterizzano
     *
     * @param nome        il nome del blocco, cioè il nome con cui è chiamato nel database
     * @param immagine    l'immagine iniziale statica del blocco
     * @param piattaforma se il blocco deve ignorare le collisioni verso l'alto del player allora questo valore sarà true
     * @param velocita    la velocita con cui si muove il blocco
     * @param attacco     il danno in caso di collisioni con il player
     * @param salute      la salute del blocco se distruttibile
     * @param punteggio   il punteggio assegnato il caso il blocco viene distrutto
     */
    public Blocco(String nome, Image immagine, boolean piattaforma, int velocita, int attacco, int salute, int punteggio) {
        super(nome, immagine, velocita);
        this.salute = salute;
        this.attacco = attacco;
        this.velocita = velocita;
        this.punteggioFornito = punteggio;
        this.piattaforma = piattaforma;
        priorita = Settings.PRIORITA_BLOCCO;
        this.saluteAttuale = salute;
    }

    @Override
    public void onCollision(Collisione collisione) {
        Sprite other = collisione.getPrimoSprite();
        if (!attraversato && other instanceof Player && collisione.getDirection() != Direction.DOWN && piattaforma) {
            attraversato = true;
        } else if (!attraversato && piattaforma && collisione.getDirection() == Direction.DOWN && other.getVerticalVelocity() < 0) {
            attraversato = true;
        } else if (other instanceof Player) {//Se il blocco è 'pericoloso' allora ferisce il player
            ((Player) other).changeLife(-attacco);
        } else if (other instanceof NPC) {//Se l'NPC colpisce il blocco allora cambia direzione
            ((NPC) other).changeDirection();
        } else if (other instanceof Proiettile) {
            if (salute > 0) {
                saluteAttuale -= ((Proiettile) other).getAttacco();
                if (saluteAttuale <= 0)
                    this.setDead();
            }
            other.setDead();
        } else if (other instanceof Blocco && attraversato && piattaforma) {
            ((Blocco) other).setAttraversato(true);
        }
    }

    @Override
    public void update() {
        if (attraversato) {
            ListaCollisioni collisioni = Collisione.rilevaCollisioni(this, 0, 0);
            if (collisioni.isEmpty()) attraversato = false;
        }
    }

    @Override
    public void muovi() {
        super.muovi();
        if (!canMove()) return;
        ListaCollisioni collisioni = Collisione.rilevaCollisioni(this, 0, -10);
        if (collisioni.collideCon(Player.class, Direction.UP) != null) {
            Player player = (Player) collisioni.collideCon(Player.class, Direction.UP);
            player.addXPosition(this.horizontalVelocity);
            if (player.getDirezione() != direzione)
                player.addXPosition(player.getHorizontalVelocity());

        }
        collisioni = Collisione.rilevaCollisioni(this, horizontalVelocity, 0);
        if (collisioni.toIgnore()) {
            Sprite spr = collisioni.collideCon(Player.class, Direction.NO_DIRECTION);
            if (spr != null)
                ((Player) spr).addXPosition(horizontalVelocity);
        }
    }

    public void setAttraversato(boolean attraversato) {
        this.attraversato = attraversato;
    }

    public boolean isPiattaforma() {
        return piattaforma;
    }

    public void setPiattaforma(boolean piattaforma) {
        this.piattaforma = piattaforma;
    }

    @Override
    public boolean bloccaMovimento() {
        return !attraversato;
    }

}
