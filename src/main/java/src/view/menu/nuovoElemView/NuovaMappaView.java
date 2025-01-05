package src.view.menu.nuovoElemView;

import src.MainController;
import src.utils.Utils;

import javax.swing.*;

/**
 * JFrame che permette di creare una nuova mappa
 */
public class NuovaMappaView extends AbstractNuovoElemView {

    private static NuovaMappaView instance = null;

    public NuovaMappaView() {
        super();
    }

    public static NuovaMappaView getInstance() {
        if (instance == null)
            instance = new NuovaMappaView();
        return instance;
    }

    public void init() {
        this.setTitle(Utils.getText("new_map_title"));
    }

    public void mostraMessaggioErrore() {
        JOptionPane.showMessageDialog(MainController.getInstance().getFrame(),
                Utils.getText("error_map_name_already_existing"),
                Utils.getText("error_map_name_already_existing_title"), JOptionPane.ERROR_MESSAGE);
    }

    public void mostraMessaggioSuccesso() {
        JOptionPane.showMessageDialog(MainController.getInstance().getFrame(),
                Utils.getText("success_map_creation"),
                Utils.getText("success_map_creation_title"), JOptionPane.PLAIN_MESSAGE);
    }

}