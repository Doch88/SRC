package src.controller.editor;

import src.model.Mappa;
import src.model.editor.Editor;
import src.view.editor.EditorView;

/**
 * Controller generale dell'editor, esso fornisce un collegamento a tutti gli altri controller e inizializza la mappa.
 */
public class EditorController {

    private static EditorController instance = null;

    private final Editor editor = Editor.getInstance();

    private final EditorView editorView = EditorView.getInstance();

    private final EditorAnteprimaController editorAnteprimaController = EditorAnteprimaController.getInstance();
    private final EditorSidebarSinistraController editorSidebarSinistraController = EditorSidebarSinistraController.getInstance();
    private final EditorSidebarDestraController editorSidebarDestraController = EditorSidebarDestraController.getInstance();
    private final EditorBarraStrumentiController editorBarraStrumentiController = EditorBarraStrumentiController.getInstance();

    public static EditorController getInstance() {
        if (instance == null)
            instance = new EditorController();
        return instance;
    }

    /**
     * Carica la mappa, imposta la modalità standard e visualizza l'editor
     * con la sidebar sinistra, la sidebar destra, la barra inferiore e l'anteprima della mappa
     */
    public void init(Mappa mappa) {
        editor.init(mappa);
        editor.setModStandard();

        editorAnteprimaController.init();
        editorSidebarSinistraController.init();
        editorSidebarDestraController.init();
        editorBarraStrumentiController.init();

        editorView.init();
        editorView.updateView();
    }

}