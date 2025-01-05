package src.model.piattaforma;

import src.model.Mappa;
import src.model.Salvataggio;
import src.model.piattaforma.Sprite.Player;
import src.model.piattaforma.Sprite.Portale;
import src.model.piattaforma.Sprite.Raccoglibile;
import src.model.piattaforma.Sprite.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Model della piattaforma che contiene tutte le informazioni sulla mappa e il salvataggio in esecuzione.
 * Contiene anche lo stato della partita, caratterizzanti della situazione attuale del gioco.
 */
public class Piattaforma {

    private static Piattaforma instance = null;

    public static final int STATO_PARTITA_TEST = -1;
    public static final int STATO_PARTITA_FINE_TEST = -2;
    public static final int STATO_PARTITA_IN_CORSO = 0;
    public static final int STATO_PARTITA_GAMEOVER = 1;
    public static final int STATO_PARTITA_LIVELLO_COMPLETATO = 2;
    public static final int STATO_PARTITA_USCITA_MANUALE = 3;
    private int statoPartita = STATO_PARTITA_IN_CORSO;

    private Salvataggio salvataggio;
    private Mappa mappa;

    private Player player;
    private List<Sprite> sprites;
    /**
     * lista degli sprites che devono essere eliminati alla fine del frame,
     * questo viene fatto per evitare problemi di modifiche concorrenziali
     * tra il Thread di updateView e quello principale
     */
    private ArrayList<Sprite> spritesDaRimuovere;
    /**
     * lista degli sprites che devono essere aggiunti alla fine del frame salvataggio,
     * questo viene fatto per evitare problemi di modifiche concorrenziali
     * tra il Thread di updateView e quello principale
     */
    private ArrayList<Sprite> spritesDaAggiungere;
    //private ArrayList<HashMap<String, Integer>> itemCollezionati;

    private Animazione animazionePuntoIniziale, animazionePuntoFinale;

    private int xMax = 0, yMax = 0;

    public static Piattaforma getInstance() {
        if (instance == null)
            instance = new Piattaforma();
        return instance;
    }

    public void init(Salvataggio salvataggio, Mappa mappa, Player player) {
        this.salvataggio = salvataggio;
        this.mappa = mappa;
        this.player = player;
        sprites = new ArrayList<>();
        spritesDaRimuovere = new ArrayList<>();
        spritesDaAggiungere = new ArrayList<>();
        //itemCollezionati = new ArrayList<>();
    }

    /**
     * Aggiunge uno sprite alla lista degli sprite da aggiungere.
     * Alla fine di ogni ciclo di update tale lista viene svuotata ed ogni suo elemento aggiunto alla lista degli sprite
     * contenuti nella mappa
     *
     * @param sprite sprite da aggiungere alla mappa
     */
    public void aggiungiSprite(Sprite sprite) {
        spritesDaAggiungere.add(sprite);
    }

    /**
     * Aggiunge uno sprite alla lista degli sprite da rimuovere.
     * Alla fine di ogni ciclo di update tale lista viene svuotata ed ogni suo elemento comparato con quelli
     * degli sprite contenuti nella mappa, e se presente un elemento in entrambi allora viene eliminato
     *
     * @param sprite sprite da eliminare dalla mappa
     */
    public void rimuoviSprite(Sprite sprite) {
        if (spritesDaRimuovere.contains(sprite))
            return;
        sprite.onDeath();
        spritesDaRimuovere.add(sprite);
    }

    /**
     * Controlla se il player collide con il punto di fine livello.
     *
     * @return true se collide, false altrimenti
     */
    public boolean collisionePuntoFine() {
        Rectangle puntoFine = new Rectangle(mappa.getCellaFineX() * mappa.getDimCella(), mappa.getCellaFineY() * mappa.getDimCella(), mappa.getDimCella(), mappa.getDimCella());
        Rectangle playerRectangle = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        return puntoFine.intersects(playerRectangle);
    }

    /**
     * Aggiorna la lista degli sprite, aggiungendo gli sprite contenuti nella lista spritesDaAggiungere e rimuovendo quelli
     * contenuti nella lista di quelli da rimuovere
     */
    public void updateListaSprites() {
        synchronized (sprites) {
            sprites.addAll(spritesDaAggiungere);     //Controlla se sono presenti sprites da aggiungere
            sprites.removeAll(spritesDaRimuovere);   //Controlla se sono presenti sprites da rimuovere e li rimuove
            if (!spritesDaAggiungere.isEmpty() || !spritesDaAggiungere.isEmpty()) {
                Collections.sort(sprites);
                spritesDaAggiungere.clear();
                spritesDaRimuovere.clear();
            }
        }

    }

