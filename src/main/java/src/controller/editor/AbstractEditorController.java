package src.controller.editor;

import src.model.DAO.EditorDAO;
import src.model.DAO.SpriteEditorDAO;
import src.model.editor.Editor;
import src.view.editor.AbstractEditorView;
import src.view.editor.EditorView;

/**
 * Questa classe astratta viene estesa dai controller delle varie sezioni dell'editor
 * (sidebar destra, sidebar sinistra, barra degli strumenti e anteprima),
 * in quanto contiene i riferimenti alle istanze dei model (editor, editorDAO, spriteEditorDAO)
 * e delle view (la view completa dell'editor e la view parziale
 * della sezione gestita dal controller che estende questa classe)
 */
public abstract class AbstractEditorController {

    protected Editor editor = Editor.getInstance();
    protected EditorDAO editorDAO = EditorDAO.getInstance();
    protected SpriteEditorDAO spriteEditorDAO = SpriteEditorDAO.getInstance();

    private final EditorView editorView = EditorView.getInstance();
    /**
     * view specifica della componente che estende questa class
     * (ad esempio, per EditorBarraStrumentiController questo attributo
     * si riferirà all'istanza di EditorBarraStrumentiView)
     */
    protected AbstractEditorView view;

    /**
     * inizializza la view della sezione dell'editor gestita dal controller
     */
    public void init() {
        view.init();
    }

    /**
     * init la view della sezione dell'editor gestita dal controller
     */
    protected void updateView() {
        view.update();
    }

    /**
     * init la view completa dell'editor
     * PRE la view dell'editor (editorView) è già stata inizializzata in EditorController
     */
    protected void updateAll() {
        editorView.updateView();
    }

    /**
     * cambia la modalità attuale che gestisce le operazioni effettuate nell'editor
     *
     * @param modalita modalità da sostituire a quella attuale
     */
    protected void cambiaModalitaAction(int modalita) {
        editor.setModalitaAttuale(modalita);
        updateAll();
    }

}
