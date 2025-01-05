package src.view.piattaforma;

import src.model.piattaforma.Animazione;
import src.model.piattaforma.Piattaforma;
import src.model.piattaforma.Sprite.Sprite;
import src.utils.Settings;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * View principale della piattaforma.
 * In questa view verr&agrave; eseguita la grafica del gioco,
 * aggiornata di mano in mano dalla funzione updateByNome() di PiattaformaController
 */
public class PiattaformaView extends JPanel {

    private static PiattaformaView instance = null;

    private final Piattaforma piattaforma;

    private Animazione animazioneSfondo;

    private int xSurplus = 0;
    private int ySurplus = 0;

    private int altezzaTesto = 0;
    private String messaggio = "";

    public PiattaformaView() {
        super();
        piattaforma = Piattaforma.getInstance();
        this.setBackground(Color.WHITE);
    }

    public static PiattaformaView getInstance() {
        if (instance == null)
            instance = new PiattaformaView();
        return instance;
    }

    /**
     * sostituisce la view attuale con questa
     */
    public void init() {
        Utils.updateView(this);
        this.setDoubleBuffered(true);
        Utils.focusWindow();
        xSurplus = 0;
        ySurplus = 0;
    }

    /**
     * Prende l'animazione del background dalla cartella degli sprites
     *
     * @param file file iniziale del background
     */
    public void createBackgroundAnimation(String file) {
        Image bg = Utils.getBufferedImage(Settings.CARTELLA_MAPPE, file);
        if (bg != null)
            animazioneSfondo = new Animazione(file, Settings.CARTELLA_MAPPE, bg);
    }

    private void scriviInAlto(Graphics g, String text) {
        Font font = new Font("Arial Black", Font.BOLD, 20);
        g.setColor(new Color(0x000000));

        g.setFont(font);
        g.drawString(text, 30, 30 + altezzaTesto);

        altezzaTesto += g.getFontMetrics(font).getHeight() + 10;
    }

    private void paintBackground(Graphics f) {
        if (animazioneSfondo != null)
            f.drawImage(animazioneSfondo.getImage(Sprite.Direction.NO_DIRECTION), 0, 0, this.getWidth(), this.getHeight(), null);
    }

    /**
     * Disegna le informazioni generali su salute e oggetti raccolti
     *
     * @param g
     */
    private void disegnaSalute(Graphics g) {
        //Scritta "Salute"
        g.setColor(new Color(0x000000));
        g.drawString(Utils.getText("health_hud") + ": ", 10, this.getHeight() - 30);

        //Box contentente la salute attuale
        g.setColor(new Color(0xFF0000));
        g.drawRect(70, this.getHeight() - 50, 200, 30);

        //Riempe il box con la percentuale di salute attuale
        double percentuale = ((double) piattaforma.getPlayer().getSaluteAttuale()) / ((double) piattaforma.getPlayer().getSaluteMassima());
        g.fillRect(70, this.getHeight() - 50, (int) (percentuale * 200), 30);

    }

