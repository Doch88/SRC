package src.model.piattaforma;


import src.model.piattaforma.Sprite.Sprite;
import src.utils.Settings;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Classe che rappresenta e gestisce le animazioni, ha associato un timer ed un ArrayList che gestirà a seconda del framerate
 * del gioco attualmente in esecuzione.
 */

public class Animazione implements ActionListener {

    /**
     * Framerate che verrà modificato dal PiattaformaController per avere un delay adeguato al framerate del gioco
     */
    private static int framerate = 60;
    /**
     * L'ArrayList contenente le immagini delle animazioni
     */
    private final ArrayList<Image> listaImmagini;
    private final ArrayList<Image> listaImmaginiSpecchiate;
    /**
     * L'indice dell'immagine attuale dell'animazione che verrà mostrata
     */
    private int immagineCorrente = 0;
    /**
     * Il timer che gestisce la successione di animazioni e il tempo in cui verranno mostrate
     */
    private Timer animTimer;
    private boolean continuous = true;
    private int stopTimer = 0;

    /**
     * Crea una nuova animazione senza aggiungere immagini aggiuntive
     *
     * @param nome: nome dei file dell'animazione
     */
    public Animazione(String nome) {
        listaImmagini = new ArrayList<>();
        listaImmaginiSpecchiate = new ArrayList<>();
        getAnimationArray(nome);
    }

    public Animazione(String nome, String cartella) {
        listaImmagini = new ArrayList<>();
        listaImmaginiSpecchiate = new ArrayList<>();
        getAnimationArray(nome, cartella);
    }

    /**
     * Costruttore che crea una nuova animazione partendo da un immagine iniziale che verrà inserita all'interno dell'ArrayList
     *
     * @param nome:     nome dei file dell'animazione
     * @param immagine: immagine iniziale da aggiungere
     */
    public Animazione(String nome, Image immagine) {
        listaImmagini = new ArrayList<>();
        listaImmaginiSpecchiate = new ArrayList<>();
        listaImmagini.add(immagine);
        listaImmaginiSpecchiate.add(Utils.mirrorImage(immagine));
        getAnimationArray(nome);
    }

    public Animazione(String nome, String cartella, Image immagine) {
        listaImmagini = new ArrayList<>();
        listaImmaginiSpecchiate = new ArrayList<>();
        listaImmagini.add(immagine);
        listaImmaginiSpecchiate.add(Utils.mirrorImage(immagine));
        getAnimationArray(nome, cartella);
    }

    public static void setFramerate(int framerate) {
        Animazione.framerate = framerate;
    }

    /**
     * Restituisce l'immagine attuale dell'animazione
     */
    public Image getImage(Sprite.Direction direction) {
        if (animTimer == null || !animTimer.isRunning())
            avviaAnimazione();
        if (listaImmagini.isEmpty()) return null;
        return direction == Sprite.Direction.LEFT ?
                listaImmaginiSpecchiate.get(immagineCorrente)
                : listaImmagini.get(immagineCorrente);
    }

    /**
     * Ferma l'animazione e impedisce quindi di modificare il valore immagineCorrente
     */
    public void stopAnimazione() {
        if (animTimer != null && animTimer.isRunning())
            animTimer.stop();
    }

    /**
     * Avvia o riavvia il timer dell'animazione per iniziare un nuovo ciclo
     */
    public void avviaAnimazione() {
        if (animTimer == null && !listaImmagini.isEmpty()) {
            immagineCorrente = 0;
            int timerTime = 1000 / framerate * listaImmagini.size();
            if (listaImmagini.size() > 5)
                timerTime = 1000 / framerate * 2;
            animTimer = new Timer(timerTime, this);
            animTimer.start();
        } else if (!listaImmagini.isEmpty() && !animTimer.isRunning())
            animTimer.restart();
    }

    /**
     * Crea l'ArrayList delle immagini dell'animazione prendendolo dalla cartella passata per parametro
     *
     * @param nome:     il nome del file iniziale da cui partiranno tutte le altre immagini dell'animazione
     * @param cartella: cartella da dove estrarre le immagini
     */
    private void getAnimationArray(String nome, String cartella) {
        boolean continua = true;
        for (int i = 1; continua; i++) {
            continua = false;
            Image tmp = Utils.getBufferedImage(cartella, nome + "_" + i);
            if (tmp != null) {
                listaImmagini.add(tmp);
                listaImmaginiSpecchiate.add(Utils.mirrorImage(tmp));
                continua = true;
            }
        }
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    private void getAnimationArray(String nome) {
        getAnimationArray(nome, Settings.CARTELLA_SPRITES);
    }

    /**
     * Richiama il metodo isEmpty() di ArrayList
     */
    public boolean isEmpty() {
        return listaImmagini.isEmpty();
    }

    /**
     * Richiama il metodo size() di ArrayList
     */
    public int size() {
        return listaImmagini.size();
    }

    /**
     * Richiama il metodo isRunning() di Timer
     */
    public boolean isRunning() {
        return animTimer != null && animTimer.isRunning();
    }

    /**
     * Ogni volta che passa un determinato intervallo di tempo (basato sul framerate), il timer richiama questa funzione e viene cambiato il valore
     * attuale dell'immagine.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        immagineCorrente = (immagineCorrente + 1) % listaImmagini.size();
        if (immagineCorrente == 0 && !continuous) {
            stopTimer++;
            if (stopTimer >= ((1000) / framerate) * 2) {
                this.stopAnimazione();
                stopTimer = 0;
            }
        }
    }
}
