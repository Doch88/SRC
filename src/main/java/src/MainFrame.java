package src;

import src.utils.Database;
import src.utils.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * Finestra principale del programma
 */
public class MainFrame extends JFrame {

    private final Database database = Database.getInstance();
    private static MainFrame instance = null;

    /**
     * inizializza la finestra con le impostazioni di base
     */
    private MainFrame() {
        super();
        this.setTitle(Settings.FRAME_TITOLO);
        this.setSize(Settings.FRAME_LARGHEZZA, Settings.FRAME_ALTEZZA);

        //necessarie per il KeyListener
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static MainFrame getInstance() {
        if (instance == null)
            instance = new MainFrame();
        return instance;
    }

    /**
     * modifica la view attualmente visualizzata nella finestra
     *
     * @param ContentPane view da sostituire alla corrente
     */
    public void update(Container ContentPane) {
        this.setContentPane(ContentPane);
        this.setVisible(true);
    }


}
