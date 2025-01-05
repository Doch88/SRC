package src.view.piattaforma;

import src.utils.Settings;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Questa view mostrerà la schermata di gameover.
 */
public class PiattaformaGameOverView extends JPanel {

    private static PiattaformaGameOverView instance = null;

    private PiattaformaGameOverView() {
        super();
        this.setBackground(Color.BLACK);
    }

    public static PiattaformaGameOverView getInstance() {
        if (instance == null)
            instance = new PiattaformaGameOverView();
        return instance;
    }

    /**
     * sostituisce la view attuale con questa
     */
    public void init() {
        Utils.updateView(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(new Color(0xFFFFFF));

        FontMetrics metrics = g.getFontMetrics(Settings.STILE_PIATTAFORMA_MSG_FONT);
        g.setFont(Settings.STILE_PIATTAFORMA_MSG_FONT);

        String str;
        g.drawString(str = Utils.getText("game_over_lost"), (this.getWidth() - metrics.stringWidth(str)) / 2, (this.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent());

        g.dispose();
    }

}
