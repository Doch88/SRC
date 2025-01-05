package src.view.piattaforma;

import src.utils.Settings;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * questa view mostrerà la schermata di vittoria (fine livello)
 */
public class PiattaformaWinView extends JPanel {

    private static PiattaformaWinView instance = null;

    private boolean fineGioco;

    private PiattaformaWinView() {
        super();
        this.setBackground(Color.BLACK);
    }

    public static PiattaformaWinView getInstance() {
        if (instance == null)
            instance = new PiattaformaWinView();
        return instance;
    }

    /**
     * sostituisce la view attuale con questa
     *
     * @param fineGioco true se il gioco è terminato e non ci sono altre mappe, false altrimenti;
     *                  serve per visualizzare un messaggio apposito nella view
     */
    public void init(boolean fineGioco) {
        this.fineGioco = fineGioco;
        Utils.updateView(this);
    }

    /**
     * visualizza un messaggio di fine livello
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(new Color(0xFFFFFF));

        FontMetrics metrics = g.getFontMetrics(Settings.STILE_PIATTAFORMA_MSG_FONT);    //serve per calcolare la lunghezza di un testo in termini di pixel
        g.setFont(Settings.STILE_PIATTAFORMA_MSG_FONT);

        String str;
        if (fineGioco)
            g.drawString(str = Utils.getText("game_over_win"), (this.getWidth() - metrics.stringWidth(str)) / 2, (this.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent());
        else
            g.drawString(str = Utils.getText("next_map"), (this.getWidth() - metrics.stringWidth(str)) / 2, (this.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent());

        g.dispose();
    }

}
