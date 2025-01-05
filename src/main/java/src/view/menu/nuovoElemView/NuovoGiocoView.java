package src.view.menu.nuovoElemView;

import src.MainController;
import src.utils.GenericButton;
import src.utils.Utils;

import javax.swing.*;
import java.io.File;

/**
 * JFrame che permette di creare un nuovo gioco
 */
public class NuovoGiocoView extends AbstractNuovoElemView {

    private static NuovoGiocoView instance = null;

    private final JTextField urlImmaginePlayerText;
    private final GenericButton immaginePlayerButton;
    private final JPanel playerPanel;

    public NuovoGiocoView() {
        super();
        urlImmaginePlayerText = new JTextField(30);
        JLabel immagineLabel = new JLabel(Utils.getText("player_image") + ": ");
        immaginePlayerButton = new GenericButton("find_player");

        playerPanel = new JPanel();
        playerPanel.add(immagineLabel);
        playerPanel.add(urlImmaginePlayerText);
        playerPanel.add(immaginePlayerButton);

        mainPanel.add(playerPanel, 2);
        this.pack();
    }

    public static NuovoGiocoView getInstance() {
        if (instance == null)
            instance = new NuovoGiocoView();
        return instance;
    }

    public void init() {
        this.setTitle(Utils.getText("new_game_title"));
    }

    /**
     * Mostra un messaggio di errore riguardante il nome già presente.
     */
    public void mostraMessaggioErrore() {
        JOptionPane.showMessageDialog(MainController.getInstance().getFrame(),
                Utils.getText("error_game_name_already_existing"),
                Utils.getText("error_game_name_already_existing_title"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Mostra un messaggio di inserimento avvenuto con successo.
     */
    public void mostraMessaggioSuccesso() {
        JOptionPane.showMessageDialog(MainController.getInstance().getFrame(),
                Utils.getText("success_game_creation"),
                Utils.getText("success_game_creation_title"), JOptionPane.PLAIN_MESSAGE);
    }

    public void cercaImmaginePlayer() {
        immagine.showOpenDialog(this);
        File a = immagine.getSelectedFile();
        urlImmaginePlayerText.setText(a.getPath());
    }

    public String getUrlImmaginePlayerText() {
        return urlImmaginePlayerText.getText();
    }

    public GenericButton getCercaImmaginePlayerButton() {
        return immaginePlayerButton;
    }

}