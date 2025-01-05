package src.view.menu;

import src.model.Gioco;
import src.model.Model;
import src.view.menu.elemPanelView.ElemPanelView;
import src.view.menu.info.InfoGiocoPiattaforma;
import src.utils.Utils;

import javax.swing.*;

/**
 * View del menu della piattaforma contenente i vari giochi disponibili.
 * Insieme ai giochi verranno descritte anche alcune caratteristiche riguardanti il player e il framerate del gioco.
 */
public class MenuPiattaformaGiochiView extends AbstractMenuView {

    private static MenuPiattaformaGiochiView instance = null;

    private MenuPiattaformaGiochiView() {
        super();
    }

    public static MenuPiattaformaGiochiView getInstance() {
        if (instance == null)
            instance = new MenuPiattaformaGiochiView();
        return instance;
    }

    @Override
    public JPanel initHeaderPanel(Model model) {
        return new HeaderView(Utils.getText("platform_games_menu"));
    }

    @Override
    protected ElemPanelView getElemPanelView(Model elemMenuCorrente, Model gioco) {
        ElemPanelView giocoPiattaformaPanelView = new ElemPanelView(gioco);
        giocoPiattaformaPanelView.setInfoPanel(new InfoGiocoPiattaforma((Gioco) gioco));
        return giocoPiattaformaPanelView;
    }
}