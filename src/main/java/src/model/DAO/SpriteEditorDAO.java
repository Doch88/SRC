package src.model.DAO;

import src.model.Model;
import src.model.editor.SpriteEditor;
import src.utils.Database;
import src.utils.ResultSQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO dello sprite dell'editor, effettua modifiche, inserimenti ed eliminazioni di sprite dal database.
 */
public class SpriteEditorDAO implements DAO<SpriteEditor> {

    private static SpriteEditorDAO instance = null;
    private final Database db = Database.getInstance();

    public static SpriteEditorDAO getInstance() {
        if (instance == null)
            instance = new SpriteEditorDAO();
        return instance;
    }

    /**
     * Aggiunge uno sprite all'interno del database
     *
     * @param elem l'elemento da aggiungere
     */
    @Override
    public void add(SpriteEditor elem) {
        db.query("INSERT INTO Sprite (nome, tipo) " +
                "VALUES ('" + elem.getNome() + "','" + elem.getTipoSprite() + "')", true);
        elem.setDatiSprite(db.query("SELECT * " +
                "FROM Sprite " +
                "WHERE nome = '" + elem.getNome() + "'", false).getLastRow());
    }

    /**
     * Aggiunge uno sprite all'interno di una mappa
     *
     * @param elem    lo sprite da aggiungere alla mappa
     * @param idMappa l'id della mappa da modificare
     * @param x       la coordinata x all'interno della mappa
     * @param y       la coordinata y all'interno della mappa
     */
    public void aggiungiAllaMappa(SpriteEditor elem, int idMappa, int x, int y) {
        db.query("INSERT INTO Collocazione (mappa_id, x, y, sprite_id) VALUES (" + idMappa + ", " + x + ", " + y +
                ", " + elem.getDatiSprite().get("id") + ")", true);
    }

    /**
     * Aggiorna uno sprite basandosi sul nome
     *
     * @param nome il nome dell'elemento all'interno del database
     * @param elem l'istanza contenente i dati da modificare
     * @return true se andato a buon fine, false altrimenti
     */
    @Override
    public boolean updateByNome(String nome, SpriteEditor elem) {
        if (existsByNome(elem.getNome())) {
            String query = "UPDATE Sprite SET";
            for (Map.Entry entry : elem.getDatiSprite().entrySet()) {
                if (!(entry.getKey().equals("piattaforma")))
                    query += " " + entry.getKey() + " = '" + entry.getValue() + "',";
                else
                    query += " " + entry.getKey() + " = " + (entry.getValue().equals("false") ? "0" : "1") + ",";
            }
            query = query.substring(0, query.length() - 1);
            query += " WHERE nome = '" + nome + "'";
            db.query(query, true);

            if (elem.getIdCollocazione() != -1) {
                query = "UPDATE Collocazione SET";
                for (Map.Entry<String, Object> entry : elem.getDatiCollocazione().entrySet())
                    if (entry.getValue() != null && !entry.getValue().equals("null"))
                        query += " " + entry.getKey() + " = '" + entry.getValue() + "',";
                query = query.substring(0, query.length() - 1);
                query += " WHERE id = " + elem.getIdCollocazione();
                db.query(query, true);
            }
            return true;
        }
        return false;
    }

    @Override
    public void remove(SpriteEditor elem) {
        removeByNome(elem.getNome());
    }

    /**
     * Rimuove uno sprite dal database usando il nome
     *
     * @param nome il nome dell'elemento da rimuovere
     * @param args
     */
    @Override
    public void removeByNome(String nome, Model... args) {
        db.query("DELETE FROM Sprite WHERE nome = '" + nome + "'", true);
    }

    /**
     * Controlla se uno sprite esiste
     *
     * @param elem l'elemento da ricercare
     * @return
     */
    @Override
    public boolean exists(SpriteEditor elem) {
        return !(db.query("SELECT * " +
                        "FROM Sprite " +
                        "WHERE nome = '" + elem.getNome() + "'",
                false).getRowsNum() == 0);
    }

    /**
     * Controlla se uno sprite esiste basandosi sul nome
     *
     * @param nome il nome dello sprite
     * @return
     */
    public boolean existsByNome(String nome) {
        return !(db.query("SELECT * " +
                        "FROM Sprite " +
                        "WHERE nome = '" + nome + "'",
                false).getRowsNum() == 0);
    }

