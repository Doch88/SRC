package src.utils;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

/**
 * Classe utilizzata per le caselle di testo che accettano solo valri interi
 */
public class NumberTextField extends JFormattedTextField {

    /**
     * Costruttore della casella, inizializza in modo corretto impostando come valore accettati solo interi
     *
     * @param text il numero inizialmente visualizzato
     */
    public NumberTextField(int text) {
        super();
        NumberFormat intFormat = NumberFormat.getIntegerInstance();

        NumberFormatter numberFormatter = new NumberFormatter(intFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0);

        this.setFormatter(numberFormatter);
        this.setText("" + text);
    }

}