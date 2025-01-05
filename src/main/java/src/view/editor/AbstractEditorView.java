package src.view.editor;

import src.model.editor.Editor;

import javax.swing.*;

/**
 * Classe astratta che definisce lo scheletro base di una view dell'editor.
 */
public abstract class AbstractEditorView extends JPanel {

    protected Editor editor;

    public AbstractEditorView() {
        editor = Editor.getInstance();

    }

    /**
     * Metodo eseguito per inizializzare la view
     */
    public abstract void init();

    /**
     * Metodo che permette di tenere aggiornato il contenuto della view ad ogni modifica
     */
    public abstract void update();
}
