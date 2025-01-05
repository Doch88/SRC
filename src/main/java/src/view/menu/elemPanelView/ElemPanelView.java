package src.view.menu.elemPanelView;

import src.model.Model;
import src.utils.ElemButton;
import src.utils.GenericButton;
import src.utils.Settings;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;


/**
 * Un elemento contenuto all'interno di uno dei Menu del programma.
 * Ogni ElemPanelView è relativo ad un determinato Model e rappresenta appunto tale Model all'interno del Menu
 * permettendo di modificarlo, di visualizzarlo o di selezionarlo per aprirlo.
 * <p>
 * Ogni ElemPanelView è diviso essenzialmente in tre parti:
 * header: contenente il nome e, nel caso di ElemRimuovibilePanelView, un bottone che permette di eliminare l'elemento
 * body: contenente il bottone che permette di selezionare il Model e un immagine rappresentativa
 * infoPanel: un pannello contenente informazioni sul Model che si possono eventualmente modificare, ed è contenuto a sua volta nel body
 */
public class ElemPanelView extends JPanel {

    protected JLabel label;
    protected ElemButton caricaElemButton;
    protected JPanel infoPanelContainer, infoPanel;
    private final JPanel header;
    private final JPanel body;

    private final Model elem;

    /**
     * Inizializza l'ElemPanelView e associa un Model ad esso
     *
     * @param elem
     */
    public ElemPanelView(Model elem) {
        super();
        this.elem = elem;

        this.setBackground(new Color(0xEEEEFF));

        header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        body = new JPanel();
        body.setOpaque(false);

        Dimension dimension = new Dimension(Settings.ELEM_PANEL_HEADER_LARGHEZZA, Settings.ELEM_PANEL_HEADER_ALTEZZA);

        header.setPreferredSize(dimension);
        header.setMaximumSize(dimension);
        header.setMinimumSize(dimension);

        header.setAlignmentY(JPanel.CENTER_ALIGNMENT);

        this.setPreferredSize(new Dimension(Settings.ELEM_PANEL_LARGHEZZA, Settings.ELEM_PANEL_ALTEZZA));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        label = new JLabel(Utils.getText(elem.getNome()));
        caricaElemButton = new ElemButton(elem);

        header.add(label);

        infoPanelContainer = new JPanel();
        infoPanelContainer.setOpaque(false);
        infoPanel = new JPanel();
        body.add(caricaElemButton);
        body.add(infoPanelContainer);

        this.add(header);
        this.add(body);
    }

    /**
     * Aggiunge un elemento all'header
     *
     * @param component
     * @param pos
     */
    public void addToHeader(Component component, String pos) {
        header.add(component, pos);
    }

    /**
     * Aggiunge un elemento al body
     *
     * @param component elemento da aggiungere
     */
    public void addToBody(Component component) {
        body.add(component);
    }

    public GenericButton getCaricaElemButton() {
        return caricaElemButton;
    }

    public void setInfoPanel(JPanel infoPanel) {
        this.infoPanel = infoPanel;
        this.infoPanelContainer.removeAll();
        this.infoPanelContainer.add(infoPanel);
        this.infoPanelContainer.revalidate();
        this.infoPanelContainer.repaint();
    }

    public Model getElem() {
        return elem;
    }

}