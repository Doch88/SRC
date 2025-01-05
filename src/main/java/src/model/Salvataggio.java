package src.model;

import src.model.DAO.SalvataggioDAO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe che memorizza i dati relativi ad un salvataggio di un gioco
 */
public class Salvataggio extends Model {

    private Gioco gioco;
    private final int num;
    private int punteggio = 0;
    private int posizioneMappaAttuale;
    private final ArrayList<HashMap<String, Integer>> collezionabili;

    /**
     * Costruttore del salvataggio, crea un salvataggio basandosi sulle informazioni passsate
     *
     * @param gioco                 il gioco relativo al salvataggio
     * @param posizioneMappaAttuale la posizione della mappa a cui si è arrivati
     * @param num                   il numero del salvataggio, identificativo insieme al gioco di appartenenza
     * @param punteggio             il punteggio ottenuto nel salvataggio
     * @param collezionabili        la lista dei collezionabili raccolti nelle diverse mappe
     */
    public Salvataggio(Gioco gioco, int posizioneMappaAttuale, int num, int punteggio,
                       ArrayList<HashMap<String, Integer>> collezionabili) {
        this.nome = "" + num;
        this.gioco = gioco;
        this.posizioneMappaAttuale = posizioneMappaAttuale;
        this.num = num;
        this.punteggio = punteggio;
        this.collezionabili = collezionabili;
        if (collezionabili == null)
            collezionabili = new ArrayList<>();
        if (collezionabili.isEmpty())
            collezionabili.add(new HashMap<>());

    }

    /**
     * Crea un nuovo salvataggio da zero, basandosi sul gioco e sul numero dell'ultimo salvataggio
     *
     * @param gioco il gioco di cui creare un nuovo salvataggio
     * @param num   il numero dell'ultimo salvataggio
     * @return il salvataggio inizializzato con tutti i valori di partenza
     */
    public static Salvataggio nuovaPartita(Gioco gioco, int num) {
        Salvataggio salvataggio = new Salvataggio(gioco, 1, num, 0, new ArrayList<>());
        salvataggio.getCollezionabili().add(new HashMap<>());
        SalvataggioDAO.getInstance().add(salvataggio);
        return salvataggio;
    }

    /**
     * Ottiene una mappa del gioco relativo al salvataggio
     *
     * @param pos la posizione della mappa da ottenere
     * @return la mappa richiesta
     */
    public Mappa getMappa(int pos) {
        return SalvataggioDAO.getInstance().getMappaCorrente(gioco, pos);
    }

    public Gioco getGioco() {
        return gioco;
    }

    public void setGioco(Gioco gioco) {
        this.gioco = gioco;
    }

    public void caricaPunteggio() {
        punteggio = SalvataggioDAO.getInstance().getPunteggio(getGioco(), num);
    }

    public int getNum() {
        return num;
    }

    /**
     * @return la posizione della mappa a cui si è arrivati
     */
    public int getPosizioneMappaAttuale() {
        return posizioneMappaAttuale;
    }

    /**
     * Imposta la posizione attuale dell'ultima mappa a cui si è arrivati
     *
     * @param posizioneMappaAttuale il numero della mappa attale
     */
    public void setPosizioneMappaAttuale(int posizioneMappaAttuale) {
        this.posizioneMappaAttuale = posizioneMappaAttuale;
        if (posizioneMappaAttuale > collezionabili.size()) {
            collezionabili.add(new HashMap<>());
        }
    }

    /**
     * Ritorna la lista di tutti i collezionabili raccolti in una determinata mappa
     *
     * @param mappa il numero della mappa da cui prendere i collezionabili
     * @return una HashMap in cui la chiave è il nome del collezionabile, il valore è il numero di tale collezionabile raccolto nella mappa
     */
    public HashMap<String, Integer> getCollezionabili(int mappa) {
        if (collezionabili.size() < mappa) return null;
        return collezionabili.get(mappa - 1);
    }

    /**
     * @return la lista di HashMap contenente tutti i collezionabili raccolti in tutte le mappe
     */
    public ArrayList<HashMap<String, Integer>> getCollezionabili() {
        return collezionabili;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    /**
     * Aggiunge un valore al punteggio
     *
     * @param value valore da aggiungere
     */
    public void addPunteggio(int value) {
        punteggio += value;
    }

}
