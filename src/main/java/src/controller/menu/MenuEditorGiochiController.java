package src.controller.menu;

import src.model.DAO.GiocoDAO;
import src.model.Gioco;
import src.model.piattaforma.Sprite.Player;
import src.utils.ElemButton;
import src.utils.Settings;
import src.utils.Utils;
import src.view.menu.MenuEditorGiochiView;
import src.view.menu.elemPanelView.ElemModificabilePanelView;
import src.view.menu.elemPanelView.ElemPanelView;
import src.view.menu.nuovoElemView.NuovoGiocoView;

import javax.swing.*;
import java.awt.*;

/**
 * controller del menu dell'editor, che permette di scegliere i giochi e le mappe da modificare
 */
public class MenuEditorGiochiController extends AbstractMenuController {

    private static MenuEditorGiochiController instance = null;

    /**
     * ottiene le istanze delle view e del controller dell'editor, quindi crea i listener per i bottoni
     */
    private MenuEditorGiochiController() {
        super();

        dao = GiocoDAO.getInstance();

        nuovoElemView = NuovoGiocoView.getInstance();
        nuovoElemView.getConfermaButton().addActionListener(e -> creaNuovoGiocoAction());
        nuovoElemView.getCercaImmagineButton().addActionListener(e -> nuovoElemView.cercaImmagine());
        ((NuovoGiocoView) nuovoElemView).getCercaImmaginePlayerButton().addActionListener(e -> ((NuovoGiocoView) nuovoElemView).cercaImmaginePlayer());

        MenuEditorGiochiView menuEditorGiochiView = MenuEditorGiochiView.getInstance();
        menuEditorGiochiView.getNuovoElemButton().addActionListener(e -> nuovoElemView.setVisible(true));
        menuEditorGiochiView.getIndietroButton().addActionListener(e -> indietroAction());
        view = menuEditorGiochiView;
    }

    public static MenuEditorGiochiController getInstance() {
        if (instance == null)
            instance = new MenuEditorGiochiController();
        return instance;
    }

    @Override
    protected void addElemButtonListeners(ElemPanelView giocoPanel) {
        ElemModificabilePanelView giocoEditorPanel = (ElemModificabilePanelView) giocoPanel;
        giocoEditorPanel.getCaricaElemButton().addActionListener(e -> MenuEditorMappeController.getInstance().init(((ElemButton) e.getSource()).getElem()));
        giocoEditorPanel.getRemoveElemButton().addActionListener(e -> removeElemAction(((JButton) e.getSource()).getName()));
        giocoEditorPanel.getConfermaButton().addActionListener(e -> modificaGioco((Gioco) giocoPanel.getElem(),
                Integer.parseInt(giocoEditorPanel.getValueOf(Utils.getText("framerate"))),
                Integer.parseInt(giocoEditorPanel.getValueOf(Utils.getText("health"))),
                Integer.parseInt(giocoEditorPanel.getValueOf(Utils.getText("attack"))),
                Integer.parseInt(giocoEditorPanel.getValueOf(Utils.getText("cooldown"))),
                Integer.parseInt(giocoEditorPanel.getValueOf(Utils.getText("attack_frequency"))),
                Integer.parseInt(giocoEditorPanel.getValueOf(Utils.getText("speed"))),
                giocoEditorPanel.getValueOf(Utils.getText("bullet"))));
    }

    /**
     * Azione richiamata dal listener del bottone di creazione nuovo gioco, apre
     */
    private void creaNuovoGiocoAction() {
        String nomeGioco = nuovoElemView.getNomeElemText();
        String urlImmaginePlayer = ((NuovoGiocoView) nuovoElemView).getUrlImmaginePlayerText();

        if (!nomeGioco.isEmpty() &&
                !urlImmaginePlayer.isEmpty()
                && Utils.fileExists(urlImmaginePlayer)) {

            nuovoElemView.close();

            String urlImmagine = nuovoElemView.getUrlImmagineText();

            Gioco nuovoGioco = new Gioco(nomeGioco, 60, new Player(nomeGioco + "Player"));

            if (dao.exists(nuovoGioco))
                nuovoElemView.mostraMessaggioErrore();
            else {
                Utils.copiaImmagine(urlImmagine, Settings.CARTELLA_GIOCHI, nomeGioco);
                Utils.copiaImmagine(urlImmaginePlayer, Settings.CARTELLA_SPRITES + "\\" + nomeGioco + "Player", nomeGioco + "Player");
                dao.add(nuovoGioco);
                nuovoElemView.mostraMessaggioSuccesso();
                this.update();
            }
        } else {
            JOptionPane.showMessageDialog(nuovoElemView,
                    Utils.getText("mandatory_game_name_and_player_image"),
                    Utils.getText("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Modifica le informazioni di un gioco dall'editor
     */
    private void modificaGioco(Gioco giocoVecchio, int nuovoFramerate, int nuovaSalute, int nuovoAttacco, int nuovoRiposo, int nuovoRiposoSparo, int velocita, String nomeProiettile) {
        Image immagine = Utils.getBufferedImage(Settings.CARTELLA_SPRITES, giocoVecchio.getPlayer().getNome());
        Player player = new Player(giocoVecchio.getPlayer().getNome(), immagine, nomeProiettile, nuovaSalute, nuovoRiposo, nuovoRiposoSparo, nuovoAttacco, velocita);
        Gioco gioco = new Gioco(giocoVecchio.getNome(), nuovoFramerate, player);
        dao.updateByNome(giocoVecchio.getNome(), gioco);
        this.update();
    }

    @Override
    protected void indietroAction() {
        Utils.setMainView();
    }

}