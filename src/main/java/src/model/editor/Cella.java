package src.model.editor;


import src.MainController;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Rappresenta una generica cella all'interno dell'editor.
 * Essa può contentere uno SpriteEditor o essere vuota.
 * <p>
 * Implementa l'interfaccia Comparable che renderà possibile l'ordinamento delle celle e la loro relativa visualizzazione
 * favorendo così un aumento di prestazioni e evitando eccessivi cali di frame.
 * Tale aumento di prestazioni deriverà dal fatto che per ogni gruppo di sprite uguali verrà scalata solamente una immagine
 * e usata per tutti gli sprites successivi. Eviterà così di utilizzare troppo spesso il metodo getScaledInstance di
 * BufferedImage, essendo questo un metodo molto costoso dal punto di vista delle prestazioni
 */
public class Cella implements Comparable<Cella> {

    private SpriteEditor contenuto;
    private final int x;
    private final int y;
    private boolean selezionato = false;
    private boolean hasWaypoint = false;

    /**
     * Costruttore della cella
     *
     * @param x coordinata x della cella
     * @param y coordinata y della cella
     */
    public Cella(int x, int y) {
        this.x = x;
        this.y = y;
        contenuto = null;
    }

    public SpriteEditor getContenuto() {
        return contenuto;
    }

    public void setContenuto(SpriteEditor contenuto) {
        this.contenuto = contenuto;
        if (contenuto != null && contenuto.getAssociato() != this)
            contenuto.setAssociato(this);
    }

    /**
     * Restituisce un immagine basandosi sullo stato attuale della cella.
     * Se la cella è selezionata allora verrà evidenziata, se la cella contiene un waypoint allora verra inserito un puntino
     * rosso al suo interno.
     *
     * @param dimX larghezza relativa della cella nell'anteprima, quindi con relativo zoom
     * @param dimY altezza relativa della cella nell'anteprima, quindi con relativo zoom
     * @return immagine da visualizzare, adattata alla dimensione della cella
     */
    public Image getImmagine(int dimX, int dimY) {
        Image img = null;

        if (contenuto != null && contenuto.getImmagine() != null) {
            img = contenuto.getImmagine().getScaledInstance(dimX, dimY, Image.SCALE_FAST);
        }
        if (selezionato) {
            BufferedImage tmp = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = tmp.createGraphics();
            g.drawImage(img, 0, 0, dimX, dimY, MainController.getInstance().getFrame());
            g.setColor(Color.BLUE);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            g.fillRect(0, 0, dimX, dimY);
            g.dispose();
            img = tmp;
        }
        if (hasWaypoint) {
            BufferedImage tmp = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = tmp.createGraphics();
            g.drawImage(img, 0, 0, dimX, dimY, MainController.getInstance().getFrame());
            g.setColor(Color.RED);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g.fillOval(dimX / 2 - dimX / 8, dimY / 2 - dimY / 8, dimX / 4, dimY / 4);
            g.dispose();
            img = tmp;
        }
        return img;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setWaypoint(boolean hasWaypoint) {
        this.hasWaypoint = hasWaypoint;
    }

    public void setSelezionato(boolean selezionato) {
        this.selezionato = selezionato;
    }

    public boolean isSelezionato() {
        return selezionato;
    }

    public boolean hasWaypoint() {
        return hasWaypoint;
    }

    /**
     * Implementa il metodo compareTo di Comparable, ciò permette di eseguire il Collections.sort() e quindi di ordinare
     * la lista di celle all'interno dell'editor. Tale ordinamento permette un miglioramento sostanziale delle performance di
     * aggiornamento dell'anteprima. L'ordinamento si baserà sul nome dello sprite contenuto all'interno, permettendo
     * di mettere gli sprite uguali vicini fra loro.
     *
     * @param T la cella con cui dovrà essere comparata
     * @return
     */
    public int compareTo(Cella T) {
        if (contenuto == null && T.getContenuto() == null)
            return 0;
        else if (contenuto == null)
            return -1;
        else if (T.getContenuto() == null)
            return 1;
        else
            return contenuto.getNome().compareTo(T.getContenuto().getNome());
    }
}
