package src;

import src.controller.menu.MenuEditorGiochiController;
import src.controller.menu.MenuPiattaformaGiochiController;
import src.utils.Database;
import src.utils.Utils;

import javax.swing.*;

/**
 * Controller del menu principale, contiene il main del programma
 */
public class MainController {

    private static MainController instance = null;

    private final Database db = Database.getInstance();

    private final MainFrame frame = MainFrame.getInstance();
    private MainView view = MainView.getInstance();

    /**
     * aggiunge i listener ai bottoni "Editor" e "Piattaforma"
     * e visualizza la view nel frame
     */
    private MainController() {
        view.getEditorButton().addActionListener(e -> editorAction());
        view.getPiattaformaButton().addActionListener(e -> piattaformaAction());
        view.getResetDatabaseButton().addActionListener(e -> resetAction());
        frame.update(view);
    }

    /**
     * avvia il programma richiamando l'istanza del main controller
     */
    public static void main(String[] args) {
        MainController.getInstance();
    }

    public static MainController getInstance() {
        if (instance == null)
            instance = new MainController();
        return instance;
    }

    /**
     * al click sul bottone apposito, viene richiamato questo metodo che carica il menu dell'editor
     */
    private void editorAction() {
        MenuEditorGiochiController.getInstance().init(null);
    }

    /**
     * al click sul bottone apposito, viene richiamato questo metodo che carica il menu della piattaforma
     */
    private void piattaformaAction() {
        MenuPiattaformaGiochiController.getInstance().init(null);
    }

    /**
     * cliccando sul bottone di Reset avvieremo la sequenza di reset del database, che permette di cancellare tutto il contenuto al suo interno
     * e di ripristinarlo alle impostazioni originali, importandolo dal file .sql associato
     */
    private void resetAction() {
        JOptionPane.showMessageDialog(getFrame(),
                Utils.getText("db_reset"),
                Utils.getText("reset_main_menu"), JOptionPane.INFORMATION_MESSAGE);
        try {
            db.resetDatabase();
            JOptionPane.showMessageDialog(getFrame(),
                    Utils.getText("db_reset_success"),
                    Utils.getText("success"), JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(getFrame(),
                    Utils.getText("db_reset_error") +  ": \n" + e.getMessage(),
                    Utils.getText("error"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * @return l'istanza della finestra del programma
     */
    public MainFrame getFrame() {
        return frame;
    }

    public MainView getView() {
        return view;
    }

    public void setView(MainView view) {
        this.view = view;
    }
}