    /**
     * Analizza la lista dei collezionabili raccolti e la stampa su schermo
     */
    private void scriviCollezionabili(Graphics g) {
        int addWidth = 0;

        FontMetrics metrics = g.getFontMetrics(Settings.STILE_PIATTAFORMA_FONT); //Ci servirà per calcolare la lunghezza di un testo in pixel

        g.setColor(new Color(0x000000));
        if (piattaforma.getSalvataggio().getCollezionabili(piattaforma.getMappa().getPosizione()) != null)
            for (Map.Entry<String, Integer> entry : piattaforma.getSalvataggio().getCollezionabili(piattaforma.getMappa().getPosizione()).entrySet()) {
                String name = Utils.getText(entry.getKey());
                String text = name.substring(0, 1).toUpperCase() + name.substring(1) + ": " + entry.getValue() + "/" + piattaforma.calcolaCollezionabileTotale(entry.getKey());
                g.drawString(text, 300 + addWidth, this.getHeight() - 30);
                addWidth += metrics.stringWidth(text + "  ");
            }
        g.drawString(Utils.getText("bullets_hud") + ": " + piattaforma.getPlayer().getNumeroProiettili(), 300 + addWidth, this.getHeight() - 30);
        addWidth += metrics.stringWidth(Utils.getText("bullets_hud") + ": " + piattaforma.getPlayer().getNumeroProiettili() + " ");
        g.drawString(Utils.getText("score_hud") + ": " + piattaforma.getSalvataggio().getPunteggio(), 300 + addWidth, this.getHeight() - 30);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Gestione dello scorrimento dello schermo.
        int nuovoxSurplus = piattaforma.getPlayer().getX() - Math.round((float) this.getWidth() / 4.0f)/* + Math.round((float) piattaforma.getPlayer().getWidth() / 2.0f)*/;
        if (nuovoxSurplus > 0
                && this.getWidth() + nuovoxSurplus <= piattaforma.getxMax())
            xSurplus = nuovoxSurplus;
        else if (nuovoxSurplus <= 0)
            xSurplus = 0;
        else if (piattaforma.getxMax() > this.getWidth() && this.getWidth() + nuovoxSurplus > piattaforma.getxMax())
            xSurplus = piattaforma.getxMax() - this.getWidth();

        int nuovoySurplus = piattaforma.getPlayer().getY() - Math.round((float) this.getHeight() / 2.0f)/* + Math.round((float) piattaforma.getPlayer().getHeight() / 2.0f)*/;
        if (nuovoySurplus > 0
                && this.getHeight() + nuovoySurplus <= piattaforma.getyMax())
            ySurplus = nuovoySurplus;
        else if (nuovoySurplus <= 0)
            ySurplus = 0;
        else if (piattaforma.getyMax() > this.getHeight() && (this.getHeight() + nuovoySurplus > piattaforma.getyMax()))
            ySurplus = piattaforma.getyMax() - this.getHeight();

        altezzaTesto = 0;

        g.setFont(Settings.STILE_PIATTAFORMA_FONT);
        paintBackground(g);

        if (piattaforma.getAnimazionePuntoFinale() != null)
            g.drawImage(piattaforma.getAnimazionePuntoFinale().getImage(Sprite.Direction.NO_DIRECTION),
                    piattaforma.getCoordinatePuntoFine().x - xSurplus,
                    piattaforma.getCoordinatePuntoFine().y - ySurplus,
                    this);
        if (piattaforma.getAnimazionePuntoIniziale() != null)
            g.drawImage(piattaforma.getAnimazionePuntoIniziale().getImage(Sprite.Direction.NO_DIRECTION),
                    piattaforma.getCoordinatePuntoIniziale().x - xSurplus,
                    piattaforma.getCoordinatePuntoIniziale().y - ySurplus,
                    this);

        synchronized (piattaforma.getSprites()) {
            //Disegniamo ogni sprites nella posizione giusta relativa al player
            for (Sprite sprite : piattaforma.getSprites())
                if (sprite.getImmagine() != null && sprite.getX() - xSurplus < getWidth() && sprite.getY() - ySurplus < getHeight()
                        && sprite.getX() + sprite.getWidth() - xSurplus > 0 && sprite.getY() + sprite.getHeight() - ySurplus > 0)
                    g.drawImage(sprite.getImmagine(), sprite.getX() - xSurplus, sprite.getY() - ySurplus, this);
        }
        disegnaSalute(g);
        scriviCollezionabili(g);

        //Stampa a schermo la possibilità di completare il livello se si è arrivati al punto finale
        if (piattaforma.collisionePuntoFine())
            messaggio = Utils.getText("complete_level");

        this.scriviInAlto(g, messaggio);
        messaggio = "";
        g.dispose();//Libera le risorse
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

}
