package src.view.menu.nuovoElemView;

import src.utils.GenericButton;
import src.utils.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Classe astratta che estende JFrame e che verrà estesa dalle due classi che permettono di creare nuovi elementi quali
 * mappe e giochi.
 */
public abstract class AbstractNuovoElemView extends JFrame {

    protected JPanel mainPanel;
    private final JPanel nomeElemPanel;
    private final JPanel immaginePanel;

    private final JTextField nomeElemText;
    private final JTextField urlImmagineText;
    protected JFileChooser immagine;

    private final GenericButton confermaButton;
    private final GenericButton cercaImmagineButton;

    /**
     * Costruttore che inizializza il JFrame on gli elementi comuni ai frame di creazione di elementi
     */
    public AbstractNuovoElemView() {
        super();
        init();

        mainPanel = new JPanel(new GridLayout(0, 1));

        nomeElemPanel = new JPanel();
        immaginePanel = new JPanel();

        JLabel nomeElemLabel = new JLabel(Utils.getText("name_label") + ": ");
        JLabel immagineLabel = new JLabel(Utils.getText("image_label") + ": ");
        nomeElemText = new JTextField(30);
        urlImmagineText = new JTextField(30);

        confermaButton = new GenericButton("confirm_button");
        cercaImmagineButton = new GenericButton("find_image");

        immagine = new JFileChooser();
        immagine.addChoosableFileFilter(new FileNameExtensionFilter(Utils.getText("image_filter_desc"), "png", "jpg", "jpeg", "gif"));

        nomeElemPanel.add(nomeElemLabel);
        nomeElemPanel.add(nomeElemText);

        immaginePanel.add(immagineLabel);
        immaginePanel.add(urlImmagineText);
        immaginePanel.add(cercaImmagineButton);

        mainPanel.add(nomeElemPanel);
        mainPanel.add(immaginePanel);

        mainPanel.add(confermaButton);

        this.add(mainPanel);
        this.pack();
    }

    /**
     * Chiude il frame
     */
    public void close() {
        this.dispose();
    }

    /**
     * Inizializza il frame
     */
    public abstract void init();

    /**
     * Apre la finestra che permette di scegliere l'immagine
     */
    public void cercaImmagine() {
        immagine.showOpenDialog(this);
        File a = immagine.getSelectedFile();
        if (a != null)
            urlImmagineText.setText(a.getPath());
    }

    /**
     * Mostra un messaggio di errore relativo all'elemento da aggiungere
     */
    public abstract void mostraMessaggioErrore();

    /**
     * Mostra un messaggio di successo nella creazione dell'elemento
     */
    public abstract void mostraMessaggioSuccesso();

    public String getNomeElemText() {
        return nomeElemText.getText();
    }

    public String getUrlImmagineText() {
        return urlImmagineText.getText();
    }

    public GenericButton getConfermaButton() {
        return confermaButton;
    }

    public GenericButton getCercaImmagineButton() {
        return cercaImmagineButton;
    }
}