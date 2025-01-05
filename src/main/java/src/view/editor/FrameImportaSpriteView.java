package src.view.editor;

import src.model.DAO.EditorDAO;
import src.model.editor.SpriteEditor;
import src.utils.Utils;

import javax.swing.*;

/**
 * Frame che permette di importare uno sprite dal database
 */
public class FrameImportaSpriteView extends JFrame {

    private static FrameImportaSpriteView instance = null;

    private final JPanel mainPanel;
    private final JList<String> listaSprites;
    private final DefaultListModel<String> listaElemSprites;
    private final JButton conferma;
    private final JButton elimina;

    public FrameImportaSpriteView() {
        super();

        this.setName(Utils.getText("import_sprite"));
        this.setSize(300, 800);
        this.setResizable(false);

        mainPanel = new JPanel();

        listaElemSprites = new DefaultListModel<>();
        listaSprites = new JList<>(listaElemSprites);
        listaSprites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.updateListaSprites();

        JPanel buttonArea = new JPanel();
        conferma = new JButton(Utils.getText("confirm_button"));
        elimina = new JButton(Utils.getText("delete_button"));

        buttonArea.setLayout(new BoxLayout(buttonArea, BoxLayout.Y_AXIS));
        mainPanel.add(listaSprites);
        buttonArea.add(conferma);
        buttonArea.add(elimina);
        mainPanel.add(buttonArea);
        this.add(mainPanel);

        this.pack();
    }

    public static FrameImportaSpriteView getInstance() {
        if (instance == null)
            instance = new FrameImportaSpriteView();
        return instance;
    }

    public void init() {
        updateListaSprites();
        this.setVisible(true);
        this.repaint();
    }

    public String getNomeSpriteSelezionato() {
        return listaSprites.getSelectedValue();
    }

    public JButton getConferma() {
        return conferma;
    }

    public JButton getElimina() {
        return elimina;
    }

    public void updateListaSprites() {
        listaElemSprites.removeAllElements();
        for (SpriteEditor spr : EditorDAO.getInstance().getListaSprites())
            listaElemSprites.addElement(spr.getNome());
    }

}
