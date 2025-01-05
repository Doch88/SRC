package src.view.editor;

import src.model.editor.SpriteEditor;
import src.utils.NumberTextField;
import src.utils.Settings;
import src.utils.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * View della sidebar sinistra dell'editor, contiene la lista degli sprites e le varie caselle per modificare le informazioni della mappa.
 */
public class EditorSidebarSinistraView extends AbstractEditorView {

    private static EditorSidebarSinistraView instance;

    private final JPanel top;
    private final JPanel bottom;

    private final JList<SpriteEditor> lista;
    private final DefaultListModel<SpriteEditor> listModel;

    private final JTextField nomeText;
    private final JTextField dimCellaText;
    private final JTextField numeroCelleText;
    private JLabel cellaInizioText;
    private JLabel cellaFineText;

    private final JFileChooser cercaImmagine;

    private final JButton modificaMappaButton;
    private final JButton cercaImmagineButton;

    private final JButton modificaPuntoInizioButton;
    private final JButton modificaImmagineInizioButton;
    private final JButton modificaPuntoFineButton;
    private final JButton modificaImmagineFineButton;

    private EditorSidebarSinistraView() {
        super();

        this.setMaximumSize(new Dimension(300, this.getHeight()));
        this.setPreferredSize(new Dimension(300, this.getHeight()));
        this.setMinimumSize(new Dimension(300, this.getHeight()));

        nomeText = new JTextField();
        dimCellaText = new NumberTextField(0);
        numeroCelleText = new NumberTextField(0);
        cellaInizioText = new JLabel();
        cellaFineText = new JLabel();

        cercaImmagine = new JFileChooser();
        cercaImmagine.setMultiSelectionEnabled(true);
        cercaImmagine.addChoosableFileFilter(new FileNameExtensionFilter(Utils.getText("image_filter_desc"), "png", "jpg", "jpeg", "gif"));

        modificaMappaButton = new JButton(Utils.getText("change_parameters"));
        cercaImmagineButton = new JButton(Utils.getText("find_image"));

        modificaPuntoInizioButton = new JButton(Utils.getText("initial_point"));
        modificaPuntoFineButton = new JButton(Utils.getText("end_point"));

        modificaImmagineInizioButton = new JButton(Utils.getText("starting_image_map"));
        modificaImmagineFineButton = new JButton(Utils.getText("end_image_map"));

        cercaImmagineButton.setName("background");

        top = new JPanel();
        bottom = new JPanel();

        top.setBackground(Color.WHITE);
        bottom.setBackground(Color.WHITE);

        this.setLayout(new GridLayout(2, 1));
        bottom.setLayout(new GridLayout(0, 1));

        this.add(top);
        this.add(bottom);

        listModel = new DefaultListModel<>();
        lista = new JList<>(listModel);
        lista.setCellRenderer(CellRenderer.getInstance());
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setVisibleRowCount(20);
        lista.setFixedCellWidth(250);
    }

    public static EditorSidebarSinistraView getInstance() {
        if (instance == null)
            instance = new EditorSidebarSinistraView();
        return instance;
    }

    @Override
    public void init() {
        nomeText.setText(editor.getMappa().getNome());
        dimCellaText.setText(String.valueOf(editor.getDimCella()));
        numeroCelleText.setText(String.valueOf(editor.getNumCelleAsse()));

        if (editor.getCellaInizio() != null)
            cellaInizioText.setText(editor.getCellaInizio().getX() + ", " + editor.getCellaInizio().getY());
        else
            cellaInizioText.setText(Utils.getText("not_set"));


        if (editor.getCellaFine() != null)
            cellaFineText.setText(editor.getCellaFine().getX() + ", " + editor.getCellaFine().getY());
        else
            cellaFineText.setText(Utils.getText("not_set"));

        listModel.removeAllElements();

        update();
    }

    @Override
    public void update() {
        top.removeAll();

        if (!listModel.contains(editor.getPlayer()))
            listModel.addElement(editor.getPlayer());

        for (SpriteEditor spriteEditor : editor.getListaSprites())
            if (!listModel.contains(spriteEditor)) {
                listModel.addElement(spriteEditor);
            }

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(lista);
        scrollPane.getVerticalScrollBar().setUnitIncrement(Settings.SCROLLPANE_VELOCITA);
        top.add(scrollPane);

        top.revalidate();
        top.repaint();


        bottom.removeAll();

        JPanel fields = new JPanel();
        fields.setBackground(Color.WHITE);
        fields.setLayout(new GridLayout(0, 2));

        if (editor.getCellaInizio() != null)
            cellaInizioText = new JLabel(editor.getCellaInizio().getX() + ", " + editor.getCellaInizio().getY());
        else
            cellaInizioText = new JLabel(Utils.getText("not_set"));

        if (editor.getCellaFine() != null)
            cellaFineText = new JLabel(editor.getCellaFine().getX() + ", " + editor.getCellaFine().getY());
        else
            cellaFineText = new JLabel(Utils.getText("not_set"));

        fields.add(new JLabel(Utils.getText("name_label") + ": "));
        fields.add(nomeText);
        fields.add(new JLabel(Utils.getText("cell_size") + ": "));
        fields.add(dimCellaText);
        fields.add(new JLabel(Utils.getText("num_cells") + ": "));
        fields.add(numeroCelleText);
        fields.add(modificaPuntoInizioButton);
        fields.add(cellaInizioText);
        fields.add(modificaPuntoFineButton);
        fields.add(cellaFineText);
        fields.add(modificaImmagineInizioButton);
        fields.add(modificaImmagineFineButton);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(modificaMappaButton);
        panel.add(cercaImmagineButton);

        bottom.add(fields);
        bottom.add(panel);

        bottom.revalidate();
        bottom.repaint();

    }

    public void messaggioErrore(String messaggio) {
        JOptionPane.showMessageDialog(this,
                messaggio,
                Utils.getText("error"), JOptionPane.ERROR_MESSAGE);
    }

    public void cercaImmagine(String nome) {
        cercaImmagine.setName(nome);
        cercaImmagine.showOpenDialog(this);
    }

    public SpriteEditor getSpriteSelezionato() {
        return lista.getSelectedValue();
    }

    public String getNome() {
        return nomeText.getText();
    }

    public String getDimCella() {
        return dimCellaText.getText();
    }

    public String getLarghezza() {
        return numeroCelleText.getText();
    }

    public JButton getModificaMappaButton() {
        return modificaMappaButton;
    }

    public JButton getCercaImmagineButton() {
        return cercaImmagineButton;
    }

    public JButton getModificaPuntoInizioButton() {
        return modificaPuntoInizioButton;
    }

    public JButton getModificaImmagineInizioButton() {
        return modificaImmagineInizioButton;
    }

    public JButton getModificaPuntoFineButton() {
        return modificaPuntoFineButton;
    }

    public JButton getModificaImmagineFineButton() {
        return modificaImmagineFineButton;
    }

    public JFileChooser getCercaImmagine() {
        return cercaImmagine;
    }

    public JList<SpriteEditor> getLista() {
        return lista;
    }

}