    /**
     * Se la salute del player è minore o uguale a zero
     * o se il player supera il limite inferiore
     * allora avviene il gameover
     */
    public void checkGameover() {
        if (player.getSaluteAttuale() <= 0 || player.getY() > yMax)
            this.setStatoPartitaGameover();
    }

    /**
     * Calcola il valore totale di un certo collezionabile contenuto all'interno della mappa
     *
     * @param nome il nome del collezionabile da calcolare
     * @return il valore totale di tale collezionabile
     */
    public int calcolaCollezionabileTotale(String nome) {
        int collezionabileTotale = salvataggio.getCollezionabili(this.getMappa().getPosizione()).get(nome) != null ? salvataggio.getCollezionabili(this.getMappa().getPosizione()).get(nome) : 0;
        synchronized (sprites) {
            for (Sprite spr : sprites) {
                if (spr instanceof Raccoglibile && spr.getNome().equals(nome))
                    collezionabileTotale += ((Raccoglibile) spr).getValore();
                else {
                    for (Sprite drop : spr.getOggettiDaDroppare()) {
                        if (drop instanceof Raccoglibile && drop.getNome().equals(nome)) {
                            collezionabileTotale += ((Raccoglibile) drop).getValore();
                        }
                    }
                }
            }
        }
        return collezionabileTotale;
    }

    /**
     * Aggiunge un dato valore raccolto dal player al totale di tale collezionabile
     *
     * @param nome  il nome del collezionabile da aggiornare
     * @param value il valore da aggiungere
     */
    public void addCollezionabile(String nome, int value) {
        if (this.salvataggio.getCollezionabili(this.getMappa().getPosizione()) == null)
            this.salvataggio.getCollezionabili().add(new HashMap<>());
        if (this.salvataggio.getCollezionabili(this.getMappa().getPosizione()).containsKey(nome)) {
            int valorePrecedente = this.salvataggio.getCollezionabili(this.getMappa().getPosizione()).get(nome);
            this.salvataggio.getCollezionabili(this.getMappa().getPosizione()).put(nome, valorePrecedente + value);
        } else
            this.salvataggio.getCollezionabili(this.getMappa().getPosizione()).put(nome, value);
    }

    /**
     * Controlla se il player collide con un portale
     *
     * @return il portale con cui collide il player o null se non collide con nessun portale
     */
    public Portale collisionePortale() {
        List<Collisione> collisioni = Collisione.rilevaCollisioni(this.player, 0, 0);
        if (collisioni == null) return null;
        for (Collisione coll : collisioni) {
            if (coll.getSecondoSprite() instanceof Portale)
                return (Portale) coll.getSecondoSprite();
        }
        return null;
    }

    /**
     * Associa due portali fra di loro, per fare in modo che uno teletrasporti all'altro
     *
     * @param sprites La lista degli sprites all'interno alla mappa
     * @param portale il portale da associare
     * @param id      l'id dell'altro portale da associare
     */
    public void associaPortale(List<Sprite> sprites, Portale portale, int id) {
        synchronized (sprites) {
            for (Sprite spr : sprites) {
                if (spr instanceof Portale && ((Portale) spr).getIDPortale() == id) {
                    ((Portale) spr).setAssociato(portale);
                    return;
                }
            }
        }
    }

    /**
     * Ritorna lo stato attuale del gioco
     *
     * @return STATO_PARTITA_TEST Se si è in modalità testing dell'editor
     * STATO_PARTITA_FINE_TEST Se è terminata la fase di test, e si deve quindi tornare all'editor
     * STATO_PARTITA_IN_CORSO Se la partita è normalmente in esecuzione
     * STATO_PARTITA_GAMEOVER Se la partita è invece terminata in seguito a morte del player
     * STATO_PARTITA_LIVELLO_COMPLETATO Se la partita è terminata e si deve procedere al livello successivo
     */
    public int getStatoPartita() {
        return statoPartita;
    }

