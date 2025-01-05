package src.controller.menu;

import src.controller.piattaforma.PiattaformaController;
import src.model.DAO.MappaDAO;
import src.model.Mappa;
import src.model.Model;
import src.model.Salvataggio;
import src.utils.ElemButton;
import src.view.menu.MenuPiattaformaMappeView;
import src.view.menu.elemPanelView.ElemPanelView;

/**
 * Controller del menu della piattaforma che mostra le mappe di un salvataggio.
 */
public class MenuPiattaformaMappeController extends AbstractMenuController {

    private static MenuPiattaformaMappeController instance = null;

    private MenuPiattaformaMappeController() {
        super();

        dao = MappaDAO.getInstance();

        view = MenuPiattaformaMappeView.getInstance();
        view.getIndietroButton().addActionListener(e -> indietroAction());
    }

    public static MenuPiattaformaMappeController getInstance() {
        if (instance == null)
            instance = new MenuPiattaformaMappeController();
        return instance;
    }

    @Override
    public void addElemButtonListeners(ElemPanelView elemPanel) {
        elemPanel.getCaricaElemButton().addActionListener(e -> caricaPiattaforma(((ElemButton) e.getSource()).getElem()));
    }

    protected void caricaPiattaforma(Model mappa) {
        PiattaformaController.getInstance().init((Salvataggio) this.getElemMenuCorrente(), (Mappa) mappa);
    }

    @Override
    protected void indietroAction() {
        MenuPiattaformaSalvataggiController.getInstance().init(((Salvataggio) this.getElemMenuCorrente()).getGioco());
    }

}