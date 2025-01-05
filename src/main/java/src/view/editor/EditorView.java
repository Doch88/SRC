package src.view.editor;

import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * View generale dell'editor, contenente tutte le altre view e posizionandole in maniera opportuna.
 */
public class EditorView extends JPanel {

    private static EditorView instance = null;

    private final EditorSidebarSinistraView sidebarSinistra;
    private final EditorSidebarDestraView sidebarDestra;
    private final EditorAnteprimaView anteprima;
    private final EditorBarraStrumentiView barraStrumenti;

    private EditorView() {
        super();
        this.setLayout(new BorderLayout());

        sidebarSinistra = EditorSidebarSinistraView.getInstance();
        sidebarDestra = EditorSidebarDestraView.getInstance();
        anteprima = EditorAnteprimaView.getInstance();
        barraStrumenti = EditorBarraStrumentiView.getInstance();

        Dimension dimAnteprima = new Dimension(this.getWidth() / 2, this.getHeight() - 100);
        anteprima.setPreferredSize(dimAnteprima);
        anteprima.setMaximumSize(dimAnteprima);

        this.add(sidebarSinistra, BorderLayout.WEST);
        this.add(sidebarDestra, BorderLayout.EAST);
        this.add(anteprima, BorderLayout.CENTER);
        this.add(barraStrumenti, BorderLayout.SOUTH);
    }

    public static EditorView getInstance() {
        if (instance == null)
            instance = new EditorView();
        return instance;
    }

    public void init() {
        sidebarSinistra.init();
        sidebarDestra.init();
        barraStrumenti.init();
        anteprima.init();

        updateView();
        Utils.updateView(this);
    }

    public void updateView() {
        sidebarDestra.update();
        sidebarSinistra.update();
        barraStrumenti.update();
        anteprima.requestFocusInWindow();
        anteprima.update();
    }

}