package src.view.editor;

import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Frame elementare che permette di creare un nuovo sprite.
 * La sua struttura è basica, poiché la costruzione vera e propria dello sprite avviene tramite la sidebar sinistra.
 */
public class FrameCreaSpriteView extends JFrame {

    private static FrameCreaSpriteView instance = null;

    private final JTextArea nomeText;
    private final JButton confermaBottone;
    private final ButtonGroup group;

    public FrameCreaSpriteView() {
        super();

        this.setTitle(Utils.getText("sprite_creation"));
        this.setSize(300, 400);
        //this.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 2));

        mainPanel.add(new JLabel(Utils.getText("sprite_name") + ": "));
        nomeText = new JTextArea("");
        mainPanel.add(nomeText);

        mainPanel.add(new JLabel(Utils.getText("block") + " "));
        JRadioButton bloccoButton = new JRadioButton();
        bloccoButton.setActionCommand("block");
        bloccoButton.setSelected(true);
        mainPanel.add(bloccoButton);

        mainPanel.add(new JLabel(Utils.getText("alert_point_ext") + " "));
        JRadioButton alertPointButton = new JRadioButton();
        alertPointButton.setActionCommand("alert_point");
        mainPanel.add(alertPointButton);

        mainPanel.add(new JLabel(Utils.getText("npc") + " "));
        JRadioButton NPCButton = new JRadioButton();
        NPCButton.setActionCommand("npc");
        mainPanel.add(NPCButton);

        mainPanel.add(new JLabel(Utils.getText("player") + " "));
        JRadioButton playerButton = new JRadioButton();
        playerButton.setActionCommand("player");
        mainPanel.add(playerButton);

        mainPanel.add(new JLabel(Utils.getText("portal") + " "));
        JRadioButton portaleButton = new JRadioButton();
        portaleButton.setActionCommand("portal");
        mainPanel.add(portaleButton);

        mainPanel.add(new JLabel(Utils.getText("bullet") + " "));
        JRadioButton proiettileButton = new JRadioButton();
        proiettileButton.setActionCommand("bullet");
        mainPanel.add(proiettileButton);

        mainPanel.add(new JLabel(Utils.getText("health") + " "));
        JRadioButton saluteButton = new JRadioButton();
        saluteButton.setActionCommand("health");
        mainPanel.add(saluteButton);

        mainPanel.add(new JLabel(Utils.getText("collectable") + " "));
        JRadioButton collezionabileButton = new JRadioButton();
        collezionabileButton.setActionCommand("collectable");
        mainPanel.add(collezionabileButton);

        mainPanel.add(new JLabel(Utils.getText("bullet_set") + " "));
        JRadioButton mucchioButton = new JRadioButton();
        mucchioButton.setActionCommand("bullets");
        mainPanel.add(mucchioButton);

        group = new ButtonGroup();
        group.add(alertPointButton);
        group.add(bloccoButton);
        group.add(mucchioButton);
        group.add(collezionabileButton);
        group.add(proiettileButton);
        group.add(playerButton);
        group.add(saluteButton);
        group.add(portaleButton);
        group.add(NPCButton);

        confermaBottone = new JButton(Utils.getText("confirm_button"));

        mainPanel.add(confermaBottone);
        //this.pack();
        this.add(mainPanel);
    }

    public static FrameCreaSpriteView getInstance() {
        if (instance == null)
            instance = new FrameCreaSpriteView();
        return instance;
    }

    public void init() {
        this.setVisible(true);
    }

    public String getNome() {
        return nomeText.getText();
    }

    public JButton getConfermaBottone() {
        return confermaBottone;
    }

    public ButtonModel getGroupSelezionato() {
        return group.getSelection();
    }
}
