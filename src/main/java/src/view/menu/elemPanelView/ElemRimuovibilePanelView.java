package src.view.menu.elemPanelView;

import src.model.Model;

import javax.swing.*;
import java.awt.*;

/**
 * Un ElemPanelView che permette di rimuovere il Model associato
 */
public class ElemRimuovibilePanelView extends ElemPanelView {

    protected JButton removeElemButton;

    public ElemRimuovibilePanelView(Model elem) {
        super(elem);

        removeElemButton = new JButton("X");
        removeElemButton.setName(elem.getNome());
        removeElemButton.setBorder(BorderFactory.createEmptyBorder());
        removeElemButton.setContentAreaFilled(false);

        this.addToHeader(removeElemButton, BorderLayout.EAST);
    }

    public JButton getRemoveElemButton() {
        return removeElemButton;
    }

}