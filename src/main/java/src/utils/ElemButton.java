package src.utils;

import src.model.Model;

import javax.swing.*;

/**
 * Classe che estende JButton e ci servirà per permettere di personalizzare i bottoni in ogni menù
 * In particolare ElemButton ci permette di associare un Model al bottone stesso, così da rendere la scelta del bottone cliccato
 * strettamente dipendente alla scelta del model associato.
 */
public class ElemButton extends GenericButton {

    private final Model elem;

    public ElemButton(Model elem) {
        super(elem.getNome(), elem.getNome(), true, new ImageIcon(Utils.getPercorsoImmagine(Settings.CARTELLA_GIOCHI, elem.getNome())));
        this.elem = elem;
    }

    public Model getElem() {
        return elem;
    }
}
