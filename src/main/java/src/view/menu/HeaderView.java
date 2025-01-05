package src.view.menu;

import src.utils.Utils;

import javax.swing.*;

/**
 * Un header è la parte sovrastante di un menu.
 * Contiene informazioni su quello che si sta visualizzando e, in alcune occasioni, permette delle modifiche all'oggetto
 * appena aperto.
 */
public class HeaderView extends JPanel {

    private JLabel titolo;

    public HeaderView(String titolo) {
        super();
        this.titolo = new JLabel(Utils.getText(titolo));
        this.add(this.titolo);
    }

    public void setTitolo(String titolo) {
        this.removeAll();
        this.titolo = new JLabel(Utils.getText(titolo));
        this.add(this.titolo);
        this.revalidate();
        this.repaint();
    }

}
