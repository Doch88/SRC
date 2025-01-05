package src.controller.menu;

import src.controller.editor.EditorController;
import src.model.DAO.GiocoDAO;
import src.model.DAO.MappaDAO;
import src.model.Gioco;
import src.model.Mappa;
import src.model.Model;
import src.model.piattaforma.Sprite.Player;
import src.utils.ElemButton;
import src.utils.Settings;
import src.utils.Utils;
import src.view.menu.MenuEditorMappeView;
import src.view.menu.elemPanelView.ElemModificabilePanelView;
import src.view.menu.elemPanelView.ElemPanelView;
import src.view.menu.info.InfoGiocoEditor;
import src.view.menu.nuovoElemView.NuovaMappaView;

import javax.swing.*;
import java.awt.*;

/**
 * controller del menu dell'editor, che permette di scegliere i giochi e le mappe da modificare
 */
public class MenuEditorMappeController extends AbstractMenuController {

    private static MenuEditorMappeController instance = null;

    /**
     * ottiene le istanze delle view e del controller dell'editor, quindi crea i listener per i bottoni
     */
    private MenuEditorMappeController() {
        super();

        dao = MappaDAO.getInstance();

        nuovoElemView = NuovaMappaView.getInstance();
        nuovoElemView.getConfermaButton().addActionListener(e -> creaNuovaMappaAction());
        nuovoElemView.getCercaImmagineButton().addActionListener(e -> nuovoElemView.cercaImmagine());

        MenuEditorMappeView menuEditorMappeView = MenuEditorMappeView.getInstance();
        menuEditorMappeView.getNuovoElemButton().addActionListener(e -> nuovoElemView.setVisible(true));
        menuEditorMappeView.getIndietroButton().addActionListener(e -> indietroAction());

        view = menuEditorMappeView;
    }

    @Override
    public void init(Model elemMenuCorrente) {
        super.init(elemMenuCorrente);

        InfoGiocoEditor infoGiocoEditor = ((MenuEditorMappeView) view).getInfoGioco();
        infoGiocoEditor.getConfermaButton()
                .addActionListener(e -> modificaGiocoAction(Integer.parseInt(infoGiocoEditor.getValueOf(Utils.getText("framerate"))),
                        Integer.parseInt(infoGiocoEditor.getValueOf(Utils.getText("health"))),
                        Integer.parseInt(infoGiocoEditor.getValueOf(Utils.getText("attack"))),
                        Integer.parseInt(infoGiocoEditor.getValueOf(Utils.getText("cooldown"))),
                        Integer.parseInt(infoGiocoEditor.getValueOf(Utils.getText("attack_frequency"))),
                        Integer.parseInt(infoGiocoEditor.getValueOf(Utils.getText("speed"))),
                        infoGiocoEditor.getValueOf(Utils.getText("bullet"))));
    }

    public static MenuEditorMappeController getInstance() {
        if (instance == null)
            instance = new MenuEditorMappeController();
        return instance;
    }

    @Override
    protected void addElemButtonListeners(ElemPanelView mappaPanel) {
        ElemModificabilePanelView mappaEditorPanel = (ElemModificabilePanelView) mappaPanel;
        mappaEditorPanel.getCaricaElemButton().addActionListener(e -> EditorController.getInstance().init((Mappa) ((ElemButton) e.getSource()).getElem()));
        mappaEditorPanel.getRemoveElemButton().addActionListener(e -> removeElemAction(((JButton) e.getSource()).getName()));
        mappaEditorPanel.getConfermaButton().addActionListener(e -> modificaMappa((Mappa) mappaPanel.getElem(),
                Integer.parseInt(mappaEditorPanel.getValueOf(Utils.getText("cell_size"))),
                Integer.parseInt(mappaEditorPanel.getValueOf(Utils.getText("num_cells")))));
    }

    private void creaNuovaMappaAction() {
        String nomeMappa = nuovoElemView.getNomeElemText();
        String urlImmagine = nuovoElemView.getUrlImmagineText();

        if (!nomeMappa.isEmpty()) {
            nuovoElemView.close();

            Mappa nuovaMappa = null;
            try {
                nuovaMappa = new Mappa(nomeMappa, (Gioco) this.getElemMenuCorrente(),
                        ((MappaDAO) dao).getAllByGioco((Gioco) this.getElemMenuCorrente()).size() + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dao.exists(nuovaMappa))
                nuovoElemView.mostraMessaggioErrore();
            else {
                Utils.copiaImmagine(urlImmagine, Settings.CARTELLA_MAPPE, nomeMappa);
                dao.add(nuovaMappa);
                nuovoElemView.mostraMessaggioSuccesso();
                this.update();
            }
        } else {
            JOptionPane.showMessageDialog(nuovoElemView,
                    Utils.getText("mandatory_map_name"),
                    Utils.getText("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificaGiocoAction(int nuovoFramerate, int nuovaSalute, int nuovoAttacco, int nuovoRiposo, int nuovoRiposoSparo, int velocita, String nomeProiettile) {
        Gioco giocoVecchio = (Gioco) this.getElemMenuCorrente();
        Image immagine = Utils.getBufferedImage(Settings.CARTELLA_SPRITES, giocoVecchio.getPlayer().getNome());
        Player player = new Player(giocoVecchio.getPlayer().getNome(), immagine, nomeProiettile, nuovaSalute, nuovoRiposo, nuovoRiposoSparo, nuovoAttacco, velocita);
        Gioco gioco = new Gioco(giocoVecchio.getNome(), nuovoFramerate, player);
        GiocoDAO.getInstance().updateByNome(giocoVecchio.getNome(), gioco);
        giocoVecchio.setPlayer(player);
        giocoVecchio.setFramerate(nuovoFramerate);
        this.update();
    }

    private void modificaMappa(Mappa mappa, int dimCella, int numeroCelle) {
        mappa.setDimCella(dimCella);
        mappa.setNumCelleAsse(numeroCelle);
        dao.updateByNome(mappa.getNome(), mappa);
        this.update();
    }


    @Override
    protected void indietroAction() {
        MenuEditorGiochiController.getInstance().init(null);
    }

}