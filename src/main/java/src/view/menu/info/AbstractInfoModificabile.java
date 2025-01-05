package src.view.menu.info;

import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Un pannello di informazioni modificabile, associato ad un elemento dei menu.
 */
public abstract class AbstractInfoModificabile extends JPanel {

    protected JButton confermaButton;

    public AbstractInfoModificabile() {
        super();
        this.setOpaque(false);
        confermaButton = new JButton(Utils.getText("confirm_button"));
    }

    public JButton getConfermaButton() {
        return confermaButton;
    }

    protected void addConfermaButton() {
        this.add(confermaButton);
    }

    public String getValueOf(String name) {
        for (Component component : this.getComponents()) {
            if (component.getName() != null && component.getName().equalsIgnoreCase(name) && component instanceof JTextField) {
                return ((JTextField) component).getText();
            }
        }
        return null;
    }
}
