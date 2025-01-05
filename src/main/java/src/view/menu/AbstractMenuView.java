package src.view.menu;

import src.model.Model;
import src.utils.GenericButton;
import src.utils.Settings;
import src.utils.Utils;
import src.view.menu.elemPanelView.ElemPanelView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Classe astratta che descrive una generica view.
 * Una view conterrà un header, un body e un pulsante per tornare indietro insieme alla lista degli elementi che deve far visualizzare.
 * Ogni elem sarà caratterizzato da un panel a se stante con le relative informazioni, modificabili o non.
 */
public abstract class AbstractMenuView extends JPanel {

    private JPanel headerPanel;
    private final JPanel bodyPanel;
    private final ArrayList<JPanel> listaElemPanel;
    private final JScrollPane bodyScrollPane;
    private final GenericButton indietroButton;

    public AbstractMenuView() {
        super();

        listaElemPanel = new ArrayList<>();

        headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        bodyPanel = new JPanel(new GridLayout(0, Settings.MENU_NUM_COLONNE));

        indietroButton = new GenericButton("back_button");

        bodyScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        bodyScrollPane.setViewportView(bodyPanel);
        bodyScrollPane.getVerticalScrollBar().setUnitIncrement(Settings.SCROLLPANE_VELOCITA);

        this.add(headerPanel);
        this.add(bodyScrollPane);
    }

    public void setHeaderPanel(Model model) {
        this.removeAll();
        this.headerPanel = this.initHeaderPanel(model);
        headerPanel.add(indietroButton);
        headerPanel.setOpaque(false);

        this.add(headerPanel);
        this.add(bodyScrollPane);

        this.revalidate();
        this.repaint();
    }

    public abstract JPanel initHeaderPanel(Model model);

    public ElemPanelView addElemPanel(Model elemMenuCorrente, Model elem) {
        ElemPanelView elemPanel = this.getElemPanelView(elemMenuCorrente, elem);
        listaElemPanel.add(elemPanel);
        return elemPanel;
    }

    protected abstract ElemPanelView getElemPanelView(Model elemMenuCorrente, Model elem);

    public void clearListaElemPanel() {
        listaElemPanel.clear();
    }

    public void updateListaElemPanel() {
        bodyPanel.removeAll();
        for (JPanel elem : listaElemPanel) {
            JPanel panel = new JPanel();    //per evitare che le dimensioni vengano distorte
            panel.add(elem);
            bodyPanel.add(panel);
        }

        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    public GenericButton getIndietroButton() {
        return indietroButton;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(Settings.STILE_MENU_SFONDO, 0, 0,
                this.getWidth(), this.getHeight(), null);
    }

}
