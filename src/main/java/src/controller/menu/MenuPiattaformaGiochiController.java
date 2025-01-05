package src.controller.menu;

import src.model.DAO.GiocoDAO;
import src.utils.ElemButton;
import src.utils.Utils;
import src.view.menu.MenuPiattaformaGiochiView;
import src.view.menu.elemPanelView.ElemPanelView;

/**
 * Controller del menu della piattaforma che mostra i giochi.
 */
public class MenuPiattaformaGiochiController extends AbstractMenuController {

    private static MenuPiattaformaGiochiController instance = null;

    public MenuPiattaformaGiochiController() {
        super();

        dao = GiocoDAO.getInstance();

        view = MenuPiattaformaGiochiView.getInstance();
        view.getIndietroButton().addActionListener(e -> indietroAction());
    }

    public static MenuPiattaformaGiochiController getInstance() {
        if (instance == null)
            instance = new MenuPiattaformaGiochiController();
        return instance;
    }

    @Override
    public void addElemButtonListeners(ElemPanelView elemPanel) {
        elemPanel.getCaricaElemButton().addActionListener(e -> MenuPiattaformaSalvataggiController.getInstance().init(((ElemButton) e.getSource()).getElem()));
    }

    @Override
    protected void indietroAction() {
        Utils.setMainView();
    }

}