    /**
     * Ottiene tutti gli sprite all'interno del database
     *
     * @param model il model a cui sono relativi
     * @return una lista di tutti gli sprites all'interno del database
     */
    @Override
    public ArrayList<SpriteEditor> getAll(Model model) {
        ArrayList<SpriteEditor> spriteEditors = new ArrayList<>();
        ResultSQL listaSprites =
                db.query("SELECT * FROM Sprite", false);
        for (String nome : (String[]) listaSprites.getColumn("nome").toArray())
            spriteEditors.add(new SpriteEditor(nome));
        return spriteEditors;
    }

    /**
     * Ottiene uno sprite basandosi sul nome
     *
     * @param nome  il nome dell'elemento da ottenere
     * @param model i model relativi all'elemento
     * @return uno SpriteEditor contenente le informazioni prelevate dal database
     */
    @Override
    public SpriteEditor getByNome(String nome, Model... model) {
        SpriteEditor spriteEditor;
        if ((exists(nome))) {
            HashMap<String, Object> datiSprite = db.query("SELECT * " +
                    "FROM Sprite " +
                    "WHERE nome = '" + nome + "'", false).getLastRow();
            spriteEditor = new SpriteEditor(datiSprite);
        } else {
            HashMap<String, Object> datiSprite = getStructure();
            spriteEditor = new SpriteEditor(datiSprite);
        }
        return spriteEditor;
                /*new SpriteEditor(nome, (String) db.query("SELECT * FROM Sprite WHERE nome = '"+nome+"'",
                    false).getFromLast("tipo"));*/
    }


    /**
     * Trova l'id della relazione fra mappa e sprite
     *
     * @param sprite lo sprite collocato all'interno della mappa
     * @param mappa  la mappa dove trovare lo sprite
     * @return un intero contenente l'id della collocazione
     */
    public int getIDCollocazione(SpriteEditor sprite, int mappa) {
        String query = "SELECT Collocazione.id " +
                "FROM Collocazione " +
                "WHERE Collocazione.mappa_id = " + mappa +
                " AND Collocazione.sprite_id = " + sprite.getDatiSprite().get("id") +
                " AND Collocazione.x = " + sprite.getSpriteX(sprite.getAssociato()) +
                " AND Collocazione.y = " + sprite.getSpriteY(sprite.getAssociato());
        ResultSQL collocazioneSprite = db.query(query, false);
        return (int) collocazioneSprite.getFromLast("id");
    }

    /**
     * Ottiene tutti i dati di uno sprite basandosi sul nome
     *
     * @param nomeSprite il nome dello sprite
     * @return una mappa stringa-oggetto contenente le tabelle del database
     */
    public HashMap<String, Object> getDati(String nomeSprite) {
        return db.query("SELECT * " +
                "FROM Sprite " +
                "WHERE nome = '" + nomeSprite + "'", false).getLastRow();
    }

    /**
     * Ottiene i dati della collocazione di uno sprite all'interno della mappa
     *
     * @param idCollocazione l'id della relazione
     * @return una mappa stringa-oggetto contenente le tabelle del database
     */
    public HashMap<String, Object> getDatiCollocazione(int idCollocazione) {
        return db.query("SELECT * " +
                "FROM Collocazione " +
                "WHERE id = " + idCollocazione, false).getLastRow();
    }

    /**
     * Ottiene i nome delle colonne della tabella Sprite, quindi tutti gli attributi possibili
     *
     * @return array di stringhe contenente i nomi
     */
    public String[] getColumns() {
        return db.getColumnNames("Sprite");
    }

    /**
     * Ottiene la struttura dello sprite, quindi tutti gli attributi che potrebbe avereuno sprite
     *
     * @return una mappa stringa-oggetto
     */
    public HashMap<String, Object> getStructure() {
        HashMap<String, Object> structure = new HashMap<>();
        for (String key : getColumns())
            structure.put(key, null);
        return structure;
    }

    /**
     * Restituisce true se nel database esiste uno sprite con il nome passato come parametro, false altrimenti
     */
    public boolean exists(String nomeSprite) {
        return db.query("SELECT id " +
                "FROM Sprite " +
                "WHERE nome = '" + nomeSprite + "'", false).getRowsNum() != 0;
    }

    /**
     * Controlla se un dato id di collocazione esiste nel database
     *
     * @param idCollocazione l'id di collocazione da controllare
     * @return true se esiste, false altrimenti
     */
    public boolean existsCollocazione(int idCollocazione) {
        return (db.query("SELECT * " +
                "FROM Collocazione " +
                "WHERE id = " + idCollocazione, false).getRowsNum() != 0);
    }
}
