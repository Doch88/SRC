package src.model.DAO;

import src.model.Mappa;
import src.model.editor.Cella;
import src.model.editor.Editor;
import src.model.editor.SpriteEditor;
import src.utils.Database;
import src.utils.ResultSQL;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Il DAO dell'editor, non implementa l'interfaccia DAO e dunque non ha i metodi simili.
 * Si occupa delle varie funzioni che riguardano l'editor, come il salvataggio di una mappa, l'inserimento
 * di uno sprite, la modifica di uno sprite o, in generale, la modifica di una mappa.
 */
public class EditorDAO {

    private static EditorDAO instance = null;
    private final Database db = Database.getInstance();

    public static EditorDAO getInstance() {
        if (instance == null)
            instance = new EditorDAO();
        return instance;
    }

    /**
     * Salva le informazioni della mappa all'interno del database
     * (La mappa deve già esistere!)
     *
     * @param mappa        mappa da salvare
     * @param dimCella     dimensioni della cella all'interno della mappa
     * @param numCelleX    numero celle degli assi della mappa
     * @param cellaInizioX coordinata x della cella iniziale
     * @param cellaInizioY coordinata y della cella iniziale
     * @param cellaFineX   coordinata x della cella finale
     * @param cellaFineY   coordinata y della cella finale
     */
    public void salvaMappa(Mappa mappa, int dimCella, int numCelleX,
                           int cellaInizioX, int cellaInizioY, int cellaFineX, int cellaFineY) {
        db.query("UPDATE Mappa " +
                "SET nome = '" + mappa.getNome() + "', dimCella = " + dimCella + ", " +
                "numCelleAsse = " + numCelleX + ", " +
                "cellaInizioX = " + cellaInizioX + ", cellaInizioY = " + cellaInizioY + ", " +
                "cellaFineX = " + cellaFineX + ", cellaFineY = " + cellaFineY + " " +
                "WHERE gioco = '" + mappa.getGioco().getNome() + "' AND nome = '" + mappa.getNome() + "'", true);
    }

    /**
     * Ottiene una lista di tutti gli sprite all'interno del database
     *
     * @return una lista sincronizzata di tutti gli sprite
     */
    public List<SpriteEditor> getListaSprites() {
        java.util.List<SpriteEditor> listaSpriteEditor = Collections.synchronizedList(new ArrayList<>());
        ResultSQL rs = db.query("SELECT * FROM Sprite", false);
        for (int i = 0; i < rs.getRowsNum(); i++) {
            SpriteEditor spr = new SpriteEditor(rs.getRow(i));
            listaSpriteEditor.add(spr);
        }
        return listaSpriteEditor;
    }

    /**
     * Rimuove uno sprite dalla mappa
     *
     * @param idCollocazione id della relazione fra sprite e mappa
     */
    public void rimuoviCollocazioneSprite(int idCollocazione) {
        db.query("DELETE FROM Collocazione WHERE id = '" + idCollocazione + "'", true);
    }

    /**
     * Rimuove uno sprite dal database
     *
     * @param sprite sprite da rimuovere
     */
    public void rimuoviSprite(SpriteEditor sprite) {
        db.query("DELETE FROM Sprite WHERE nome = '" + sprite.getNome() + "'", true);
    }

    /**
     * Inserisce uno sprite nel database
     *
     * @param nomeSprite nome dello sprite da aggiungere
     * @param tipo       tipologia dello sprite da aggiungere
     */
    public void inserisciSprite(String nomeSprite, String tipo) {
        db.query("INSERT INTO Sprite (nome, tipo) VALUES ('" + nomeSprite + "', '" + tipo + "')", true);
    }

    /**
     * Ottiene lo sprite del Player dal database
     *
     * @param id          id della mappa
     * @param cellaInizio cella iniziale della mappa, dove spawnerà il player
     * @return uno SpriteEditor con le informazioni del player
     */
    public SpriteEditor getPlayerFromDatabase(int id, Cella cellaInizio) {
        ResultSQL rs;
        rs = db.query("SELECT Sprite.nome " +
                "FROM   Mappa, Sprite, Gioco " +
                "WHERE  Mappa.id = " + id + " AND Mappa.gioco = Gioco.nome AND Sprite.nome = Gioco.player", false);
        String nome = (String) rs.getFromLast("nome");
        SpriteEditor plr = new SpriteEditor(nome);
        plr.setAssociato(cellaInizio);
        return plr;
    }

    /**
     * Ottiene la lista di tutti gli sprites droppati da un altro sprite
     *
     * @param spriteEditor sprite di cui ottenere la lista drop
     * @return lista drop dello sprite
     */
    public ArrayList<SpriteEditor> getDropOggetti(SpriteEditor spriteEditor) {
        ResultSQL listaDrop = db.query("SELECT * " +
                "FROM Oggetto_droppato " +
                "WHERE sprite_id = " + spriteEditor.getDatiSprite().get("id"), false);
        ArrayList<SpriteEditor> drop = new ArrayList<>();
        for (int i = 0; i < listaDrop.getRowsNum(); i++) {
            ResultSQL ld = db.query("SELECT * " +
                    "FROM Sprite " +
                    "WHERE id = " + listaDrop.getValue(i, "droppato_id"), false);
            SpriteEditor spr = new SpriteEditor((String) ld.getFromLast("nome"));
            drop.add(spr);

        }
        return drop;
    }

