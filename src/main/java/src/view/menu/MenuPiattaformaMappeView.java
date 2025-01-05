package src.view.menu;

import src.model.Mappa;
import src.model.Model;
import src.model.Salvataggio;
import src.view.menu.elemPanelView.ElemPanelView;
import src.view.menu.info.InfoMappaPiattaforma;
import src.view.menu.info.InfoSalvataggio;
import src.utils.Utils;

import javax.swing.*;

/**
 * View del menu della piattaforma contenente le mappe di un salvataggio e le relative informazioni.
 * Ogni mappa avrà al suo fianco le informazioni riguardanti il salvataggio del giocatore su quella specifica mappa, quali i collezionabili raccolti
 */
public class MenuPiattaformaMappeView extends AbstractMenuView {

    private static MenuPiattaformaMappeView instance = null;

    private MenuPiattaformaMappeView() {
        super();
    }

    @Override
    public JPanel initHeaderPanel(Model model) {
        Salvataggio salvataggio = (Salvataggio) model;
        HeaderView headerView = new HeaderView(Utils.getText("platform_maps_menu"));
        headerView.add(new InfoSalvataggio(salvataggio));
        return headerView;
    }

    @Override
    protected ElemPanelView getElemPanelView(Model elemMenuCorrente, Model mappa) {
        ElemPanelView mappaPiattaformaPanelView = new ElemPanelView(mappa);
        if (((Salvataggio) elemMenuCorrente).getPosizioneMappaAttuale() < ((Mappa) mappa).getPosizione())
            mappaPiattaformaPanelView.getCaricaElemButton().setEnabled(false);
        mappaPiattaformaPanelView.setInfoPanel(new InfoMappaPiattaforma((Salvataggio) elemMenuCorrente, (Mappa) mappa));
        return mappaPiattaformaPanelView;
    }

    public static MenuPiattaformaMappeView getInstance() {
        if (instance == null)
            instance = new MenuPiattaformaMappeView();
        return instance;
    }
}