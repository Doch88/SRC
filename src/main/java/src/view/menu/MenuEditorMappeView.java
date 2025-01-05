package src.view.menu;

import src.model.Gioco;
import src.model.Mappa;
import src.model.Model;
import src.utils.GenericButton;
import src.view.menu.elemPanelView.ElemModificabilePanelView;
import src.view.menu.elemPanelView.ElemPanelView;
import src.view.menu.info.InfoGiocoEditor;
import src.view.menu.info.InfoMappaEditor;
import src.utils.Utils;

import javax.swing.*;

/**
 * View del menu dell'editor contenente le mappe di un determinato gioco.
 * Da qui potremo modificare delle informazioni di base relative alle mappe e alcuni dettagli relativi al gioco di cui fanno parte le mappe.
 */
public class MenuEditorMappeView extends AbstractMenuView {

    private static MenuEditorMappeView instance = null;

    private final GenericButton nuovoElemButton;
    private InfoGiocoEditor infoGiocoEditor;

    public MenuEditorMappeView() {
        super();
        nuovoElemButton = new GenericButton("editor_new_map");
    }

    public static MenuEditorMappeView getInstance() {
        if (instance == null)
            instance = new MenuEditorMappeView();
        return instance;
    }

    @Override
    public JPanel initHeaderPanel(Model model) {
        Gioco gioco = (Gioco) model;
        HeaderView headerView = new HeaderView(Utils.getText("editor_maps_menu"));
        infoGiocoEditor = new InfoGiocoEditor(gioco);
        headerView.add(infoGiocoEditor);
        headerView.add(nuovoElemButton);
        return headerView;
    }

    @Override
    protected ElemPanelView getElemPanelView(Model elemMenuCorrente, Model mappa) {
        ElemModificabilePanelView mappaEditorPanelView = new ElemModificabilePanelView(mappa);
        mappaEditorPanelView.setInfoPanel(new InfoMappaEditor((Mappa) mappa));
        return mappaEditorPanelView;
    }

    public GenericButton getNuovoElemButton() {
        return nuovoElemButton;
    }

    public InfoGiocoEditor getInfoGioco() {
        return infoGiocoEditor;
    }
}
