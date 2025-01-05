package src.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Classe che estende JButton e ci servirà per permettere di personalizzare i bottoni in ogni menù
 */
public class GenericButton extends JButton {

    private String nome;
    private String testo;
    private ImageIcon immagine;

    public GenericButton(String nome, String testo, boolean iconButton, ImageIcon immagine) {
        super();
        if (nome != null)
            this.setName(nome);
        if (testo != null && (immagine == null || immagine.getIconWidth() < 0))
            this.setText(Utils.getText(testo));
        Dimension dm;
        if (iconButton) {
            this.setBackground(new Color(0x091DE));
            dm = new Dimension(200, 200);
            if (immagine != null) {
                immagine.setImage(immagine.getImage().getScaledInstance((int) dm.getWidth(), (int) dm.getHeight(), Image.SCALE_DEFAULT));
                this.setIcon(immagine);
            }
        } else {
            this.setBackground(new Color(0x0009E));
            dm = new Dimension(200, 75);
        }
        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.setMaximumSize(dm);
        this.setMinimumSize(dm);
        this.setPreferredSize(dm);
        this.setFont(new Font("Century gothic", Font.PLAIN, 16));
        this.setForeground(Color.white);
        this.setOpaque(true);
    }

    public GenericButton(String nome) {
        this(nome, nome, false, null);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public ImageIcon getImmagine() {
        return immagine;
    }

    public void setImmagine(ImageIcon immagine) {
        this.immagine = immagine;
    }

}
