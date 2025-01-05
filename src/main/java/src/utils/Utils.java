package src.utils;

import src.MainController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Classe contenente metodi statici di utilità generale.
 */
public class Utils {

    private static ResourceBundle bundle = null;
    /**
     * Specchia un'immagine rispetto all'azzd verticale
     *
     * @param immagine immagine da specchiare
     * @return immagine specchiata
     */
    public static Image mirrorImage(Image immagine) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-immagine.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        immagine = op.filter((BufferedImage) immagine, null);
        return immagine;
    }

    /**
     * Ottiene il percorso di un file all'interno della cartella immagini
     *
     * @param nomeCartella nome della cartella, o della classificazione (es. mappe, sprite, etc) dell'immagine
     * @param nomeFile     il nome del file
     * @return la stringa contenente il percorso
     */
    public static String getPercorsoImmagine(String nomeCartella, String nomeFile) {
        return Paths.get("rsc", "img", nomeCartella, nomeFile + ".png").toString();
    }

    public static Path getImmagineDaPercorso(String nomeCartella, String nomeFile) {
        return Paths.get(getPercorsoImmagine(nomeCartella, nomeFile));
    }

    /**
     * Crea una nuova immagine prendendola in input dalla cartella delle immagini
     *
     * @param nomeCartella nome della cartella, o della classificazione (es. mappe, sprite, etc) dell'immagine
     * @param nomeFile     il nome dell'immagine
     * @return una istanza di Image contenente l'immagine
     */
    public static Image getBufferedImage(String nomeCartella, String nomeFile) {
        try {
            File file = new File(getPercorsoImmagine(nomeCartella, nomeFile));
            if (file.exists())
                return ImageIO.read(file.toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Rinomina un immagine
     *
     * @param nomeCartella    nome della cartella, o della classificazione (es. mappe, sprite, etc) dell'immagine
     * @param nomeFileVecchio
     * @param nomeFileNuovo
     */
    public static void rinominaImmagine(String nomeCartella, String nomeFileVecchio, String nomeFileNuovo) {
        try {
            Files.move(getImmagineDaPercorso(nomeCartella, nomeFileVecchio), getImmagineDaPercorso(nomeCartella, nomeFileNuovo), REPLACE_EXISTING);
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Copia una determina immagine, fuori dalla directory del programma, all'interno della directory delle immagini
     *
     * @param urlImmagine  immagine da copiare, esterna al programma
     * @param nomeCartella nome della cartella, o della classificazione (es. mappe, sprite, etc) dell'immagine
     * @param nomeFile     il nome dell'immagine
     */
    public static void copiaImmagine(String urlImmagine, String nomeCartella, String nomeFile) {
        try {
            File directory = new File(Paths.get("rsc", "img", nomeCartella).toString());
            if (!directory.exists())
                directory.mkdirs();
            CopyOption[] options = new CopyOption[]{
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES
            };
            Files.copy(Paths.get(urlImmagine), getImmagineDaPercorso(nomeCartella, nomeFile), options);
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean fileExists(String url) {
        return new File(url).exists();
    }

    /**
     * Aggiorna il contenuto della view principale con il Container scelto
     *
     * @param ContentPane un JPanel contenente l'interfaccia da visualizzare
     */
    public static void updateView(Container ContentPane) {
        MainController.getInstance().getFrame().update(ContentPane);
    }

    public static void raiseError(String error) {
        JOptionPane.showMessageDialog(MainController.getInstance().getFrame(),
                "Errore: " + error,
                "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Torna al menu principale
     */
    public static void setMainView() {
        updateView(MainController.getInstance().getView());
    }

    /**
     * Dà il focus alla finestra del programma, permette ai vari listener di funzionare correttamente
     */
    public static void focusWindow() {
        MainController.getInstance().getFrame().requestFocusInWindow();
    }

    /**
     * Aggiunge un KeyListener alla finestra, permettendo quindi di poter ricevere correttamente gli input da tastiera
     *
     * @param keyListener il KeyListener da aggiungere alla finestra
     */
    public static void addKeyListener(KeyListener keyListener) {
        MainController.getInstance().getFrame().addKeyListener(keyListener);
    }

    /**
     * Rimuove un determinato KeyListener dalla finestra
     *
     * @param keyListener il KeyListener da rimuovere
     */
    public static void removeKeyListener(KeyListener keyListener) {
        MainController.getInstance().getFrame().removeKeyListener(keyListener);
    }

    public static String getText(String textName) {
        if (bundle == null) {
            try {
                bundle = ResourceBundle.getBundle("locales.language", Locale.getDefault());
            }
            catch (Exception e) {
                bundle = ResourceBundle.getBundle("locales.language", new Locale("en", "US"));
            }
        }
        try {
            return bundle.getString(textName.replaceAll(" ", "_"));
        }
        catch(Exception e) {
            return textName;
        }
    }

}
