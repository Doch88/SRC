package src.model.piattaforma.Sprite;

import src.MainController;
import src.model.Model;
import src.model.piattaforma.Animazione;
import src.model.piattaforma.Collisione;
import src.utils.Settings;

import java.awt.*;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Classe astratta che verrà estesa dai vari tipi di sprites.
 * Contiene le informazioni generali dello sprite e implementa le interfacce Comparable e Cloneable.
 * <p>
 * La prima delle due interfacce servirà alla view per mostrare nell'ordine corretto, a seconda delle priorità, le immagini.
 * La seconda servirà sopratutto nel caso dei proiettili per ridurre le query al database e creare oggetti essenzialmente identici.
 */

public abstract class Sprite extends Model implements Comparable<Sprite>, Cloneable {

    /**
     * Rappresenta la direzione (destra, sinistra, sopra o sotto) rispetto ad uno sprite
     * e verrà usata principalmente nelle collisioni.
     */
    public enum Direction {
        /**
         * Indica il valore nullo.
         */
        NO_DIRECTION,
        /**
         * Equivalente della direzione "Destra"
         */
        RIGHT,
        /**
         * Equivalente della direzione "Sinistra"
         */
        LEFT,
        /**
         * Equivalente della direzione "Sopra"
         */
        UP,
        /**
         * Equivalente della direzione "Sotto"
         */
        DOWN
    }

    protected Image immagine;
    protected ArrayList<Sprite> oggettiDaDroppare;
    /**
     * Velocità verticale di movimento, poi applicata tramite muovi()
     */
    protected int verticalVelocity;
    /**
     * Velocità orizzontale di movimento, poi applicata tramite muovi()
     */
    protected int horizontalVelocity;
    protected int priorita;
    /**
     * Animazioni statica quando lo sprite è fermo
     */
    protected Animazione animazioneStatica;
    protected int punteggioFornito = 0;
    /**
     * Animazione di movimento, quando lo sprite si muove
     */
    protected Animazione animazioneMovimento;
    /**
     * Se lo sprite è a terra, quindi se collide con un blocco in basso, allora è true
     */
    protected boolean onGround = false;
    /**
     * Posizione x assoluta dello sprite, non relativa al player
     */
    protected int xPosition;
    /**
     * Posizione y assoluta dello sprite, non relativa al player
     */
    protected int yPosition;
    /**
     * Larghezza dello sprite, che sarà relativa all'immagine e all'observer scelto
     */
    protected int width;
    /**
     * Altezza dello sprite, che sarà relativa all'immagine e all'observer scelto
     */
    protected int height;
    protected boolean isDead = false;
    protected String percorso;
    protected String staticPath;

    protected Sprite() {
    }

    public Sprite(String nome, Image immagine) {
        this.init(nome, immagine);
    }

    protected void init(String nome, Image immagine) {
        this.nome = nome;
        this.immagine = immagine;
        this.horizontalVelocity = 0;
        this.verticalVelocity = 0;
        if (immagine != null) {
            this.width = immagine.getWidth(MainController.getInstance().getFrame());
            this.height = immagine.getHeight(MainController.getInstance().getFrame());
            percorso = Paths.get(Settings.CARTELLA_SPRITES, nome).toString();
            staticPath = Paths.get(percorso, "static").toString();
            animazioneStatica = new Animazione(nome + "_static", staticPath, immagine);
            animazioneMovimento = new Animazione(nome, percorso);

        }
        oggettiDaDroppare = new ArrayList<>();
    }

    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    /**
     * Gestisce il movimento dello sprite nel tempo e permette quindi di fare in modo che gli sprite si muovano
     * nel modo che indica la loro tipologia.
     */
    public abstract void muovi();

    /**
     * Quando uno sprite collide con un secondo sprite, il primo sprite richiama la funzione onCollision() del secondo
     *
     * @param collisione dati della collisione avvenuta
     */
    public abstract void onCollision(Collisione collisione);