    /**
     * Inserisce una mappa all'interno del database
     *
     * @param mappa mappa da inserire
     */
    public void insertMappa(Mappa mappa) {
        db.query("INSERT INTO Mappa (nome, gioco) " +
                        "VALUES ('" + mappa.getNome() + "', '" + mappa.getGioco().getNome() + "')",
                true);
    }

    /**
     * Ritorna tutti i dati relativi ad una mappa
     *
     * @param mappa mappa da cui ottenere i dati
     * @return un ResultSQL contenente i risultati della query al database
     */
    public ResultSQL getDatiMappa(Mappa mappa) {
        return db.query("SELECT * " +
                        "FROM Mappa " +
                        "WHERE gioco = '" + mappa.getGioco().getNome() + "' AND nome = '" + mappa.getNome() + "'",
                false);
    }

    /**
     * Ottiene la lista di tutti gli sprite presenti in una determinata mappa
     *
     * @param editor istanza di Editor contenente le informazioni della mappa da cui prendere gli sprite
     * @return la lista degli sprite contenuti all'interno della mappa
     */
    public List<SpriteEditor> getSprites(Editor editor) {
        ResultSQL datiSprites =
                db.query("SELECT Sprite.*, Collocazione.x, Collocazione.y, Collocazione.id " +
                        "FROM Sprite, Collocazione " +
                        "WHERE Collocazione.mappa_id = " + editor.getIdMappa() +
                        " AND Collocazione.sprite_id = Sprite.id", false);

        List<SpriteEditor> listaSprites = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < datiSprites.getRowsNum(); i++) {
            int xSprite = ((int) datiSprites.getValue(i, "x")) / editor.getDimCella();
            int ySprite = ((int) datiSprites.getValue(i, "y")) / editor.getDimCella();
            int collocazione = (int) datiSprites.getValue(i, "_id");
            datiSprites.removeValue(i, "x");
            datiSprites.removeValue(i, "y");
            datiSprites.removeValue(i, "_id");
            SpriteEditor spr = new SpriteEditor(datiSprites.getRow(i));
            spr.setIdCollocazione(collocazione);
            for (Cella cella : editor.getListaCelle()) {
                if (cella.getX() == xSprite && cella.getY() == ySprite) {
                    cella.setContenuto(spr);
                    spr.setAssociato(cella);
                }
            }
            listaSprites.add(spr);
        }
        return listaSprites;
    }

    /**
     * Aggiunge un determinato sprite ai drop di un altro sprite, ossia alla distruzione dello sprite principale
     * spawneranno nella stessa posizione gli sprite nella lista dei drop dello sprite eliminato.
     *
     * @param spriteEditor sprite a cui aggiungere un drop
     * @param drop         nome dello sprite da aggiungere ai drop
     */
    public void aggiungiDropOggetto(SpriteEditor spriteEditor, String drop) {
        int id = (int) db.query("SELECT id " +
                "FROM Sprite " +
                "WHERE nome = '" + drop + "'", false).getFromLast("id");
        db.query("INSERT INTO Oggetto_droppato (sprite_id, droppato_id) VALUES (" + spriteEditor.getDatiSprite().get("id") + ", " + id + ")", true);
    }

    public void setImmagineInizioFine(String nomeImmagine, Mappa mappa, boolean fine) {
        String id = fine ? "Fine" : "Inizio";
        db.query("UPDATE Mappa " +
                "SET immagineCella" + id + " = '" + nomeImmagine + "' " +
                "WHERE gioco = '" + mappa.getGioco().getNome() + "' AND nome = '" + mappa.getNome() + "'", true);
    }

    /**
     * Ottiene uno waypoint (destro o sinistro) di uno sprite
     *
     * @param spriteEditor sprite da cui ottenere lo waypoint
     * @param numWaypoint  1, se si vuole ottenere lo waypoint sinistro, 2 se si vuole ottenere il destro
     * @return una istanza di Point contenente la posizione del waypoint
     */
    public Point getWaypoint(SpriteEditor spriteEditor, int numWaypoint) {
        if (spriteEditor.getIdCollocazione() == -1) spriteEditor.updateDatiCollocazione();
        if (spriteEditor.getIdCollocazione() == -1) return new Point(0, 0);
        ResultSQL dati = db.query("SELECT Collocazione.xWaypoint" + numWaypoint + " " +
                "FROM Collocazione " +
                "WHERE Collocazione.id = " + spriteEditor.getIdCollocazione(), false);
        if (dati.getRowsNum() == 0) return new Point(0, 0);
        return new Point((int) dati.getFromLast("xWaypoint" + numWaypoint), spriteEditor.getAssociato().getY());

    }

    /**
     * Elimina da un determinato sprite nel database il drop di un determinato altro sprite
     *
     * @param spriteEditor sprite da cui eliminare il drop
     * @param drop         nome dello sprite da levare dai drop
     */
    public void eliminaDropOggetto(SpriteEditor spriteEditor, String drop) {
        int id = (int) db.query("SELECT id " +
                "FROM Sprite " +
                "WHERE nome = '" + drop + "'", false).getFromLast("id");
        db.query("DELETE FROM Oggetto_droppato WHERE sprite_id = " +
                spriteEditor.getDatiSprite().get("id") +
                " AND droppato_id = " + id, true);
    }

    /**
     * Controlla se una mappa esiste nel database
     *
     * @param mappa la mappa da verificare
     * @return true se la mappa esiste, false altrimenti
     */
    public boolean exists(String gioco, String mappa) {
        return db.query("SELECT * " +
                        "FROM Mappa " +
                        "WHERE gioco = '" + gioco + "' AND nome = '" + mappa + "'",
                false).getRowsNum() != 0;
    }
}