    private void setStatoPartita(int statoPartita) {
        this.statoPartita = statoPartita;
    }

    /**
     * Ritorna se la partita è normalmente in esecuzione
     *
     * @return true in caso positivo, false altrimenti
     */
    public boolean isStatoPartitaInCorso() {
        return this.statoPartita == Piattaforma.STATO_PARTITA_IN_CORSO ||
                this.statoPartita == Piattaforma.STATO_PARTITA_TEST;
    }

    /**
     * Imposta lo stato della partita come STATO_PARTITA_IN_CORSO
     */
    public void setStatoPartitaInCorso() {
        if (this.statoPartita != Piattaforma.STATO_PARTITA_TEST)
            this.setStatoPartita(Piattaforma.STATO_PARTITA_IN_CORSO);
    }

    public void setStatoPartitaUscitaManuale() {
        if (this.statoPartita != Piattaforma.STATO_PARTITA_TEST)
            this.setStatoPartita(Piattaforma.STATO_PARTITA_USCITA_MANUALE);
        else
            this.setStatoPartita(Piattaforma.STATO_PARTITA_FINE_TEST);
    }

    /**
     * Imposta lo stato della partita come STATO_PARTITA_GAMEOVER
     */
    public void setStatoPartitaGameover() {
        if (this.statoPartita == Piattaforma.STATO_PARTITA_TEST)
            this.setStatoPartita(Piattaforma.STATO_PARTITA_FINE_TEST);
        else
            this.setStatoPartita(Piattaforma.STATO_PARTITA_GAMEOVER);
    }

    /**
     * Imposta lo stato della partita come STATO_PARTITA_LIVELLO_COMPLETATO
     */
    public void setStatoPartitaLivelloCompletato() {
        if (this.statoPartita == Piattaforma.STATO_PARTITA_TEST)
            this.setStatoPartita(Piattaforma.STATO_PARTITA_FINE_TEST);
        else
            this.setStatoPartita(Piattaforma.STATO_PARTITA_LIVELLO_COMPLETATO);
    }

    /**
     * Imposta lo stato della partita come STATO_PARTITA_TEST
     */
    public void setStatoPartitaTest() {
        this.setStatoPartita(Piattaforma.STATO_PARTITA_TEST);
    }

    /**
     * Ritorna il player attualmente in gioco
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Imposta il player attualmente in gioco
     *
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return La lista degli sprites contenuti nella mappa.
     */
    public List<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(List<Sprite> sprites) {
        this.sprites = sprites;
        this.sprites.add(player);
        Collections.sort(sprites);
        this.setCoordinateMax();
    }

    /**
     * Imposta le xMax e yMax, necessarie per determinare i limiti della mappa e di movimento di determinati sprites.
     */
    public void setCoordinateMax() {
        xMax = yMax = 0;
        for (Sprite sprite : sprites) {
            if (yMax < sprite.getY() + sprite.getHeight())
                yMax = sprite.getY() + sprite.getHeight();
            if (xMax < sprite.getX() + sprite.getWidth())
                xMax = sprite.getX() + sprite.getWidth();
        }
    }

    public Point getCoordinatePuntoFine() {
        return new Point(mappa.getCellaFineX() * mappa.getDimCella(),
                mappa.getCellaFineY() * mappa.getDimCella());
    }

    public Point getCoordinatePuntoIniziale() {
        return new Point(mappa.getCellaInizioX() * mappa.getDimCella(),
                mappa.getCellaInizioY() * mappa.getDimCella());
    }

    public Animazione getAnimazionePuntoIniziale() {
        return animazionePuntoIniziale;
    }

    public void setAnimazionePuntoIniziale(Animazione animazionePuntoIniziale) {
        this.animazionePuntoIniziale = animazionePuntoIniziale;
    }

    public Animazione getAnimazionePuntoFinale() {
        return animazionePuntoFinale;
    }

    public void setAnimazionePuntoFinale(Animazione animazionePuntoFinale) {
        this.animazionePuntoFinale = animazionePuntoFinale;
    }

    public Salvataggio getSalvataggio() {
        return salvataggio;
    }

    public void setSalvataggio(Salvataggio salvataggio) {
        this.salvataggio = salvataggio;
    }

    public Mappa getMappa() {
        return mappa;
    }

    public void setMappa(Mappa mappa) {
        this.mappa = mappa;
    }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }

}