    /**
     * Metodo richiamato ad ogni ciclo di update.
     * Definisce le istruzioni necessarie da eseguire ad ogni ciclo.
     * Ogni tipologia di sprite ha il suo specifico metodo di update, poiché ogni sprite fa cose diverse.
     */
    public abstract void update();

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    /**
     * Ottiene l'immagine attuale, dipendente dall'animazione che deve essere visualizzata.
     *
     * @return l'immagine corrispondente all'animazione attuale
     */
    public Image getImmagine() {
        Image immagine = this.immagine;
        if (animazioneStatica == null) return null;
        if (!this.animazioneStatica.isEmpty() && this.animazioneStatica.size() != 1 && this.onGround) {
            immagine = animazioneStatica.getImage(Direction.NO_DIRECTION);
            animazioneMovimento.stopAnimazione();
        }
        return immagine;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVerticalVelocity() {
        return verticalVelocity;
    }

    public int getHorizontalVelocity() {
        return horizontalVelocity;
    }

    /**
     * Cambia la velocità in relazione alla direzione
     *
     * @param direzione
     * @param value
     * @param min
     * @param max
     * @param progressive
     */
    public void setVelocita(Direction direzione, int value, int min, int max, boolean progressive) {
        if (direzione == Direction.UP || direzione == Direction.DOWN) {
            this.verticalVelocity += (direzione == Direction.DOWN ? value : -value);
            if (this.verticalVelocity < min)
                this.verticalVelocity = min;
            if (this.verticalVelocity > max)
                this.verticalVelocity = max;
        } else {
            if (direzione == Direction.RIGHT && this.horizontalVelocity < 0 && !progressive)
                this.horizontalVelocity = value;
            else if (direzione == Direction.LEFT && this.horizontalVelocity > 0 && !progressive)
                this.horizontalVelocity = -value;
            else
                this.horizontalVelocity += (direzione == Direction.RIGHT ? value : -value);
            if (this.horizontalVelocity < min)
                this.horizontalVelocity = min;
            if (this.horizontalVelocity > max)
                this.horizontalVelocity = max;
        }
    }

    public void setVelocita(Direction direzione, int value, int min, int max) {
        setVelocita(direzione, value, min, max, false);
    }

    /**
     * Aggiunge uno sprite alla lista di oggetti lasciati alla morte.
     *
     * @param spr Sprite da aggiungere
     */
    public void addSpriteDaDroppare(Sprite spr) {
        this.oggettiDaDroppare.add(spr);
    }

    /**
     * Alla morte dello sprite viene richiamato questo metodo che esegue nelle istruzioni generiche necessarie
     * per la fine della sua esecuzione.
     */
    public void onDeath() {
        animazioneStatica.stopAnimazione();
        animazioneMovimento.stopAnimazione();
    }

    public ArrayList<Sprite> getOggettiDaDroppare() {
        return oggettiDaDroppare;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Animazione getAnimazioneStatica() {
        return animazioneStatica;
    }

    public Animazione getAnimazioneMovimento() {
        return animazioneMovimento;
    }

    public int getPriorita() {
        return priorita;
    }

    public int getPunteggioFornito() {
        return punteggioFornito;
    }

    /**
     * @return true se lo sprite blocca il movimento degli altri sprite, false altrimenti
     */
    public boolean bloccaMovimento() {
        return false;
    }

    public boolean isDead() {
        return isDead;
    }

    /**
     * Implementazione del metodo di Comparable, permette di ordinare in maniera efficiente gli sprite
     * che devono essere disegnati prima nell'anteprima
     *
     * @param o2 sprite da comparare
     */
    public int compareTo(Sprite o2) {
        if (this.getPriorita() > o2.getPriorita())
            return 1;
        else if (this.getPriorita() == o2.getPriorita())
            return 0;
        else
            return -1;
    }

    /**
     * Imposta lo stato di "morte" allo sprite.
     * Tale stato farà in modo di cancellare lo sprite al prossimo ciclo di update della piattaforma
     */
    public void setDead() {
        isDead = true;
    }

    public Sprite clone() {
        try {
            return (Sprite) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "verticalVelocity=" + verticalVelocity +
                ", horizontalVelocity=" + horizontalVelocity +
                ", xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                ", width=" + width +
                ", height=" + height +
                ", isDead=" + isDead +
                ", nome='" + nome + '\'' +
                '}';
    }


}
