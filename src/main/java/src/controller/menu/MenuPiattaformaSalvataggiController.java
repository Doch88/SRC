package src.controller.menu;

import src.model.DAO.SalvataggioDAO;
import src.model.Gioco;
import src.model.Model;
import src.model.Salvataggio;
import src.utils.ElemButton;
import src.utils.GenericButton;
import src.view.menu.MenuPiattaformaSalvataggiView;
import src.view.menu.elemPanelView.ElemPanelView;
import src.view.menu.elemPanelView.ElemRimuovibilePanelView;

import javax.swing.*;

/**
 * Controller del menu della piattaforma che mostra i salvataggi di un gioco.
 */
public class MenuPiattaformaSalvataggiController extends AbstractMenuController {

    private static MenuPiattaformaSalvataggiController instance = null;

    public MenuPiattaformaSalvataggiController() {
        super();

        dao = SalvataggioDAO.getInstance();

        view = MenuPiattaformaSalvataggiView.getInstance();
        view.getIndietroButton().addActionListener(e -> indietroAction());
    }

    @Override
    public void init(Model elemMenuCorrente) {
        super.init(elemMenuCorrente);

        GenericButton nuovoElemButton = ((MenuPiattaformaSalvataggiView) view).getNuovoElemButton();

        if (nuovoElemButton.getActionListeners().length > 0)
            nuovoElemButton.removeActionListener(nuovoElemButton.getActionListeners()[0]);

        nuovoElemButton.addActionListener(e -> caricaNuovaPartitaAction(elemMenuCorrente));
    }

    public static MenuPiattaformaSalvataggiController getInstance() {
        if (instance == null)
            instance = new MenuPiattaformaSalvataggiController();
        return instance;
    }

    @Override
    public void addElemButtonListeners(ElemPanelView elemPanel) {
        elemPanel.getCaricaElemButton().addActionListener(e -> MenuPiattaformaMappeController.getInstance().init(((ElemButton) e.getSource()).getElem()));
        ((ElemRimuovibilePanelView) elemPanel).getRemoveElemButton().addActionListener(e -> removeElemAction(((JButton) e.getSource()).getName()));
    }

    private void caricaNuovaPartitaAction(Model model) {
        int num = dao.getAll(model).size() + 1;
        Salvataggio salvataggio = Salvataggio.nuovaPartita((Gioco) model, num);
        MenuPiattaformaMappeController.getInstance().init(salvataggio);
    }

    @Override
    protected void indietroAction() {
        MenuPiattaformaGiochiController.getInstance().init(null);
    }

}