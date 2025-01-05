package src.model.piattaforma;


import src.model.piattaforma.Sprite.Sprite;

import java.awt.*;

/**
 * Una collisione avviene quando due sprite, ad un successivo passo, si intersecano.
 * Dunque viene creata una istanza di questa classe contenente i due sprite e la direzione di collisione rispetto al primo
 * sprite.
 */
public class Collisione {

    private static final Piattaforma piattaforma = Piattaforma.getInstance();
    private final Sprite primoSprite;
    private final Sprite secondoSprite;
    private final Sprite.Direction direction;
    private boolean toIgnore = true;

    /**
     * Crea una collisione fra due sprite.
     * La direzione passata come parametro è la direzione in cui avviene la collisione per il primo sprite.
     *
     * @param first     sprite da cui viene vista la collisione
     * @param second    sprite che collide con il primo
     * @param direction direzione della collisione rispetto al primo sprite
     */
    public Collisione(Sprite first, Sprite second, Sprite.Direction direction) {
        this.primoSprite = first;
        this.secondoSprite = second;
        this.direction = direction;
    }

    /**
     * Controlla se due sprite collidono.
     *
     * @param other     sprite con cui controllare se avvengono collisioni
     * @param xMovement x da aggiungere
     * @param yMovement y da aggiungere
     * @return un oggetto Collisione con tutte le informazioni sulla collisione
     */
    public static Collisione collideCon(Sprite first, Sprite other, int xMovement, int yMovement) {
        Rectangle firstSprite = new Rectangle(first.getX() + xMovement, first.getY() + yMovement, first.getWidth(), first.getHeight());
        Rectangle secondSprite = new Rectangle(other.getX(), other.getY(),
                other.getWidth(), other.getHeight());
        if (firstSprite.intersects(secondSprite)) {
            Sprite.Direction direzione;
            Rectangle its = firstSprite.intersection(secondSprite);
            double minH = firstSprite.getHeight();
            if (secondSprite.getHeight() < minH)
                minH = secondSprite.getHeight();
            if (its.getWidth() > its.getHeight() && its.getHeight() < minH) {
                if (its.getY() > firstSprite.getY())
                    direzione = Sprite.Direction.DOWN;
                else
                    direzione = Sprite.Direction.UP;
            } else {
                if (its.getX() > firstSprite.getX())
                    direzione = Sprite.Direction.RIGHT;
                else
                    direzione = Sprite.Direction.LEFT;
            }
            Collisione coll = new Collisione(first, other, direzione);
            other.onCollision(coll);
            return coll;
        } else {
            return null;
        }
    }

    /**
     * Funzione che controlla se lo sprite passato come parametro collide con un altro sprites all'interno della mappa.
     * Inoltre collideCon al suo interno richiamerà "onCollision" dello sprite con cui sta collidendo.
     *
     * @param sprite    sprite di cui controllare le collisioni
     * @param xMovement differenza sull'asse x rispetto alla posizione dello sprite
     * @param yMovement differenza sull'asse y rispetto alla posizione dello sprite
     * @return la direzione con cui collide con un oggetto di tipo blocco
     */
    public static ListaCollisioni rilevaCollisioni(Sprite sprite, int xMovement, int yMovement) {
        ListaCollisioni collisioni = new ListaCollisioni(sprite);
        synchronized (piattaforma.getSprites()) {
            for (Sprite spr : piattaforma.getSprites()) {
                Collisione coll = Collisione.collideCon(sprite, spr, xMovement, yMovement);
                if (coll != null && spr != sprite) {
                    if (spr.bloccaMovimento()) { //rileva la dir solo in caso di blocchi
                        if (coll.getDirection() == Sprite.Direction.DOWN)
                            sprite.setOnGround(true);
                        coll.setToIgnore(false);
                    }
                    collisioni.add(coll);
                }
            }
        }
        return collisioni;
    }

    public Sprite getPrimoSprite() {
        return primoSprite;
    }

    public Sprite getSecondoSprite() {
        return secondoSprite;
    }

    public Sprite.Direction getDirection() {
        return direction;
    }

    /**
     * Una collisione può essere ignorabile o no, se è ignorabile allora non viene processata dalla maggior parte
     * dei metodi degli sprite.
     *
     * @return true se è ignorabile, false altrimenti
     */
    public boolean isToIgnore() {
        return toIgnore;
    }

    /**
     * Imposta se una collisione è da ignorare
     */
    public void setToIgnore(boolean toIgnore) {
        this.toIgnore = toIgnore;
    }
}
