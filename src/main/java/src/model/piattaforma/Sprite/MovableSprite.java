package src.model.piattaforma.Sprite;


import src.model.piattaforma.Collisione;
import src.model.piattaforma.ListaCollisioni;

import java.awt.*;

/**
 * Classe che estende Sprite e che indica gli sprites che si muovono avanti e indietro fra due waypoints.
 * Avremo principalmente due sprite di questo tipo: blocchi e NPC.
 */
public abstract class MovableSprite extends Sprite {

    /**
     * La direzione attuale dello sprite, la direzione in cui si sta muovendo
     */
    protected Sprite.Direction direzione = null;
    /**
     * Il waypoint sinistro
     */
    protected int waypoint1 = 0;
    /**
     * Il waypoint destro
     */
    protected int waypoint2 = 0;
    /**
     * Velocita di movimento, se la velocità è nulla allora lo sprite è immobile
     */
    protected int velocita;

    public MovableSprite(String nome, Image immagine, int velocita) {
        super(nome, immagine);
        this.direzione = Direction.LEFT;
        this.velocita = velocita;
    }

    /**
     * Inverte la direzione di movimento dello sprite, sia essa a destra o sinistra
     *
     * @return la nuova direzione
     */
    public Sprite.Direction changeDirection() {
        if (direzione == Direction.LEFT)
            direzione = Direction.RIGHT;
        else
            direzione = Direction.LEFT;
        return direzione;
    }

    public Rectangle getWaypoint1() {
        return new Rectangle(waypoint1 - getWidth() / 2, getY() - getHeight() / 2, getWidth(), getHeight());
    }

    /**
     * Il waypoint 1 sarà il waypoint sinistro dello sprite
     *
     * @param x coordinata x del waypoint
     */
    public void setWaypoint1(int x) {
        this.waypoint1 = x;
    }

    public Rectangle getWaypoint2() {
        return new Rectangle(waypoint2 + getWidth() / 2, getY() + getHeight() / 2, getWidth(), getHeight());
    }

    /**
     * Il waypoint 2 sarà il waypoint destro dello sprite
     *
     * @param x coordinata x del waypoint
     */
    public void setWaypoint2(int x) {
        this.waypoint2 = x;
    }

    /**
     * Indica se lo sprite può muoversi, basandosi sulla sua velocità
     *
     * @return true se velocita è diverso da 0, false altrimenti
     */
    public boolean canMove() {
        return (velocita != 0 && waypoint1 != waypoint2);
    }

    /**
     * Il movimento di un MovableSprite è caratterizzato da un andare da un waypoint ad un altro, al contatto con un waypoint o con un blocco
     * la direzione viene invertita
     */
    @Override
    public void muovi() {
        if (!canMove()) return;
        Rectangle rectangleSprite = new Rectangle(this.getX(), this.getY(), width, height);

        ListaCollisioni movimento = Collisione.rilevaCollisioni(this, horizontalVelocity, 0);

        if (movimento != null && !movimento.toIgnore() && movimento.collideDirezione(direzione))
            changeDirection();
        else {
            movimento = Collisione.rilevaCollisioni(this, horizontalVelocity, 0);
            if (movimento == null || movimento.toIgnore())
                this.xPosition += horizontalVelocity;
            else
                changeDirection();
        }

        if (getWaypoint1().intersects(rectangleSprite))
            direzione = Sprite.Direction.RIGHT;
        else if (getWaypoint2().intersects(rectangleSprite))
            direzione = Sprite.Direction.LEFT;
        this.setVelocita(direzione, 1, -velocita, velocita);
    }

    public Direction getDirezione() {
        return direzione;
    }

    public int getVelocita() {
        return velocita;
    }
}
