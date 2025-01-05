package src.view.editor;

import src.model.editor.Cella;
import src.model.editor.SpriteEditor;
import src.utils.Settings;
import src.utils.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * View della sidebar destra contenente le proprietà di un determinato sprite.
 */
public class EditorSidebarDestraView extends AbstractEditorView {

    private static EditorSidebarDestraView instance;

    private final JPanel top;
    private final JPanel bottom;

    private final JButton modificaProprietaButton;
    private final JButton inserisciInMappaButton;
    private final JButton waypoint1Button;
    private final JButton waypoint2Button;
    private final JButton associaButton;
    private final JButton inserisciAnimazioneStaticaButton;
    private final JButton inserisciAnimazioneMovimentoButton;
    private final JButton inserisciAnimazioneSaltoButton;
    private final JButton inserisciAnimazioneSparoButton;
    private final JButton inserisciAnimazioneColpitoButton;
    private final JButton inserisciOggettiDroppatiButton;
    private final JButton eliminaSpriteDatabaseButton;

    private final JFileChooser cercaImmagine;
    private final ArrayList<File> sortedSelectedFiles = new ArrayList<>();

    private HashMap<String, JTextField> textFields;

    private EditorSidebarDestraView() {
        super();

        this.setMaximumSize(new Dimension(300, this.getHeight()));
        this.setPreferredSize(new Dimension(300, this.getHeight()));
        this.setMinimumSize(new Dimension(300, this.getHeight()));
        this.setLayout(new GridLayout(2, 1));

        top = new JPanel();
        bottom = new JPanel();

        cercaImmagine = new JFileChooser();
        cercaImmagine.setMultiSelectionEnabled(true);
        cercaImmagine.addChoosableFileFilter(new FileNameExtensionFilter(Utils.getText("image_filter_desc"), "png", "jpg", "jpeg", "gif"));
        cercaImmagine.addPropertyChangeListener(
                JFileChooser.SELECTED_FILES_CHANGED_PROPERTY,
                e -> {
                    java.util.List<File> selected = Arrays.asList(cercaImmagine.getSelectedFiles());
                    Iterator<File> it = sortedSelectedFiles.iterator();

                    while (it.hasNext())
                        if (!selected.contains(it.next()))
                            it.remove();

                    for (File file : selected)
                        if (!sortedSelectedFiles.contains(file))
                            sortedSelectedFiles.add(file);
                });

        modificaProprietaButton = new JButton(Utils.getText("apply_changes"));
        inserisciInMappaButton = new JButton(Utils.getText("insert_button"));
        waypoint1Button = new JButton(Utils.getText("set_waypoint_1"));
        waypoint2Button = new JButton(Utils.getText("set_waypoint_2"));
        associaButton = new JButton(Utils.getText("pair_portal"));
        eliminaSpriteDatabaseButton = new JButton(Utils.getText("delete_from_db"));

        inserisciAnimazioneStaticaButton = new JButton(Utils.getText("insert_button"));
        inserisciAnimazioneStaticaButton.setName("static");
        inserisciAnimazioneMovimentoButton = new JButton(Utils.getText("insert_button"));
        inserisciAnimazioneMovimentoButton.setName("movimento");
        inserisciAnimazioneSaltoButton = new JButton(Utils.getText("insert_button"));
        inserisciAnimazioneSaltoButton.setName("salto");
        inserisciAnimazioneSparoButton = new JButton(Utils.getText("insert_button"));
        inserisciAnimazioneSparoButton.setName("sparo");
        inserisciAnimazioneColpitoButton = new JButton(Utils.getText("insert_button"));
        inserisciAnimazioneColpitoButton.setName("colpito");
        inserisciOggettiDroppatiButton = new JButton("Gestisci");

        top.setBackground(Color.WHITE);
        bottom.setBackground(Color.WHITE);
        this.setBackground(Color.WHITE);

        this.add(top);
        this.add(bottom);
    }

    public static EditorSidebarDestraView getInstance() {
        if (instance == null)
            instance = new EditorSidebarDestraView();
        return instance;
    }

    @Override
    public void init() {
        update();
    }

