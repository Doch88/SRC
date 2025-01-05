package src.model;

import src.utils.Utils;

/**
 * Contiene tutte le informazioni generali relativi ad una mappa
 */
public class Mappa extends Model {

    private final Gioco gioco;
    private final int posizione;
    private int numCelleAsse;
    private int cellaInizioX = 5, cellaFineX = 5;
    private int cellaInizioY = 1, cellaFineY = 1;
    private int dimCella = 100;

    public Mappa(String nome, Gioco gioco, int posizione) {
        this.nome = nome;
        this.gioco = gioco;
        this.posizione = posizione;
    }

    public Mappa(String nome, Gioco gioco, int posizione, int numCelleAsse,
                 int cellaInizioX, int cellaFineX, int cellaInizioY, int cellaFineY, int dimCella) {
        this.nome = nome;
        this.gioco = gioco;
        this.posizione = posizione;

        this.numCelleAsse = numCelleAsse;
        this.cellaInizioX = cellaInizioX;
        this.cellaFineX = cellaFineX;
        this.cellaInizioY = cellaInizioY;
        this.cellaFineY = cellaFineY;
        this.dimCella = dimCella;
    }

    public Gioco getGioco() {
        return gioco;
    }

    public int getPosizione() {
        return posizione;
    }

    public int getNumCelleAsse() {
        return numCelleAsse;
    }

    /**
     * Imposta la quantità di celle di lunghezza e altezza, controlla prima se la nuova quantità va in conflitto con la posizione della
     * cella iniziale e di quella finale.
     *
     * @param numCelleAsse
     */
    public void setNumCelleAsse(int numCelleAsse) {
        if (numCelleAsse < cellaInizioX || numCelleAsse < cellaInizioY) {
            Utils.raiseError(Utils.getText("initial_cell_over_limits"));
            return;
        }
        if (numCelleAsse < cellaFineX || numCelleAsse < cellaFineX) {
            Utils.raiseError(Utils.getText("end_cell_over_limits"));
            return;
        }
        this.numCelleAsse = numCelleAsse;
    }

    public int getCellaInizioX() {
        return cellaInizioX;
    }

    public int getCellaFineX() {
        return cellaFineX;
    }

    public int getCellaInizioY() {
        return cellaInizioY;
    }

    public int getCellaFineY() {
        return cellaFineY;
    }

    public int getDimCella() {
        return dimCella;
    }

    public void setDimCella(int dimCella) {
        this.dimCella = dimCella;
    }
}
