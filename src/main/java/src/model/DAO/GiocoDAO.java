package src.model.DAO;

import src.model.Gioco;
import src.model.Model;
import src.model.piattaforma.Sprite.Player;
import src.utils.Database;
import src.utils.ResultSQL;
import src.utils.Settings;
import src.utils.Utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * DAO del gioco, gestisce tutte le query relative all'aggiunta, modifica ed eliminazione di un gioco.
 */
public class GiocoDAO implements DAO<Gioco> {

    private static GiocoDAO instance = null;
    private final Database db = Database.getInstance();

    public static GiocoDAO getInstance() {
        if (instance == null)
            instance = new GiocoDAO();
        return instance;
    }

    @Override
    public void add(Gioco gioco) {
        db.query("INSERT INTO Sprite (nome, tipo, salute, riposo, velocita, attacco, ripososparo, proiettile) " +
                "VALUES ('" +
                gioco.getPlayer().getNome() + "', " +
                "'Player', " +
                gioco.getPlayer().getSaluteMassima() + ", " +
                gioco.getPlayer().getPeriodoDiRiposo() + ", " +
                gioco.getPlayer().getVelocita() + ", " +
                gioco.getPlayer().getAttacco() + ", " +
                gioco.getPlayer().getPeriodoDiRiposoSparo() + ", '" +
                gioco.getPlayer().getNomeProiettile() + "')", true);

        db.query("INSERT INTO Gioco (nome, framerate, player) " +
                "VALUES ('" + gioco.getNome() + "'," + gioco.getFramerate() + ",'" + gioco.getNome() + "Player" + "')", true);
    }

    @Override
    public boolean updateByNome(String nome, Gioco elem) {
        if (exists(elem)) {
            db.query("UPDATE Gioco " +
                    "SET "/*nome = '" + elem.getNome() + "' AND " +
                                "*/ + "framerate = " + elem.getFramerate() + " " +
                    "WHERE nome = '" + nome + "'", true);
            Player player = elem.getPlayer();
            db.query("UPDATE Sprite " +
                    "SET salute = " + player.getSaluteMassima() + ", " +
                    "attacco = " + player.getAttacco() + ", " +
                    "riposo = " + player.getPeriodoDiRiposo() + ", " +
                    "ripososparo = " + player.getPeriodoDiRiposoSparo() + ", " +
                    "velocita = " + player.getVelocita() + " " +
                    "WHERE nome = '" + player.getNome() + "'", true);
            return true;
        }
        return false;
    }

    @Override
    public void remove(Gioco elem) {
        removeByNome(elem.getNome());
    }

    @Override
    public void removeByNome(String nome, Model... model) {
        db.query("DELETE FROM Gioco WHERE nome = '" + nome + "'", true);
    }

    @Override
    public boolean exists(Gioco elem) {
        return !(db.query("SELECT * " +
                        "FROM Gioco " +
                        "WHERE nome = '" + elem.getNome() + "' " +
                        "AND framerate = " + elem.getFramerate(),
                false).getRowsNum() == 0);
    }

    /**
     * Ottiene lo sprite di un player, basandosi sul nome dello sprite
     *
     * @param nome il nome del player
     * @return il player
     */
    public Player getPlayer(String nome) {
        ResultSQL infoPlayer = db.query("SELECT * FROM Sprite WHERE tipo = 'player' AND nome = '" + nome + "'", false);
        if (infoPlayer.getRowsNum() == 0)
            return null;
        Image immagine = Utils.getBufferedImage(Settings.CARTELLA_SPRITES + "\\" + nome, nome);
        return new Player((String) infoPlayer.getFromLast("nome"),
                immagine,
                (String) infoPlayer.getFromLast("proiettile"),
                (int) infoPlayer.getFromLast("salute"),
                (int) infoPlayer.getFromLast("riposo"),
                (int) infoPlayer.getFromLast("ripososparo"),
                (int) infoPlayer.getFromLast("attacco"),
                (int) infoPlayer.getFromLast("velocita"));

    }

    @Override
    public ArrayList<Gioco> getAll(Model model) {
        ArrayList<Gioco> giochi = new ArrayList<>();
        ResultSQL listaGiochi =
                db.query("SELECT nome, framerate, player FROM Gioco", false);
        for (int i = 0; i < listaGiochi.getRowsNum(); i++) {
            Player player = getPlayer((String) listaGiochi.getValue(i, "player"));
            giochi.add(new Gioco((String) listaGiochi.getValue(i, "nome"), (int) listaGiochi.getValue(i, "framerate"), player));
        }
        return giochi;
    }

    @Override
    public Gioco getByNome(String nome, Model[] model) {
        ResultSQL res = db.query("SELECT * FROM Gioco WHERE nome = '" + nome + "'",
                false);
        return new Gioco(nome, (int) res.getFromLast("framerate"), new Player((String) res.getFromLast("player")));
    }
}
