package src.view.menu.info;

import src.model.Mappa;
import src.model.Salvataggio;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Contiene le informazioni relative ad una con tutti i collezionabili raccolti e le varie informazioni relative
 * alla posizione attuale nel salvataggio.
 */
public class InfoMappaPiattaforma extends AbstractInfoModificabile {

    public InfoMappaPiattaforma(Salvataggio salvataggioCorrente, Mappa mappa) {
        super();
        this.setOpaque(false);

        if (salvataggioCorrente.getPosizioneMappaAttuale() < (mappa).getPosizione()) {
            this.setLayout(new GridLayout(1, 0));
            this.add(new JLabel(Utils.getText("locked_map")));
        } else if (salvataggioCorrente.getPosizioneMappaAttuale() > (mappa).getPosizione()) {
            this.setLayout(new GridLayout(salvataggioCorrente.getCollezionabili(mappa.getPosizione()).size() + 1, 0));
            this.add(new JLabel(Utils.getText("collectables") + ":"));

            if (salvataggioCorrente.getCollezionabili(mappa.getPosizione()) != null) {
                for (Map.Entry<String, Integer> entry : salvataggioCorrente.getCollezionabili(mappa.getPosizione()).entrySet()) {
                    this.add(new JLabel(Utils.getText(entry.getKey()) + ": " + entry.getValue()));
                }
            } else
                this.add(new JLabel(Utils.getText("no_collectables")));
        } else {
            this.setLayout(new GridLayout(1, 0));
            this.add(new JLabel(Utils.getText("current_map")));
        }
    }

}