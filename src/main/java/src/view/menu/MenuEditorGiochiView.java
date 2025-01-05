package src.view.menu;

import src.model.Gioco;
import src.model.Model;
import src.utils.GenericButton;
import src.view.menu.elemPanelView.ElemModificabilePanelView;
import src.view.menu.elemPanelView.ElemPanelView;
import src.view.menu.info.InfoGiocoEditor;
import src.utils.Utils;

import javax.swing.*;

/**
 * View dell'editor contenente i giochi.
 * Insieme ai giochi verranno descritte alcune informazioni che saranno modificabili, come framerate e caratteristiche del player.
 */
public class MenuEditorGiochiView extends AbstractMenuView {

    private static MenuEditorGiochiView instance = null;

    private final GenericButton nuovoElemButton;

    private MenuEditorGiochiView() {
        super();
        nuovoElemButton = new GenericButton("editor_new_game");
    }

    public static MenuEditorGiochiView getInstance() {
        if (instance == null)
            instance = new MenuEditorGiochiView();
        return instance;
    }

    @Override
    public JPanel initHeaderPanel(Model model) {
        HeaderView headerView = new HeaderView(Utils.getText("editor_games_menu"));
        headerView.add(nuovoElemButton);
        return headerView;
    }

    @Override
    protected ElemPanelView getElemPanelView(Model elemMenuCorrente, Model gioco) {
        ElemModificabilePanelView giocoEditorPanelView = new ElemModificabilePanelView(gioco);
        giocoEditorPanelView.setInfoPanel(new InfoGiocoEditor((Gioco) gioco));
        return giocoEditorPanelView;
    }

    public GenericButton getNuovoElemButton() {
        return nuovoElemButton;
    }
}