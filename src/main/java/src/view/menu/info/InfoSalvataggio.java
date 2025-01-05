package src.view.menu.info;

import src.model.Salvataggio;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Contiene le informazioni relative ad un salvataggio e i progressi attuali.
 */
public class InfoSalvataggio extends AbstractInfoModificabile {

    public InfoSalvataggio(Salvataggio salvataggio) {
        super();
        this.setOpaque(false);
        this.setLayout(new GridLayout(0, 2));

        this.add(new JLabel(Utils.getText("current_level") + ": " + salvataggio.getPosizioneMappaAttuale()));
        this.add(new JLabel(Utils.getText("current_score") + ": " + salvataggio.getPunteggio()));

    }


}