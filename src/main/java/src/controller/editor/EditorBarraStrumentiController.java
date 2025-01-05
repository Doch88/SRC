package src.controller.editor;

import src.controller.menu.MenuEditorMappeController;
import src.controller.piattaforma.PiattaformaController;
import src.model.editor.Editor;
import src.model.editor.SpriteEditor;
import src.view.editor.EditorBarraStrumentiView;
import src.view.editor.FrameCreaSpriteView;
import src.view.editor.FrameImportaSpriteView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controller che gestisce la barra strumenti e come essa dovrà funzionare.
 */
public class EditorBarraStrumentiController extends AbstractEditorController {

    private static EditorBarraStrumentiController instance = null;

    private final FrameCreaSpriteView creaSpriteView;
    private final FrameImportaSpriteView importaSpriteView;

    private final PiattaformaController piattaformaController = PiattaformaController.getInstance();

    private EditorBarraStrumentiController() {
        super();

        view = EditorBarraStrumentiView.getInstance();
        EditorBarraStrumentiView barraStrumentiView = (EditorBarraStrumentiView) view;

        creaSpriteView = FrameCreaSpriteView.getInstance();
        importaSpriteView = FrameImportaSpriteView.getInstance();

        barraStrumentiView.getPosizionaButton().addActionListener(e -> cambiaModalitaAction(Editor.MOD_POSIZIONAMENTO));
        barraStrumentiView.getPosizionaButton().setMnemonic(Editor.KEY_POSIZIONA);
        barraStrumentiView.getDuplicaButton().addActionListener(e -> duplicaAction());
        barraStrumentiView.getDuplicaButton().setMnemonic(Editor.KEY_DUPLICA);
        barraStrumentiView.getCreaButton().addActionListener(e -> visualizzaFinestraCreaSpriteAction());
        barraStrumentiView.getCreaButton().setMnemonic(Editor.KEY_CREA);
        barraStrumentiView.getImportaButton().addActionListener(e -> visualizzaFinestraImportaSpriteAction());
        barraStrumentiView.getImportaButton().setMnemonic(Editor.KEY_IMPORTA);
        barraStrumentiView.getEliminaButton().addActionListener(e -> cambiaModalitaAction(Editor.MOD_ELIMINAZIONE));
        barraStrumentiView.getEliminaButton().setMnemonic(Editor.KEY_ELIMINA);
        barraStrumentiView.getAvviaMappaButton().addActionListener(e -> avviaMappa());
        barraStrumentiView.getAvviaMappaButton().setMnemonic(Editor.KEY_AVVIA);
        barraStrumentiView.getIndietroButton().addActionListener(e -> indietroAction());
        barraStrumentiView.getIndietroButton().setMnemonic(Editor.KEY_INDIETRO);

        barraStrumentiView.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    indietroAction();
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        creaSpriteView.getConfermaBottone().addActionListener(e ->
                creaSpriteConfermaAction(creaSpriteView.getNome(), creaSpriteView.getGroupSelezionato().getActionCommand()));

        importaSpriteView.getConferma().addActionListener(e ->
                importaSpriteConfermaAction(importaSpriteView.getNomeSpriteSelezionato()));
        importaSpriteView.getElimina().addActionListener(e ->
                importaSpriteEliminaAction(importaSpriteView.getNomeSpriteSelezionato()));
    }

    /**
     * si entra in modalità creazione sprite, viene visualizzata
     * la finestra che permette di creare un nuovo sprite
     */
    private void visualizzaFinestraCreaSpriteAction() {
        cambiaModalitaAction(Editor.MOD_CREAZIONE);
        creaSpriteView.init();
    }

    /**
     * permette la creazione di un nuovo sprite e il suo inserimento nel database
     * di un nome e un tipo scelti nella finestra di creazione sprite
     * <p>
     * se nel database esiste già uno sprite con il nome inviato come parametro,
     * viene visualizzato un messaggio di errore,
     * altrimenti viene creato un nuovo sprite nel database con impostazioni di default,
     * quindi lo sprite appena creato diventa lo sprite selezionato e le sue informazioni
     * sono visualizzabili e modificabili dalla sidebar destra
     * <p>
     * in ogni caso alla fine si torna in modalità standard e viene chiusa la finestra di creazione sprite
     *
     * @param nomeSprite nome dello sprite da creare
     * @param tipoSprite tipo dello sprite da creare (NPC, blocco...)
     */
    private void creaSpriteConfermaAction(String nomeSprite, String tipoSprite) {
        if (spriteEditorDAO.exists(nomeSprite))
            ((EditorBarraStrumentiView) view).messaggioErroreSpriteEsistente();
        else {
            SpriteEditor sprite = new SpriteEditor(nomeSprite, tipoSprite);
            spriteEditorDAO.add(sprite);
            editor.setSpriteSelezionato(sprite);
        }

        editor.setModStandard();
        creaSpriteView.setVisible(false);
        updateAll();
    }

    /**
     * si entra in modalità importazione sprite, viene visualizzata
     * la finestra che permette di importare uno sprite tra quelli presenti nel database
     */
    private void visualizzaFinestraImportaSpriteAction() {
        cambiaModalitaAction(Editor.MOD_IMPORTAZIONE);
        importaSpriteView.init();
    }

    /**
     * Azione di duplicazione dello sprite attualmente selezionato.
     * Verrà quindi creata una nuova istanza uguale allo sprite selezionato e verrà selezionata
     */
    private void duplicaAction() {
        if (editor.getSpriteSelezionato() != null) {
            editor.duplicaSpriteSelezionato();
            editor.setModalitaAttuale(Editor.MOD_POSIZIONAMENTO);
        }
    }

    /**
     * permette di importare uno sprite dal database con il nome selezionato
     * nella finestra di importazione sprite e le informazioni corrispondenti
     * per visualizzarle nella sidebar destra ed eventualmente modificarle,
     * quindi lo sprite in questione diventa lo sprite selezionato
     * <p>
     * alla fine si torna in modalità standard e viene chiusa la finestra di creazione sprite
     *
     * @param sprite nome (univoco) dello sprite da importare
     */
    private void importaSpriteConfermaAction(String sprite) {
        editor.setSpriteSelezionato(new SpriteEditor(sprite));
        editor.setModStandard();
        importaSpriteView.setVisible(false);
        updateAll();
    }

    /**
     * Permette di eliminare uno sprite dalla finestra di importaSprite
     *
     * @param sprite nome dello sprite da eliminare
     */
    private void importaSpriteEliminaAction(String sprite) {
        editorDAO.rimuoviSprite(new SpriteEditor(sprite));
        editor.setModStandard();
        editor.eliminaSpriteUguali(sprite);
        importaSpriteView.setVisible(false);
        updateAll();
    }

    /**
     * permette di avviare la piattaforma (senza salvataggi) per testare la mappa,
     * si entra quindi in modalità test
     */
    private void avviaMappa() {
        cambiaModalitaAction(Editor.MOD_CHIUSURA_EDITOR);
        piattaformaController.initTest(editor.getMappa());
    }

    /**
     * Ritorna al menù dei giochi
     */
    private void indietroAction() {
        editor.setModalitaAttuale(Editor.MOD_CHIUSURA_EDITOR);
        MenuEditorMappeController.getInstance().init(editor.getMappa().getGioco());
    }

    public static EditorBarraStrumentiController getInstance() {
        if (instance == null)
            instance = new EditorBarraStrumentiController();
        return instance;
    }
}