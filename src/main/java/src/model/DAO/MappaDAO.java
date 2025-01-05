package src.model.DAO;

import src.model.Gioco;
import src.model.Mappa;
import src.model.Model;
import src.utils.Database;
import src.utils.ResultSQL;

import java.util.ArrayList;

/**
 * DAO delle mappe, crea, aggiorna, ottiene ed elimina mappe dal database.
 */
public class MappaDAO implements DAO<Mappa> {

    private static MappaDAO instance = null;
    private final Database db = Database.getInstance();

    public static MappaDAO getInstance() {
        if (instance == null)
            instance = new MappaDAO();
        return instance;
    }

    @Override
    public void add(Mappa mappa) {
        db.query("INSERT INTO Mappa (nome, gioco, posizione) " +
                "VALUES ('" + mappa.getNome() + "','" + mappa.getGioco().getNome() + "', " + mappa.getPosizione() + ")", true);
    }

    @Override
    public boolean updateByNome(String nome, Mappa elem) {
        if (exists(elem)) {
            db.query("UPDATE Mappa SET dimCella = " + elem.getDimCella() + ", " +
                    "numCelleAsse = " + elem.getNumCelleAsse() + " " +
                    "WHERE nome = '" + nome + "'", true);
            return true;
        } else return false;
    }

    @Override
    public void remove(Mappa elem) {
        removeByNome(elem.getNome());
    }

    @Override
    public void removeByNome(String nome, Model... model) {
        Gioco gioco = (Gioco) model[0];
        db.query("DELETE FROM Mappa " +
                "WHERE nome = '" + nome + "' " +
                "AND gioco = '" + gioco.getNome() + "'", true);
    }

    @Override
    public boolean exists(Mappa elem) {
        return !(db.query("SELECT * " +
                        "FROM Mappa " +
                        "WHERE nome = '" + elem.getNome() + "' " +
                        "AND gioco = '" + elem.getGioco().getNome() + "'",
                false).getRowsNum() == 0);
    }

    @Override
    public ArrayList<Mappa> getAll(Model model) {
        Gioco gioco = (Gioco) model;
        ArrayList<Mappa> mappe = new ArrayList<>();
        ResultSQL rs =
                db.query("SELECT nome, posizione, numCelleAsse, cellaInizioX, cellaFineX, cellaInizioY, cellaFineY, dimCella " +
                        "FROM Mappa " +
                        "WHERE gioco = '" + gioco.getNome() + "'", false);
        for (int i = 0; i < rs.getRowsNum(); i++)
            mappe.add(new Mappa((String) rs.getValue(i, "nome"), gioco, (int) rs.getValue(i, "posizione"),
                    (int) rs.getValue(i, "numCelleAsse"), (int) rs.getValue(i, "cellaInizioX"),
                    (int) rs.getValue(i, "cellaFineX"), (int) rs.getValue(i, "cellaInizioY"),
                    (int) rs.getValue(i, "cellaFineY"), (int) rs.getValue(i, "dimCella")));
        return mappe;
    }

    @Override
    public Mappa getByNome(String nome, Model... model) {
        Gioco gioco = (Gioco) model[0];
        int posizione = (int) (db.query("SELECT * " +
                        "FROM Mappa " +
                        "WHERE nome = '" + nome + "' " +
                        "AND gioco = '" + gioco.getNome() + "'",
                false).getFromLast("posizione"));
        return new Mappa(nome, gioco, posizione);
    }

    /**
     * Cerca tutte le mappe relative ad un gioco
     *
     * @param gioco il gioco da cui ottenere le mappe
     * @return una lista di tutte le mappe contenute nel gioco
     */
    public ArrayList<Mappa> getAllByGioco(Gioco gioco) {
        ArrayList<Mappa> mappe = new ArrayList<>();
        ResultSQL rs = db.query("SELECT nome, posizione FROM Mappa WHERE gioco = '" + gioco.getNome() + "'",
                false);
        for (int i = 0; i < rs.getRowsNum(); i++)
            mappe.add(new Mappa((String) rs.getValue(i, "nome"), gioco, (int) rs.getValue(i, "posizione")));
        return mappe;
    }

    /**
     * Elimina la mappa passata come parametro dal database
     *
     * @param nome
     * @param gioco
     */
    public void removeByNome(String nome, Gioco gioco) {
        db.query("DELETE FROM Mappa WHERE nome = '" + nome + "' AND gioco = '" + gioco.getNome() + "'", true);
    }

    /**
     * Controlla se esiste una mappa, dato il suo nome e il gioco di appartenza.
     *
     * @param nome  nome della mappa
     * @param gioco gioco a cui appartiene
     * @return true se esiste, false altrimenti
     */
    public boolean exists(String nome, Gioco gioco) {
        return !(db.query("SELECT * FROM Mappa WHERE gioco = '" +
                gioco.getNome() + "' AND nome = '" + nome + "'", false).getRowsNum() == 0);
    }

}
