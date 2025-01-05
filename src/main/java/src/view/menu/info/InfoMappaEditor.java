package src.view.menu.info;

import src.model.Mappa;
import src.utils.NumberTextField;
import src.utils.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * Contiene le informazioni relative ad una mappa, liberamente modificabili.
 */
public class InfoMappaEditor extends AbstractInfoModificabile {

    private final JTextField nomeText;
    private final JTextField dimCellaText;
    private final JTextField numCelleAsseText;

    private final JFileChooser cercaImmagine;

    private final JButton cercaImmagineButton;

    private final JButton modificaImmagineInizioButton;
    private final JButton modificaImmagineFineButton;

    public InfoMappaEditor(Mappa mappa) {
        super();
        this.setOpaque(false);
        this.setLayout(new GridLayout(0, 2));

        cercaImmagine = new JFileChooser();
        cercaImmagine.setMultiSelectionEnabled(true);
        cercaImmagine.addChoosableFileFilter(new FileNameExtensionFilter(Utils.getText("image_filter_desc"), "png", "jpg", "jpeg", "gif"));

        cercaImmagineButton = new JButton(Utils.getText("find_image"));

        modificaImmagineInizioButton = new JButton(Utils.getText("starting_image_map"));
        modificaImmagineFineButton = new JButton(Utils.getText("end_image_map"));

        cercaImmagineButton.setName("background");

        nomeText = new JTextField(mappa.getNome());
        nomeText.setName(Utils.getText("name_label"));
        this.add(new JLabel(Utils.getText("name_label") + ": "));
        this.add(nomeText);

        dimCellaText = new NumberTextField(mappa.getDimCella());
        dimCellaText.setName(Utils.getText("cell_size"));
        this.add(new JLabel(Utils.getText("cell_size") + ": "));
        this.add(dimCellaText);

        numCelleAsseText = new NumberTextField(mappa.getNumCelleAsse());
        numCelleAsseText.setName(Utils.getText("num_cells"));
        this.add(new JLabel(Utils.getText("num_cells") + ": "));
        this.add(numCelleAsseText);

        this.add(modificaImmagineInizioButton);
        this.add(modificaImmagineFineButton);

        this.add(confermaButton);
    }
}