package src.controller.editor;

import src.model.editor.Cella;
import src.model.editor.Editor;
import src.model.editor.SpriteEditor;
import src.utils.Settings;
import src.utils.Utils;
import src.view.editor.EditorSidebarSinistraView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

/**
 * Controller della sidebar sinistra dell'editor, ossia delle informazioni generali della mappa.
 * Gestisce la lista degli sprites e la modifica delle impostazioni quali grandezza blocchi e dimensione mappa
 */
public class EditorSidebarSinistraController extends AbstractEditorController {

    private static EditorSidebarSinistraController instance = null;

    private EditorSidebarSinistraController() {
        super();

        view = EditorSidebarSinistraView.getInstance();
        EditorSidebarSinistraView sidebarSinistraView = (EditorSidebarSinistraView) view;

        sidebarSinistraView.getModificaMappaButton().addActionListener(e ->
                modificaMappaAction(sidebarSinistraView.getNome(), sidebarSinistraView.getDimCella(),
                        sidebarSinistraView.getLarghezza()));
        sidebarSinistraView.getModificaPuntoInizioButton().addActionListener(e ->
                cambiaModalitaAction(Editor.MOD_PUNTO_INIZIO));
        sidebarSinistraView.getModificaPuntoFineButton().addActionListener(e ->
                cambiaModalitaAction(Editor.MOD_PUNTO_FINE));
        sidebarSinistraView.getCercaImmagineButton().addActionListener(e ->
                visualizzaFinestraCercaImmagineAction(Editor.IMMAGINE_BACKGROUND));
        sidebarSinistraView.getModificaImmagineInizioButton().addActionListener(e ->
                visualizzaFinestraCercaImmagineAction(Editor.IMMAGINE_INIZIO_LIVELLO));
        sidebarSinistraView.getModificaImmagineFineButton().addActionListener(e ->
                visualizzaFinestraCercaImmagineAction(Editor.IMMAGINE_FINE_LIVELLO));
        sidebarSinistraView.getCercaImmagine().addActionListener(e ->
                cercaImmagineAction(sidebarSinistraView.getCercaImmagine().getName(),
                        sidebarSinistraView.getCercaImmagine().getSelectedFiles()));
        sidebarSinistraView.getLista().addListSelectionListener(e ->
                selezionaSpriteAction(sidebarSinistraView.getSpriteSelezionato(), e.getValueIsAdjusting()));
    }

    public static EditorSidebarSinistraController getInstance() {
        if (instance == null)
            instance = new EditorSidebarSinistraController();
        return instance;
    }

    /**
     * modifica le proprietà della mappa e dell'editor in base ai dati inseriti negli opportuni campi di testo,
     * se alcuni di questi sono vuoti vengono mantenuti i dati precedenti,
     * la modifica non viene effettuata se il nuovo nome della mappa è uguale a quello di un'altra mappa
     * <p>
     * queste nuove proprietà vengono modificate anche nel database,
     * insieme ai dati sulla cella di inizio e sulla cella di fine
     * (che non vengono direttamente modificati tramite campi di testo)
     * <p>
     * se il nome della mappa viene modificato, viene modificato di conseguenza il nome dell'immagine
     * <p>
     * alla fine viene aggiornata la view dell'editor
     *
     * @param nomeText      nuovo nome della mappa
     * @param dimCellaText  nuova dimensione della cella
     * @param larghezzaText nuova larghezza della mappa (numero di celle asse X)
     */
    private void modificaMappaAction(String nomeText, String dimCellaText, String larghezzaText) {
        String nomeVecchio = editor.getMappa().getNome();
        String s;
        String nome = ((s = nomeText).isEmpty() ? editor.getMappa().getNome() : s);

        if (!nome.equals(editor.getMappa().getNome()) && editorDAO.exists(editor.getMappa().getGioco().getNome(), nome))
            ((EditorSidebarSinistraView) view).messaggioErrore(Utils.getText("error_map_name_already_existing_title"));
        else {
            int dimCella = ((s = dimCellaText).isEmpty() ? editor.getDimCella() : Integer.parseInt(s));
            int dim = ((s = larghezzaText).isEmpty() ? editor.getNumCelleAsse() : Integer.parseInt(s));

            int cellaInizioX = editor.getCellaInizio().getX();
            int cellaInizioY = editor.getCellaInizio().getY();
            int cellaFineX = editor.getCellaFine().getX();
            int cellaFineY = editor.getCellaFine().getY();

            editor.getMappa().setNome(nome);
            editor.setDimCella(dimCella);
            if (!editor.setNumCelleAsse(dim))
                dim = editor.getNumCelleAsse();

            editorDAO.salvaMappa(editor.getMappa(), dimCella, dim,
                    cellaInizioX, cellaInizioY, cellaFineX, cellaFineY);
            if (!nomeVecchio.equals(editor.getMappa().getNome()))
                Utils.rinominaImmagine(Settings.CARTELLA_MAPPE, nomeVecchio, editor.getMappa().getNome());

            updateAll();
        }
    }

    /**
     * apre la finestra che permette la scelta di un'immagine da file
     *
     * @param nome definisce il tipo di immagine che si sceglie
     *             (mappa, punto di inizio livello, punto di fine livello)
     */
    private void visualizzaFinestraCercaImmagineAction(String nome) {
        ((EditorSidebarSinistraView) view).cercaImmagine(nome);
    }

    private void cercaImmagineAction(String nome, File[] files) {
        if (files != null) {
            try {
                if (nome.equals(Editor.IMMAGINE_BACKGROUND)) {
                    if (files.length > 0 && !files[0].getPath().isEmpty()) {
                        Utils.copiaImmagine(files[0].getPath(), Settings.CARTELLA_MAPPE, editor.getMappa().getNome());
                        Image immagine = ImageIO.read(files[0]);
                        editor.setImmagine(immagine);
                        for (int i = 1; i < files.length; i++)
                            Utils.copiaImmagine(files[i].getPath(), Settings.CARTELLA_MAPPE, editor.getMappa().getNome() + "_" + i);
                    }
                } else if (files.length > 0 && !files[0].getPath().isEmpty()) {
                    Utils.copiaImmagine(files[0].getPath(), Settings.CARTELLA_SPRITES, files[0].getName().split("\\.")[0]);
                    if (nome.equals(Editor.IMMAGINE_INIZIO_LIVELLO))
                        editor.setNomeImmagineInizio(files[0].getName().split("\\.")[0]);
                    else if (nome.equals(Editor.IMMAGINE_FINE_LIVELLO))
                        editor.setNomeImmagineFine(files[0].getName().split("\\.")[0]);
                    for (int i = 1; i < files.length; i++)
                        Utils.copiaImmagine(files[i].getPath(), Settings.CARTELLA_SPRITES, files[i].getName().split("\\.")[0] + "_" + i);
                }
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Cambia lo sprite attualmente selezionato con quello cliccato sulla lista
     *
     * @param spriteEditor lo sprite da selezionare
     * @param isAdjusting  valore passato dal listener della lista
     */
    private void selezionaSpriteAction(SpriteEditor spriteEditor, boolean isAdjusting) {
        if (spriteEditor != null && !isAdjusting) {
            for (Cella cella : editor.getListaCelle())
                if (cella.getContenuto() != null && cella.getContenuto().equals(spriteEditor)) {
                    editor.setCellaSelezionata(cella, true);
                    break;
                }
        }
        updateAll();
    }
}