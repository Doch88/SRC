package src.controller.editor;

import src.model.editor.Cella;
import src.model.editor.Editor;
import src.model.editor.SpriteEditor;
import src.utils.Settings;
import src.utils.Utils;
import src.view.editor.EditorSidebarDestraView;
import src.view.editor.FrameSelezionaDropView;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller della sidebar destra contenente le proprietà degli sprite.
 * Gestisce quindi la modifica di tali proprietà.
 */
public class EditorSidebarDestraController extends AbstractEditorController {

    private static EditorSidebarDestraController instance = null;

    private final EditorSidebarDestraView sidebarDestraView;
    private final FrameSelezionaDropView selezionaDropView;

    private EditorSidebarDestraController() {
        super();
        view = EditorSidebarDestraView.getInstance();
        selezionaDropView = FrameSelezionaDropView.getInstance();
        sidebarDestraView = (EditorSidebarDestraView) view;

        sidebarDestraView.getModificaProprietaButton().addActionListener(e -> modificaProprietaSpriteAction(sidebarDestraView.getTextFields()));
        sidebarDestraView.getInserisciInMappaButton().addActionListener(e -> inserisciInMappaAction());
        sidebarDestraView.getWaypoint1Button().addActionListener(e -> cambiaModalitaAction(Editor.MOD_WAYPOINT1));
        sidebarDestraView.getWaypoint2Button().addActionListener(e -> cambiaModalitaAction(Editor.MOD_WAYPOINT2));
        sidebarDestraView.getAssociaButton().addActionListener(e -> cambiaModalitaAction(Editor.MOD_ASSOCIA_PORTALE));
        sidebarDestraView.getCercaImmagine().addActionListener(e ->
                cercaImmagineAction(sidebarDestraView.getCercaImmagine().getName(),
                        sidebarDestraView.getSortedSelectedFiles()));
        sidebarDestraView.getInserisciAnimazioneStaticaButton().addActionListener(e -> visualizzaFinestraCercaImmagineAction("static"));
        sidebarDestraView.getInserisciAnimazioneMovimentoButton().addActionListener(e -> visualizzaFinestraCercaImmagineAction("movimento"));
        sidebarDestraView.getInserisciAnimazioneSaltoButton().addActionListener(e -> visualizzaFinestraCercaImmagineAction("salto"));
        sidebarDestraView.getInserisciAnimazioneSparoButton().addActionListener(e -> visualizzaFinestraCercaImmagineAction("sparo"));
        sidebarDestraView.getInserisciAnimazioneColpitoButton().addActionListener(e -> visualizzaFinestraCercaImmagineAction("colpito"));
        sidebarDestraView.getInserisciOggettiDroppatiButton().addActionListener(e -> inserisciOggettiDroppatiAction());
        sidebarDestraView.getEliminaSpriteDatabaseButton().addActionListener(e -> eliminaSpriteDatabaseAction());

    }

    public static EditorSidebarDestraController getInstance() {
        if (instance == null)
            instance = new EditorSidebarDestraController();
        return instance;
    }

    /**
     * @param textFields
     */
    private void modificaProprietaSpriteAction(HashMap<String, JTextField> textFields) {
        HashMap<String, Object> parametriSprite = new HashMap<>();
        for (String key : textFields.keySet())
            parametriSprite.put(key, textFields.get(key).getText());

        editor.getSpriteSelezionato().modificaSprite(parametriSprite);
        updateView();
    }

    /**
     * inserisce lo sprite selezionato nella mappa posizionandolo nella prima cella libera,
     * quindi init la view dell'editor
     */
    private void inserisciInMappaAction() {
        for (Cella cella : editor.getListaCelle())
            if (cella.getContenuto() == null) {
                editor.posizionaSpriteSelezionato(cella);
                break;
            }
        updateAll();
    }

    /**
     * apre la finestra che permette la scelta di un'immagine da file
     *
     * @param nome definisce il tipo di immagine che si sceglie
     *             (mappa, punto di inizio livello, punto di fine livello)
     */
    private void visualizzaFinestraCercaImmagineAction(String nome) {
        sidebarDestraView.cercaImmagine(nome);
    }

    private void eliminaSpriteDatabaseAction() {
        editor.eliminaSpriteDalDatabase();
    }

    /**
     * Sposta e rinomina, nella maniera opportuna, gli sprite selezionati dal frame di ricerca immagini
     *
     * @param nome  tipologia di immagine da selezionare
     * @param files file selezionati, che verranno copiati e rinominati
     */
    private void cercaImmagineAction(String nome, ArrayList<File> files) {
        if (!files.isEmpty()) {
            try {
                if (files.size() > 0) {
                    String partNome = "_" + nome;
                    if (nome.equals("movimento"))
                        partNome = "";
                    //Elimina prima tutti i file relativi alla precedente animazione
                    SpriteEditor spriteEditor = editor.getSpriteSelezionato();
                    String cartella = Settings.CARTELLA_SPRITES + "\\" + spriteEditor.getNome() + (nome.equals("movimento") ? "" : "\\" + nome);
                    for (int i = 1; Files.deleteIfExists(Utils.getImmagineDaPercorso(cartella, spriteEditor.getNome() + partNome + "_" + i)); i++)
                        ;
                    //Successivamente aggiunge i file della nuova animazione
                    for (int i = 0; i < files.size(); i++)
                        Utils.copiaImmagine(files.get(i).getPath(), cartella, spriteEditor.getNome() + partNome + "_" + (i + 1));
                    if (nome.equals("static")) {
                        cartella = Settings.CARTELLA_SPRITES + "\\" + spriteEditor.getNome();
                        Files.deleteIfExists(Utils.getImmagineDaPercorso(cartella, spriteEditor.getNome()));
                        Utils.copiaImmagine(files.get(0).getPath(), cartella, spriteEditor.getNome());
                        editor.updateImages();
                    }

                }

            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Apre la view che permette di cambiare i drop di un determinato sprite
     */
    private void inserisciOggettiDroppatiAction() {
        selezionaDropView.init(editor.getSpriteSelezionato());
    }
}