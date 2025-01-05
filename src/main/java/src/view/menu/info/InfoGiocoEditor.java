package src.view.menu.info;

import src.model.Gioco;
import src.utils.NumberTextField;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Contiene le informazioni relative ad un gioco, liberamente modificabili.
 */
public class InfoGiocoEditor extends AbstractInfoModificabile {

    private final NumberTextField framerate;
    private final NumberTextField salutePlayer;
    private final NumberTextField attaccoPlayer;
    private final NumberTextField periodoRiposoPlayer;
    private final NumberTextField periodoRiposoSparoPlayer;
    private final NumberTextField velocitaPlayer;
    private final JTextField proiettile;

    public InfoGiocoEditor(Gioco gioco) {
        super();
        this.setOpaque(false);
        this.setLayout(new GridLayout(0, 2));

        framerate = new NumberTextField(gioco.getFramerate());
        framerate.setName(Utils.getText("framerate"));
        this.add(new JLabel(Utils.getText("framerate") + ": "));
        this.add(framerate);

        salutePlayer = new NumberTextField(gioco.getPlayer().getSaluteMassima());
        salutePlayer.setName(Utils.getText("health") );
        this.add(new JLabel(Utils.getText("player_health") + ": "));
        this.add(salutePlayer);

        attaccoPlayer = new NumberTextField(gioco.getPlayer().getAttacco());
        attaccoPlayer.setName(Utils.getText("attack") );
        this.add(new JLabel(Utils.getText("player_damage") + ": "));
        this.add(attaccoPlayer);

        periodoRiposoPlayer = new NumberTextField(gioco.getPlayer().getPeriodoDiRiposo());
        periodoRiposoPlayer.setName(Utils.getText("cooldown"));
        this.add(new JLabel(Utils.getText("player_cooldown") + ": "));
        this.add(periodoRiposoPlayer);

        periodoRiposoSparoPlayer = new NumberTextField(gioco.getPlayer().getPeriodoDiRiposoSparo());
        periodoRiposoSparoPlayer.setName(Utils.getText("attack_frequency") );
        this.add(new JLabel(Utils.getText("player_attack_frequency")  + ": "));
        this.add(periodoRiposoSparoPlayer);

        velocitaPlayer = new NumberTextField(gioco.getPlayer().getVelocita());
        velocitaPlayer.setName(Utils.getText("speed") );
        this.add(new JLabel(Utils.getText("player_speed")  + ": "));
        this.add(velocitaPlayer);

        proiettile = new JTextField(gioco.getPlayer().getNomeProiettile());
        proiettile.setName(Utils.getText("bullet") );
        this.add(new JLabel(Utils.getText("bullet") + ": "));
        this.add(proiettile);

        this.addConfermaButton();
    }

}
