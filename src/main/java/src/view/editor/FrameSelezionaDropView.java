package src.view.editor;

import src.model.DAO.EditorDAO;
import src.model.editor.Editor;
import src.model.editor.SpriteEditor;
import src.utils.GenericButton;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Frame che permette di selezionare i drop di uno sprite.
 * E' diviso in una parte superiore ed una inferiore. La parte superiore consiste nella lista di tutti gli sprite, la parte inferiore
 * è la lista degli sprites specifici presenti nel drop dell'oggetto
 */
public class FrameSelezionaDropView extends JFrame {

    private static FrameSelezionaDropView instance = null;

    private final Editor editor;
    private final EditorDAO editorDAO;

    private List<String> spritesSelezionati;
    private List<String> spritesDroppatiSelezionati;

    private JPanel mainPanel = new JPanel();
    private GenericButton inserisciDrop;
    private GenericButton eliminaDrop;

    public FrameSelezionaDropView() {
        super();

        editor = Editor.getInstance();
        editorDAO = EditorDAO.getInstance();

        this.setName(Utils.getText("drop_sprite"));
        this.setSize(300, 800);
        this.setResizable(false);
        mainPanel = new JPanel();
    }

    public static FrameSelezionaDropView getInstance() {
        if (instance == null)
            instance = new FrameSelezionaDropView();
        return instance;
    }

    public void init(SpriteEditor spriteEditor) {
        mainPanel.removeAll();
        GridLayout layout = new GridLayout(2, 2);
        layout.setVgap(5);

        mainPanel.setLayout(layout);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (SpriteEditor spr : editorDAO.getListaSprites())
            if (spr.getDatiSprite().get("tipo").equals("collectable") ||
                    spr.getDatiSprite().get("tipo").equals("health") ||
                    spr.getDatiSprite().get("tipo").equals("bullets"))
                listModel.addElement(spr.getNome());
        JList<String> lista = new JList<>(listModel);
        lista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lista.addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting())
                spritesSelezionati = lista.getSelectedValuesList();
        });

        mainPanel.add(lista);
        JPanel panelButton = new JPanel();

        inserisciDrop = new GenericButton("insert_button");

        inserisciDrop.addActionListener(e -> {
            for (String nome : spritesSelezionati)
                editorDAO.aggiungiDropOggetto(spriteEditor, nome);
            spritesSelezionati.clear();
        });
        panelButton.add(inserisciDrop);
        mainPanel.add(panelButton);
        DefaultListModel<String> listDropModel = new DefaultListModel<>();
        for (SpriteEditor spr : editorDAO.getDropOggetti(spriteEditor))
            listDropModel.addElement(spr.getNome());
        JList<String> listaDrop = new JList<>(listDropModel);
        listaDrop.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaDrop.addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting())
                spritesDroppatiSelezionati = listaDrop.getSelectedValuesList();
        });
        mainPanel.add(listaDrop);

        JPanel panelButton2 = new JPanel();
        eliminaDrop = new GenericButton("delete_button");
        eliminaDrop.addActionListener(e -> {
            for (String nome : spritesDroppatiSelezionati)
                editorDAO.eliminaDropOggetto(spriteEditor, nome);
            spritesDroppatiSelezionati.clear();
        });
        panelButton2.add(eliminaDrop);
        mainPanel.add(panelButton2);


        this.add(mainPanel);

        this.pack();
        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }

    public GenericButton getInserisciDrop() {
        return inserisciDrop;
    }

    public GenericButton getEliminaDrop() {
        return eliminaDrop;
    }

}
