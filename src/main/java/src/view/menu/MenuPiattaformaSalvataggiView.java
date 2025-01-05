package src.view.menu;

import src.model.Gioco;
import src.model.Model;
import src.model.Salvataggio;
import src.utils.GenericButton;
import src.utils.Utils;
import src.view.menu.elemPanelView.ElemPanelView;
import src.view.menu.elemPanelView.ElemRimuovibilePanelView;
import src.view.menu.info.InfoGiocoPiattaforma;
import src.view.menu.info.InfoSalvataggio;

import javax.swing.*;

/**
 * View del menu della piattaforma contenente i vari salvataggi.
 * Ogni salvataggio sarà caratterizzato da un numero, questo numero sarà il suo identificativo.
 */
public class MenuPiattaformaSalvataggiView extends AbstractMenuView {

    private static MenuPiattaformaSalvataggiView instance = null;

    private final GenericButton nuovoElemButton;

    private MenuPiattaformaSalvataggiView() {
        super();
        nuovoElemButton = new GenericButton("platform_new_save");
    }

    @Override
    public JPanel initHeaderPanel(Model model) {
        Gioco gioco = (Gioco) model;
        HeaderView headerView = new HeaderView(Utils.getText("platform_saves_menu"));
        headerView.add(new InfoGiocoPiattaforma(gioco));
        headerView.add(nuovoElemButton);
        return headerView;
    }

    @Override
    protected ElemPanelView getElemPanelView(Model elemMenuCorrente, Model salvataggio) {
        ElemRimuovibilePanelView salvataggioPiattaformaPanelView = new ElemRimuovibilePanelView(salvataggio);
        salvataggioPiattaformaPanelView.setInfoPanel(new InfoSalvataggio((Salvataggio) salvataggio));
        return salvataggioPiattaformaPanelView;
    }

    public GenericButton getNuovoElemButton() {
        return nuovoElemButton;
    }

    public static MenuPiattaformaSalvataggiView getInstance() {
        if (instance == null)
            instance = new MenuPiattaformaSalvataggiView();
        return instance;
    }
}