package src;

import src.utils.GenericButton;
import src.utils.Settings;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * La prima view visualizzata all'interno del programma.
 * Contiene i bottoni per scegliere la modalità da avviare.
 */
public class MainView extends JPanel {

    private static MainView instance = null;

    private final GenericButton editorButton;
    private final GenericButton piattaformaButton;
    private final GenericButton resetDatabaseButton;

    /**
     * inizializza il layout della view e crea i bottoni
     */
    private MainView() {
        editorButton = new GenericButton("editor_main_menu");
        piattaformaButton = new GenericButton("platform_main_menu");
        resetDatabaseButton = new GenericButton("reset_main_menu");

        LayoutManager lm = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(lm);

        this.add(Box.createVerticalGlue());
        this.add(editorButton);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(piattaformaButton);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(resetDatabaseButton);
        this.add(Box.createVerticalGlue());

    }

    public static MainView getInstance() {
        if (instance == null)
            instance = new MainView();
        return instance;
    }

    /**
     * disegna lo sfondo della view
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(Settings.STILE_MENU_SFONDO, 0, 0,
                this.getWidth(), this.getHeight(), null);
        g.setFont(Settings.STILE_MENU_FONT);
        g.setColor(Color.DARK_GRAY);
        FontMetrics metrics = g.getFontMetrics(Settings.STILE_MENU_FONT);

        g.drawString(Settings.FRAME_TITOLO,
                this.getWidth() / 2 - metrics.stringWidth(Settings.FRAME_TITOLO) / 2,
                this.getHeight() / 10 + metrics.getHeight() / 2);
    }

    public GenericButton getEditorButton() {
        return editorButton;
    }

    public GenericButton getPiattaformaButton() {
        return piattaformaButton;
    }

    public GenericButton getResetDatabaseButton() {
        return resetDatabaseButton;
    }
}