    @Override
    public void update() {
        top.removeAll();

        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(Settings.SCROLLPANE_VELOCITA);
        textFields = new HashMap<>();
        JPanel topFields = new JPanel();

        topFields.setLayout(new GridLayout(0, 2));
        topFields.setBackground(Color.WHITE);

        Cella cella = editor.getCellaSelezionata();

        if (cella != null) {
            topFields.add(new JLabel(Utils.getText("cell_position")));
            topFields.add(new JLabel("(" + cella.getX() + ", " + cella.getY() + ")"));
        }

        SpriteEditor spriteEditor = editor.getSpriteSelezionato();

        if (spriteEditor == null) {
            editor.setSpriteSelezionato(editor.getPlayer());
            spriteEditor = editor.getPlayer();
        } else {
            JLabel label = new JLabel("instance_id: ");
            JTextField field = new JTextField("");
            if (spriteEditor.getIdCollocazione() > -1)
                field.setText("" + spriteEditor.getIdCollocazione());
            field.setEnabled(false);
            topFields.add(label);
            topFields.add(field);
        }

        for (String key : spriteEditor.getNomeAttributi()) {
            JLabel label = new JLabel(key + ": ");
            JTextField field = new JTextField("");
            if (spriteEditor.getDatiSprite().get(key) != null)
                field.setText(spriteEditor.getDatiSprite().get(key).toString());
            textFields.put(key, field);
            topFields.add(label);
            topFields.add(field);
        }
        /*
         * Inserimento immagini animazioni statiche, di movimento, di salto e di danno subito
         */

        topFields.add(new JLabel(Utils.getText("static_animation")));
        topFields.add(inserisciAnimazioneStaticaButton);

        topFields.add(new JLabel(Utils.getText("movement_animation")));
        topFields.add(inserisciAnimazioneMovimentoButton);

        if (spriteEditor.getTipoSprite().equals("player")) {
            topFields.add(new JLabel(Utils.getText("jump_animation")));
            topFields.add(inserisciAnimazioneSaltoButton);

            topFields.add(new JLabel(Utils.getText("fire_animation")));
            topFields.add(inserisciAnimazioneSparoButton);
        }
        if (spriteEditor.getTipoSprite().equals("player") || spriteEditor.getTipoSprite().equals("npc")) {
            topFields.add(new JLabel(Utils.getText("damaged_animation")));
            topFields.add(inserisciAnimazioneColpitoButton);
        }
        if (spriteEditor.getTipoSprite().equals("npc") || spriteEditor.getTipoSprite().equals("block")) {
            topFields.add(new JLabel(Utils.getText("dropped_items")));
            topFields.add(inserisciOggettiDroppatiButton);
        }

        topFields.add(modificaProprietaButton);
        topFields.add(eliminaSpriteDatabaseButton);
        scrollPane.setViewportView(topFields);
        scrollPane.getVerticalScrollBar().setValue(0);
        top.add(scrollPane);
        scrollPane.setPreferredSize(top.getSize());
        scrollPane.setMaximumSize(top.getSize());
        scrollPane.setMinimumSize(top.getSize());
        top.revalidate();
        top.repaint();

        bottom.removeAll();

        JPanel bottomFields = new JPanel();

        bottomFields.setLayout(new GridLayout(0, 2));
        bottomFields.setBackground(Color.WHITE);

        if (!spriteEditor.isInMappa() && !(spriteEditor.getTipoSprite().equals("player"))) {
            bottomFields.add(inserisciInMappaButton);
            bottomFields.add(new JLabel(Utils.getText("first_cell_insert")));
        }
        if (spriteEditor.isInMappa() && (spriteEditor.getTipoSprite().equals("npc") || spriteEditor.getTipoSprite().equals("block"))) {
            JLabel coordinateW1Label = spriteEditor.getWaypoint1() != null ?
                    new JLabel(" X: " + spriteEditor.getWaypoint1().getX()) : new JLabel(" " + Utils.getText("not_defined"));
            JLabel coordinateW2Label = spriteEditor.getWaypoint2() != null ?
                    new JLabel(" X: " + spriteEditor.getWaypoint2().getX()) : new JLabel(" " + Utils.getText("not_defined"));

            bottomFields.add(waypoint1Button);
            bottomFields.add(coordinateW1Label);
            bottomFields.add(waypoint2Button);
            bottomFields.add(coordinateW2Label);
        }
        if (spriteEditor.isInMappa() && (spriteEditor.getTipoSprite().equals("portal"))) {
            JLabel associatoLabel;
            if (spriteEditor.getDatiCollocazione().get("associato") != null)
                associatoLabel = new JLabel(String.valueOf((int) spriteEditor.getDatiCollocazione().get("associato")));
            else
                associatoLabel = new JLabel(Utils.getText("no_paired"));
            bottomFields.add(associaButton);
            bottomFields.add(associatoLabel);
        }
        bottom.add(bottomFields);

        bottom.revalidate();
        bottom.repaint();
    }

    public void cercaImmagine(String nome) {
        cercaImmagine.setName(nome);
        cercaImmagine.showOpenDialog(this);
    }

    public JButton getModificaProprietaButton() {
        return modificaProprietaButton;
    }

    public JButton getInserisciInMappaButton() {
        return inserisciInMappaButton;
    }

    public JButton getWaypoint1Button() {
        return waypoint1Button;
    }

    public JButton getWaypoint2Button() {
        return waypoint2Button;
    }

    public JButton getAssociaButton() {
        return associaButton;
    }

    public JButton getEliminaSpriteDatabaseButton() {
        return eliminaSpriteDatabaseButton;
    }

    public JButton getInserisciAnimazioneStaticaButton() {
        return inserisciAnimazioneStaticaButton;
    }

    public JButton getInserisciAnimazioneMovimentoButton() {
        return inserisciAnimazioneMovimentoButton;
    }

    public JButton getInserisciAnimazioneSaltoButton() {
        return inserisciAnimazioneSaltoButton;
    }

    public JButton getInserisciAnimazioneSparoButton() {
        return inserisciAnimazioneSparoButton;
    }

    public JButton getInserisciAnimazioneColpitoButton() {
        return inserisciAnimazioneColpitoButton;
    }

    public JButton getInserisciOggettiDroppatiButton() {
        return inserisciOggettiDroppatiButton;
    }

    public JFileChooser getCercaImmagine() {
        return cercaImmagine;
    }

    public ArrayList<File> getSortedSelectedFiles() {
        return sortedSelectedFiles;
    }

    public HashMap<String, JTextField> getTextFields() {
        return textFields;
    }
}