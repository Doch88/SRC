package src.view.editor;

import src.model.editor.Editor;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * View della barra strumenti nell'editor.
 * Visualizza tutti i bottoni relativi alle varie modalità.
 */
public class EditorBarraStrumentiView extends AbstractEditorView {

    private static EditorBarraStrumentiView instance;

    private final JButton posizionaButton;
    private final JButton duplicaButton;
    private final JButton creaButton;
    private final JButton importaButton;
    private final JButton eliminaButton;
    private final JButton avviaMappaButton;
    private final JButton indietroButton;

    public EditorBarraStrumentiView() {
        super();

        posizionaButton = new JButton(Utils.getText("place_button"));
        duplicaButton = new JButton(Utils.getText("clone_button"));
        creaButton = new JButton(Utils.getText("create_button"));
        importaButton = new JButton(Utils.getText("import_button"));
        eliminaButton = new JButton(Utils.getText("delete_button"));
        avviaMappaButton = new JButton(Utils.getText("start_button"));
        indietroButton = new JButton(Utils.getText("back_button"));

        this.add(posizionaButton);
        this.add(creaButton);
        this.add(importaButton);
        this.add(duplicaButton);
        this.add(eliminaButton);
        this.add(avviaMappaButton);
        this.add(indietroButton);
    }

    public static EditorBarraStrumentiView getInstance() {
        if (instance == null)
            instance = new EditorBarraStrumentiView();
        return instance;
    }

    @Override
    public void init() {
    }

    @Override
    public void update() {
        posizionaButton.setBackground(Color.WHITE);
        duplicaButton.setBackground(Color.WHITE);
        creaButton.setBackground(Color.WHITE);
        importaButton.setBackground(Color.WHITE);
        eliminaButton.setBackground(Color.WHITE);
        avviaMappaButton.setBackground(Color.WHITE);
        indietroButton.setBackground(Color.WHITE);

        int modalitaAttuale = editor.getModalitaAttuale();

        switch (modalitaAttuale) {
            case Editor.MOD_POSIZIONAMENTO:
                posizionaButton.setBackground(Color.BLUE);
                break;
            case Editor.MOD_CREAZIONE:
                creaButton.setBackground(Color.BLUE);
                break;
            case Editor.MOD_IMPORTAZIONE:
                importaButton.setBackground(Color.BLUE);
                break;
            case Editor.MOD_ELIMINAZIONE:
                eliminaButton.setBackground(Color.BLUE);
                break;
        }
    }

    /**
     * Mostra un messaggio di errore per sprite già esistenti
     */
    public void messaggioErroreSpriteEsistente() {
        JOptionPane.showMessageDialog(this,
                Utils.getText("error") + ": " + Utils.getText("sprite_already_existing"),
                Utils.getText("error"), JOptionPane.ERROR_MESSAGE);
    }

    public JButton getPosizionaButton() {
        return posizionaButton;
    }

    public JButton getDuplicaButton() {
        return duplicaButton;
    }

    public JButton getCreaButton() {
        return creaButton;
    }

    public JButton getImportaButton() {
        return importaButton;
    }

    public JButton getEliminaButton() {
        return eliminaButton;
    }

    public JButton getAvviaMappaButton() {
        return avviaMappaButton;
    }

    public JButton getIndietroButton() {
        return indietroButton;
    }

}