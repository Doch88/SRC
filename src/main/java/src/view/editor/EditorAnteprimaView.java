package src.view.editor;

import src.MainController;
import src.model.editor.Cella;
import src.model.editor.SpriteEditor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * View dell'editor che mostra l'anteprima della mappa.
 * Contiene una griglia, ridimensionata dallo zoom, e i vari sprite contenuti all'interno della mappa.
 * Questa view viene aggiornata ad ogni ciclo e, dato che contiene parecchi sprite ridimensionati per farli entrare nei blocchi, deve
 * contenere alcuni algoritmi di ottimizzazione basati sull'ordinamento degli sprite.
 */
public class EditorAnteprimaView extends AbstractEditorView {

    public static EditorAnteprimaView instance;

    private EditorAnteprimaView() {
        super();
    }

    public static EditorAnteprimaView getInstance() {
        if (instance == null)
            instance = new EditorAnteprimaView();
        return instance;
    }

    @Override
    public void init() {

        //this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.requestFocusInWindow();

        //Per evitare problemi nel caso si passi da Piattaforma ad init
        if (MainController.getInstance().getFrame().getKeyListeners().length > 0)
            MainController.getInstance().getFrame().removeKeyListener(MainController.getInstance().getFrame().getKeyListeners()[0]);

    }

    @Override
    public void update() {
        this.repaint();
    }

    /**
     * Disegna la griglia dell'anteprima.
     *
     * @param g         Graphics dove verrà stampata la griglia
     * @param xPosition la coordinata x di spostamento della visuale tramite tastiera
     * @param yPosition la coordinata y di spostamento della visuale tramite tastiera
     */
    private void disegnaGriglia(Graphics g, int xPosition, int yPosition) {
        g.setColor(new Color(0xC4C4C4));

        int dimCellaAnteprima = editor.getDimCellaAnteprima();

        for (int i = 1; i <= editor.getNumCelleAsse(); i++) {
            g.drawLine(dimCellaAnteprima * i - xPosition, -yPosition,
                    dimCellaAnteprima * i - xPosition, this.getHeight());
        }
        for (int i = 1; i <= editor.getNumCelleAsse(); i++) {
            g.drawLine(-xPosition, dimCellaAnteprima * i - yPosition,
                    this.getWidth(), dimCellaAnteprima * i - yPosition);
        }
    }

    /**
     * Imposta i parametri relativi alla grandezza e dimensioni dell'anteprima
     *
     * @param dimCella     dimensione della cella
     * @param altAnteprima altezza dell'anteprima
     * @param lunAnteprima lunghezza dell'anteprima
     */
    public void setParameters(int dimCella, int altAnteprima, int lunAnteprima) {
        editor.setDimCellaAnteprima(dimCella);
        editor.setAltezzaAnteprima(altAnteprima);
        editor.setLunghezzaAnteprima(lunAnteprima);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Per diminuire il lag usiamo un double buffering
        BufferedImage bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bufferImage.getGraphics();

        int dimCellaAnteprima = (int) Math.round((double) Math.max(this.getWidth(), this.getHeight()) / (double) editor.getNumCelleAsse()) + editor.getZoomAnteprima();

        setParameters(dimCellaAnteprima, this.getHeight(), this.getWidth());

        g2.drawImage(editor.getImmagine(), 0, 0, this.getWidth(), this.getHeight(), null);
        Cella cIn = editor.getCellaInizio(); //Cella iniziale
        Cella cFi = editor.getCellaFine(); //Cella finale
        Image iIn = editor.getImmagineInizio();
        Image iFi = editor.getImmagineFine();
        g2.drawImage(iIn,
                cIn.getX() * dimCellaAnteprima - editor.getxAnteprima(),
                cIn.getY() * dimCellaAnteprima - editor.getyAnteprima(),
                dimCellaAnteprima, dimCellaAnteprima, this);

        g2.drawImage(iFi,
                cFi.getX() * dimCellaAnteprima - editor.getxAnteprima(),
                cFi.getY() * dimCellaAnteprima - editor.getyAnteprima(),
                dimCellaAnteprima, dimCellaAnteprima, this);

        SpriteEditor lastSprite = null; //Per fini di ottimizzazione
        Image lastImage = null; //Per fini di ottimizzazione
        synchronized (editor.getListaCelle()) {
            for (Cella cella : editor.getListaCelle()) {
                if (cella.isSelezionato() || cella.hasWaypoint()) {
                    g2.drawImage(cella.getImmagine(dimCellaAnteprima, dimCellaAnteprima), cella.getX() * dimCellaAnteprima - editor.getxAnteprima(),
                            cella.getY() * dimCellaAnteprima - editor.getyAnteprima(),
                            this);
                } else if (cella.getContenuto() != null &&
                        cella.getX() * dimCellaAnteprima - editor.getxAnteprima() < this.getWidth() &&
                        cella.getY() * dimCellaAnteprima - editor.getyAnteprima() < this.getHeight()) {
                    if (cella.getContenuto() == null || lastSprite == null || !cella.getContenuto().getNome().equals(lastSprite.getNome())) {
                        lastSprite = cella.getContenuto();
                        lastImage = cella.getImmagine(dimCellaAnteprima, dimCellaAnteprima);
                        g2.drawImage(lastImage, cella.getX() * dimCellaAnteprima - editor.getxAnteprima(),
                                cella.getY() * dimCellaAnteprima - editor.getyAnteprima(),
                                this);
                    } else {
                        g2.drawImage(lastImage, cella.getX() * dimCellaAnteprima - editor.getxAnteprima(),
                                cella.getY() * dimCellaAnteprima - editor.getyAnteprima(),
                                this);
                    }
                }
            }
        }
        disegnaGriglia(g2, editor.getxAnteprima(), editor.getyAnteprima());

        g.drawImage(bufferImage, 0, 0, this);

    }

}