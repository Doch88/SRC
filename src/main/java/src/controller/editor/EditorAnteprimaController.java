package src.controller.editor;

import src.model.editor.Cella;
import src.model.editor.Editor;
import src.utils.Settings;
import src.view.editor.EditorAnteprimaView;

import java.awt.event.*;

/**
 * Controller dell'anteprima dell'editor che gestisce l'input e lo spostamento/ridimensionamento dell'anteprima.
 */
public class EditorAnteprimaController extends AbstractEditorController {

    private static EditorAnteprimaController instance = null;

    /**
     * carica l'istanza della view dell'anteprima e crea i listener associati ai bottoni e ai tasti
     */
    private EditorAnteprimaController() {
        super();
        view = EditorAnteprimaView.getInstance();

        view.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickSuCellaAction(e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        view.addMouseWheelListener(e ->
                ridimensionaAnteprimaAction(e.getWheelRotation(), e.getScrollType(), e.getScrollAmount()));

        view.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT ||
                        keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN)
                    muoviAnteprimaAction(keyCode);
                if (e.getKeyCode() == Editor.KEY_DUPLICA)
                    if (editor.getSpriteSelezionato() != null) {
                        editor.duplicaSpriteSelezionato();
                        editor.setModalitaAttuale(Editor.MOD_POSIZIONAMENTO);
                    }
                if (e.getKeyCode() == Editor.KEY_ELIMINA)
                    editor.setModalitaAttuale(Editor.MOD_ELIMINAZIONE);
                if (e.getKeyCode() == KeyEvent.VK_DELETE)
                    editor.eliminaSpriteCella();
                if (e.getKeyCode() == Editor.KEY_POSIZIONA)
                    editor.setModalitaAttuale(Editor.MOD_POSIZIONAMENTO);
                updateAll();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
    }

    public static EditorAnteprimaController getInstance() {
        if (instance == null)
            instance = new EditorAnteprimaController();
        return instance;
    }

    /**
     * inizializza l'anteprima avviando un thread che aggiorna la view ad ogni frame
     * (il numero di frame al secondo è definito in Settings.STANDARD_FRAMERATE)
     */
    @Override
    public void init() {
        super.init();
        Thread loop = new Thread(() -> {
            int millisec = 1000 / Settings.STANDARD_FRAMERATE;
            while (editor.getModalitaAttuale() != Editor.MOD_CHIUSURA_EDITOR) {
                view.update();
                editor.adjustZoomAnteprima();
                editor.ordinaListaCelle();
                try {
                    Thread.sleep(millisec);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        loop.start();
    }

    /**
     * A seconda della modalità attualmente in uso esegue delle azioni sulla cella attualmente selezionata.
     * Tali azioni richiameranno delle funzioni di editor che applicheranno modifiche alla mappa attuale o all'anteprima.
     * A seconda della modalità selezionata avremo:
     * MOD_POSIZIONAMENTO: lo sprite attualmente selezionato verrà spostato (o inserito) nella cella scelta.
     * MOD_ELIMINAZIONE: lo sprite contenuto all'interno della cella scelta verrà eliminato e la cella svuotata.
     * MOD_WAYPOINT1 e MOD_WAYPOINT2: imposta le coordinate dei waypoint dello sprite, rispettivamente di quello a sinistra
     * e di quello a destra
     * MOD_ASSOCIA_PORTALE: associa allo sprite attualmente selezionato lo sprite contenuto nella cella scelta, se entrambi sono
     * di tipo portale
     * MOD_PUNTO_FINE e MOD_PUNTO_INIZIO: imposta la cella iniziale (quella in cui spawnerà il player la prima volta) e la cella finale,
     * cioè il punto in cui sarà necessario arrivare per vincere la partita
     *
     * @param x la coordinata x della cella selezionata
     * @param y la coordinata y della cella selezionata
     */
    private void clickSuCellaAction(int x, int y) {
        Cella cellaPrev = editor.getCellaSelezionata();
        editor.setCellaSelezionata(editor.getCella(x, y), false);
        if (editor.getCellaSelezionata() != null) {
            switch (editor.getModalitaAttuale()) {
                case Editor.MOD_POSIZIONAMENTO:
                    editor.posizionaSpriteSelezionato();
                    break;
                case Editor.MOD_ELIMINAZIONE:
                    editor.eliminaSpriteDallaMappa();
                    break;
                case Editor.MOD_WAYPOINT1:
                    editor.setWaypoint(cellaPrev.getContenuto(), true);
                    break;
                case Editor.MOD_WAYPOINT2:
                    editor.setWaypoint(cellaPrev.getContenuto(), false);
                    break;
                case Editor.MOD_ASSOCIA_PORTALE:
                    editor.setCellaSelezionata(editor.getCella(x, y), true);
                    editor.associaPortale(cellaPrev.getContenuto());
                    break;
                case Editor.MOD_PUNTO_INIZIO:
                    editor.setCellaInizio();
                    break;
                case Editor.MOD_PUNTO_FINE:
                    editor.setCellaFine();
                    break;
                default:
                    editor.setCellaSelezionata(editor.getCella(x, y), true);
                    break;
            }
        }

        editor.setModalitaAttuale(Editor.MOD_STANDARD);
        updateAll();
    }

    /**
     * Evento richiamato quando si usa la rotellina del mouse nell'anteprima dell'editor.
     * Tale funzione aggiunge un valore di zoom alla visualizzazione dell'anteprima, che si sommerà alla grandezza di
     * ogni sprite
     *
     * @param wheelRotation direzione verso la quale si è girata la rotellina, indicherà se aggiungere o togliere zoom
     * @param scrollType    indicherà il tipo di scorrimento della rotellina, legato a MouseWheelEvent
     * @param scrollAmount  valore del quale si è girata la rotellina, indicherà il valore di zoom da aggiungere
     */
    private void ridimensionaAnteprimaAction(int wheelRotation, int scrollType, int scrollAmount) {
        if (scrollType == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            editor.addZoomAnteprima(wheelRotation < 0 ? scrollAmount : -scrollAmount);
            updateAll();
        }
    }

    /**
     * Evento richiamato per permettere lo spostamento della parte della mappa attualmente visualizzabile nell'anteprima
     *
     * @param direzione direzione in cui verrà spostata la visualizzazione
     */
    private void muoviAnteprimaAction(int direzione) {
        int spostamento = Settings.SPOSTAMENTO_ANTEPRIMA;
        switch (direzione) {
            case KeyEvent.VK_RIGHT:
                int xMax = editor.getDimCellaAnteprima() * editor.getNumCelleAsse() - editor.getLunghezzaAnteprima();
                if (editor.addxAnteprima(spostamento) > xMax)
                    editor.setxAnteprima(xMax);
                break;
            case KeyEvent.VK_LEFT:
                if (editor.addxAnteprima(-spostamento) < 0)
                    editor.setxAnteprima(0);
                break;
            case KeyEvent.VK_UP:
                if (editor.addyAnteprima(-spostamento) < 0)
                    editor.setyAnteprima(0);
                break;
            case KeyEvent.VK_DOWN:
                int yMax = editor.getDimCellaAnteprima() * editor.getNumCelleAsse() - editor.getAltezzaAnteprima();
                if (editor.addyAnteprima(spostamento) > yMax)
                    editor.setyAnteprima(yMax);
                break;
        }
        updateAll();
    }
}