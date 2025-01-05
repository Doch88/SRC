package src.view.menu.info;

import src.model.Gioco;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Un pannelo contenente le informazioni di un determinato gioco nella piattaforma.
 */
public class InfoGiocoPiattaforma extends JPanel {

    public InfoGiocoPiattaforma(Gioco gioco) {
        super();
        this.setOpaque(false);
        this.setLayout(new GridLayout(0, 1));

        this.add(new JLabel(Utils.getText("framerate") + ": " + gioco.getFramerate()));
        this.add(new JLabel(Utils.getText("player_health") + ": " + gioco.getPlayer().getSaluteMassima()));
        this.add(new JLabel(Utils.getText("player_damage") + ": " + gioco.getPlayer().getAttacco()));
        this.add(new JLabel(Utils.getText("player_cooldown") + ": " + gioco.getPlayer().getPeriodoDiRiposo()));
        this.add(new JLabel(Utils.getText("player_attack_frequency") + ": " + gioco.getPlayer().getPeriodoDiRiposoSparo()));
    }

}
