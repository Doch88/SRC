package src.model.DAO;

import src.model.Gioco;
import src.model.Mappa;
import src.model.Model;
import src.model.Salvataggio;
import src.utils.Database;
import src.utils.ResultSQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO dei salvataggi, crea, aggiorna, ottiene ed elimina salvataggi dal database.
 */
public class SalvataggioDAO implements DAO<Salvataggio> {

    private static SalvataggioDAO instance = null;
    private final Database db = Database.getInstance();

    public static SalvataggioDAO getInstance() {
        if (instance == null)
            instance = new SalvataggioDAO();
        return instance;
    }

    /**
     * Inserisce un salvataggio nel database
     *
     * @param salvataggio il salvataggio da aggiungere
     */
    @Override
    public void add(Salvataggio salvataggio) {
        db.query("INSERT INTO Salvataggio(gioco, num, mappa) " +
                "values ('" + salvataggio.getGioco().getNome() + "'," + salvataggio.getNum() + "," + salvataggio.getPosizioneMappaAttuale() + ")", true);
    }

    /**
     * Aggiorna un salvataggio nel database
     *
     * @param elem il salvataggio da aggiornare
     */
    @Override
    public boolean updateByNome(String nome, Salvataggio elem) {
        if (exists(elem)) {
            db.query("UPDATE Salvataggio SET mappa = " + elem.getPosizioneMappaAttuale() + ", " +
                    "punteggio = " + elem.getPunteggio() +
                    "WHERE gioco = '" + elem.getGioco().getNome() + "' AND " +
                    "num = " + elem.getNum(), true);
            int i = 1;
            for (HashMap<String, Integer> coll : elem.getCollezionabili()) {
                for (Map.Entry<String, Integer> entry : coll.entrySet()) {
                    addCollezionabile(elem, i, entry.getKey(), entry.getValue());
                }
                i++;
            }
            return true;
        } else return false;
    }

    @Override
    public void remove(Salvataggio elem) {
        removeByNome(elem.getNome());
    }

    @Override
    public void removeByNome(String nome, Model... model) {
        db.query("DELETE FROM Salvataggio " +
                "WHERE num = " + nome + " " +
                "AND gioco = '" + model[0].getNome() + "'", true);
    }

    /**
     * Controlla se esiste un salvataggio
     *
     * @param elem l'elemento da ricercare
     */
    @Override
    public boolean exists(Salvataggio elem) {
        return !(db.query("SELECT * " +
                        "FROM Gioco " +
                        "WHERE nome = '" + elem.getNome() + "' " +
                        "AND gioco = '" + elem.getGioco().getNome() + "'",
                false).getRowsNum() == 0);
    }

    /**
     * Aggiunge l'informazione riguardo la quantità di un collezionabile raccolto ad un salvataggio
     *
     * @param elem     il salvataggio a cui aggiungere il collezionabile
     * @param posMappa la mappa relativa ai colleizionabili raccolti
     * @param nome     il nome del collezionabile
     * @param valore   la quantità raccolta di un collezionabile
     */
    public void addCollezionabile(Salvataggio elem, int posMappa, String nome, int valore) {
        String condition = "gioco = '" + elem.getGioco().getNome() + "' AND " +
                "numSalvataggio = " + elem.getNum() + " AND " +
                "posMappa = " + posMappa;
        if ((db.query("SELECT * FROM Collezionabile " +
                "WHERE nome = '" + nome + "' AND " +
                condition, false).getRowsNum() != 0)) {
            db.query("UPDATE Collezionabile SET valore = " + valore + " WHERE nome = '" + nome + "' AND " +
                    condition, true);
        } else {
            db.query("INSERT INTO Collezionabile (gioco, posMappa, numSalvataggio, nome, valore) " +
                    "VALUES ('" + elem.getGioco().getNome() + "'," +
                    posMappa + ", " + elem.getNum() + ", " + nome + ", " + valore + ")", true);
        }
    }

    /**
     * Ottiene tutti i salvataggi relativi ad un gioco
     *
     * @param model il gioco a cui sono relativi
     * @return una lista contenente i salvataggi
     */
    @Override
    public ArrayList<Salvataggio> getAll(Model model) {
        Gioco gioco = (Gioco) model;
        ArrayList<Salvataggio> salvataggi = new ArrayList<>();
        ResultSQL rs =
                db.query("SELECT * " +
                        "FROM Salvataggio " +
                        "WHERE gioco = '" + gioco.getNome() + "'", false);
        for (int i = 0; i < rs.getRowsNum(); i++)
            salvataggi.add(new Salvataggio(gioco, (int) rs.getValue(i, "mappa"), (int) rs.getValue(i, "num"), (int) rs.getValue(i, "punteggio"),
                    getCollezionabiliByGiocoNum((String) rs.getValue(i, "gioco"), (int) rs.getValue(i, "num"))));
        return salvataggi;
    }

    @Override
    public Salvataggio getByNome(String nome, Model[] model) {
        return null;//new Salvataggio(nome, Utils.getGiocoMenu());
    }

    /**
     * Ottiene la mappa ad una determinata posizione relativa ad un gioco
     *
     * @param gioco il gioco relativo alla mappa
     * @param pos   la posizione della mappa
     * @return la mappa
     */
    public Mappa getMappaCorrente(Gioco gioco, int pos) {
        ResultSQL result = db.query("SELECT * " +
                "FROM Mappa " +
                "WHERE Mappa.gioco = '" + gioco.getNome() + "' AND " +
                "Mappa.posizione = " + pos, false);
        if (result.getRowsNum() == 0)
            return null;
        return new Mappa((String) result.getFromLast("nome"), gioco, pos, (int) result.getFromLast("numCelleAsse"),
                (int) result.getFromLast("cellaInizioX"), (int) result.getFromLast("cellaFineX"),
                (int) result.getFromLast("cellaInizioY"), (int) result.getFromLast("cellaFineY"),
                (int) result.getFromLast("dimCella"));
    }

    public int getPunteggio(Gioco gioco, int num) {
        ResultSQL rs = db.query("SELECT punteggio " +
                "FROM Salvataggio " +
                "WHERE gioco = '" + gioco.getNome() + "' AND num = " + num, false);
        if (rs.getFromLast("punteggio") == null) return 0;
        return (int) rs.getFromLast("punteggio");
    }

    /**
     * Ottiene la lista di tutti i collezionabili di un gioco relativi ad un salvataggio
     *
     * @param gioco il gioco da cui prendere i collezionabili
     * @param num   il numero del salvataggio
     * @return una lista di HashMap stringa-intero, la lista è ordinata per numero di mappa e ogni elemento della lista
     * corrisponde ad una mappa, ogni elemento della mappa corrisponde ad un collezionabile.
     */
    private ArrayList<HashMap<String, Integer>> getCollezionabiliByGiocoNum(String gioco, int num) {
        ArrayList<HashMap<String, Integer>> collezionabili = new ArrayList<>();
        ResultSQL resultSQL = db.query("SELECT * " +
                "FROM Collezionabile " +
                "WHERE gioco = '" + gioco + "' AND numsalvataggio = " + num + " " +
                "ORDER BY Collezionabile.posMappa", false);
        int posMappa = 0;
        for (int i = 0; i < resultSQL.getRowsNum(); i++) {
            if ((int) resultSQL.getValue(i, "posMappa") == posMappa + 1) {
                collezionabili.add(new HashMap<>());
                posMappa++;
            }
            collezionabili.get(collezionabili.size() - 1).put((String) resultSQL.getValue(i, "nome"),
                    (int) resultSQL.getValue(i, "valore"));
        }
        return collezionabili;
    }

}
