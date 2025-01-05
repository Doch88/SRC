package src.model.piattaforma;

import src.model.piattaforma.Sprite.Sprite;

import java.util.ArrayList;

/**
 * Viene restituita dalle funzioni statiche della classe Collisione, è una lista contenente tutte le collisioni
 * che effettua un determinato sprite.
 */
public class ListaCollisioni extends ArrayList<Collisione> {

    private final Sprite sprite;

    /**
     * Costruttore della lista di collisioni di un determinato sprite
     *
     * @param sprite lo sprite con cui sono state effettuate le collisioni.
     *               Ogni collisione e direzione della lista sarà relativa a questo sprite
     */
    public ListaCollisioni(Sprite sprite) {
        super();
        this.sprite = sprite;
    }

    /**
     * Restituisce il primo sprite della lista del tipo indicato che collide nella direzione passata come parametro
     *
     * @param tipoSprite tipo di sprite con cui controllare le collisioni
     * @param direction  direzione da verificare
     * @return il primo sprite che collide nella direzione direction di tipo tipoSprite o null se non esiste nessuno sprite
     * che soddisfi le condizioni
     */
    public Sprite collideCon(Class tipoSprite, Sprite.Direction direction) {
        for (Collisione coll : this) {
            if (coll.getSecondoSprite() != null && coll.getSecondoSprite().getClass() == tipoSprite &&
                    (direction == Sprite.Direction.NO_DIRECTION || coll.getDirection() == direction)) {
                return coll.getSecondoSprite();
            }
        }
        return null;
    }

    /**
     * Prende tutte le collisioni della lista la cui direzione sia quella passata come parametro
     *
     * @param direction direzione che devono avere le collisioni della sottolista
     * @return una sottolista che contenga elementi della lista principale con direzione uguale a quella passata come
     * parametro
     */
    public ListaCollisioni collisioniDirezione(Sprite.Direction direction) {
        ListaCollisioni collDirezione = new ListaCollisioni(sprite);
        if (isEmpty()) return this;
        for (Collisione coll : this)
            if (coll.getDirection() == direction)
                collDirezione.add(coll);
        return collDirezione;
    }

    /**
     * Controlla se lo sprite collide nella direzione indicata
     *
     * @param direction direzione da controllare
     * @return true se collide con qualcosa, false altrimenti
     */
    public boolean collideDirezione(Sprite.Direction direction) {
        return !collisioniDirezione(direction).isEmpty();
    }

    /**
     * Controlla se le collisioni della lista possono essere ignorate ai fini del movimento
     *
     * @return true se le collisioni all'interno soddisfano tutte la condizione Collisione.isIgnore(), false altrimenti
     */
    public boolean toIgnore() {
        if (isEmpty()) return true;
        for (Collisione coll : this)
            if (!coll.isToIgnore()) return false;
        return true;
    }
}
