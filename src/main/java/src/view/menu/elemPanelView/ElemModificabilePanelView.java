package src.view.menu.elemPanelView;

import src.model.Model;
import src.view.menu.info.AbstractInfoModificabile;

import javax.swing.*;

/**
 * Un ElemRimuovibilePanelView che permette di modificare le informazioni del Model associato
 */
public class ElemModificabilePanelView extends ElemRimuovibilePanelView {

    private JButton removeElemButton;

    public ElemModificabilePanelView(Model elem) {
        super(elem);
    }

    public JButton getConfermaButton() {
        return ((AbstractInfoModificabile) infoPanel).getConfermaButton();
    }

    /**
     * Metodo che permette di ottenere il contenuto di una casella di testo all'interno dell'infoPanel
     *
     * @param name nome della casella di testo da cui ottenere il testo
     * @return il contenuto della casella di testo
     */
    public String getValueOf(String name) {
        return ((AbstractInfoModificabile) infoPanel).getValueOf(name);
    }

}