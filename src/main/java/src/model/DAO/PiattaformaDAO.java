package src.model.DAO;

import src.model.Gioco;
import src.model.Mappa;
import src.model.Salvataggio;
import src.model.piattaforma.Animazione;
import src.model.piattaforma.Piattaforma;
import src.model.piattaforma.Sprite.*;
import src.utils.Database;
import src.utils.ResultSQL;
import src.utils.Settings;
import src.utils.Utils;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * DAO della piattaforma, contiene tutte le query necessarie al funzionamento della piattaforma.
 * Non implementa l'interfaccia DAO, poichè non svolge le stesse funzioni. Il suo scopo è quello di ottenere tutti i dati
 * relativi ad una mappa e un salvataggio e tutte le informazioni sui relativi sprites.
 * Effettua anche delle modifiche ai salvataggi e alle relazioni con i collezionabili ottenuti.
 */
public class PiattaformaDAO {

    private static PiattaformaDAO instance = null;

    private final Piattaforma piattaforma = Piattaforma.getInstance();
    private final Database db = Database.getInstance();

    public static PiattaformaDAO getInstance() {
        if (instance == null)
            instance = new PiattaformaDAO();
        return instance;
    }

    /**
     * Restituisce tutti gli sprites relativi ad una mappa e tutte le informazioni associate
     *
     * @return lista degli sprites presenti nella mappa
     */
    public List<Sprite> getSpritesInMappa(Mappa mappa) {
        List<Sprite> sprites = Collections.synchronizedList(new ArrayList<>());
        ResultSQL listaSprites;
        listaSprites = db.query("SELECT Sprite.*, Collocazione.id, Collocazione.x, Collocazione.y, " +
                "Mappa.cellaInizioX, Mappa.cellaInizioY, Mappa.dimCella, " +
                "Collocazione.xWaypoint1, Collocazione.xWaypoint2, Collocazione.associato " +
                "FROM Sprite, Collocazione, Mappa " +
                "WHERE   Collocazione.sprite_id = Sprite.id AND " +
                "Collocazione.mappa_id = Mappa.id AND " +
                "Mappa.gioco = '" + mappa.getGioco().getNome() + "' AND " +
                "Mappa.posizione = " + mappa.getPosizione(), false);

        for (int i = 0; i < listaSprites.getRowsNum(); i++) {
            Sprite tmp = getSprite(sprites, listaSprites.getRow(i));
            ResultSQL listaDrop = db.query("SELECT * " +
                    "FROM Oggetto_droppato " +
                    "WHERE sprite_id = " + listaSprites.getRow(i).get("id"), false);
            for (int j = 0; j < listaDrop.getRowsNum(); j++) {
                ResultSQL drop = db.query("SELECT * " +
                        "FROM Sprite " +
                        "WHERE id = " + listaDrop.getRow(j).get("droppato_id"), false);
                for (int k = 0; k < drop.getRowsNum(); k++) {
                    Sprite oggettoDrop = getSprite(sprites, drop.getRow(k));
                    tmp.addSpriteDaDroppare(oggettoDrop);
                }
            }
            sprites.add(tmp);
        }
        return sprites;
    }

    /**
     * Ottiene lo sprite del player di una determinata mappa
     *
     * @param gioco il gioco di cui prendere il player
     * @param mappa la mappa dove verrà inserito il player (necessaria per la posizione iniziale)
     * @return un istanza di Player con posizione derivata dalla mappa
     */
    public Player getPlayer(Gioco gioco, Mappa mappa) {
        ResultSQL rs;
        rs = db.query("SELECT Sprite.*, Mappa.cellaInizioX, Mappa.cellaInizioY, Mappa.dimCella " +
                "FROM   Mappa, Sprite, Gioco " +
                "WHERE  Mappa.gioco = '" + gioco.getNome() + "' AND Gioco.nome = '"
                + gioco.getNome() + "' AND Sprite.nome = Gioco.player AND Mappa.posizione = " + mappa.getPosizione(), false);
        Sprite spr = getSprite(piattaforma.getSprites(), rs.getLastRow());
        if (spr instanceof Player) {
            Player player = (Player) spr;
            player.setGravita(getGravita(gioco));
            return (Player) spr;
        } else
            return null;
    }

    /**
     * Ottiene il valore della gravita' di un determinato gioco
     *
     * @param gioco il gioco da cui ottenere la gravità
     * @return la gravita' del gioco
     */
    public int getGravita(Gioco gioco) {
        ResultSQL elem = db.query("SELECT gravita " +
                "FROM   Gioco " +
                "WHERE  Gioco.nome = '" + gioco.getNome() + "'", false);
        return (int) elem.getFromLast("gravita");
    }

    /**
     * Crea una istanza di proiettile, cercandola col nome, e le assegna il proprietario (cioè colui che ha sparato il proiettile).
     *
     * @param nome         il nome del proiettile all'interno del database
     * @param proprietario lo sprite che ha sparato il proiettile
     * @return Una istanza di proiettile che parte con verso e direzione del proprietario
     */
    public Proiettile getProiettile(String nome, Sprite proprietario) {
        ResultSQL elem = db.query("SELECT Sprite.* " +
                "FROM   Sprite " +
                "WHERE  Sprite.nome = '" + nome + "'", false);
        Image immagine = Utils.getBufferedImage(Settings.CARTELLA_SPRITES + "\\" + elem.getFromLast(("nome")),
                (String) elem.getFromLast(("nome")));
        return new Proiettile((String) elem.getFromLast("nome"), immagine, proprietario,
                (int) elem.getFromLast("velocita"), (int) elem.getFromLast("attacco"),
                Piattaforma.getInstance().getxMax());
    }

    /**
     * Prende le coordinate del punto di fine livello dal database
     */

    public Animazione getAnimazioneInizioFine(String gioco, int mappa, boolean fineLivello) {
        String nome = "immagineCella" + (fineLivello ? "Fine" : "Inizio");
        ResultSQL rs;
        rs = db.query("SELECT " + nome + " " +
                "FROM   Mappa " +
                "WHERE  Mappa.gioco = '" + gioco + "' AND Mappa.posizione = " + (mappa), false);
        Image firstImage = Utils.getBufferedImage(Settings.CARTELLA_SPRITES, (String) rs.getFromLast(nome));
        if (firstImage == null) return null;
        return new Animazione((String) rs.getFromLast(nome), firstImage);
    }

    /**
     * Basandosi sui dati passati come parametro, crea una nuova istanza di Sprite che sia coerente con
     * gli attributi passati al metodo e agli sprites già presenti all'interno della mappa dove verrà inserito
     * lo sprite creato.
     *
     * @param sprites la lista degli sprites presenti nella mappa nel momento della creazione
     * @param elem    i dati dello sprite da creare
     * @return una istanza di Sprite coerente con le informazioni passate
     */
    public Sprite getSprite(List<Sprite> sprites, HashMap<String, Object> elem) {
        Image immagine = Utils.getBufferedImage(Settings.CARTELLA_SPRITES + "\\" + elem.get("nome"),
                (String) elem.get("nome"));
        Sprite tmp;
        String tipo = (String) elem.get("tipo");
        switch (tipo) { //Creiamo uno sprite in base al tipo inserito nel database
            case "player":
                tmp = new Player((String) elem.get("nome"), immagine, (String) elem.get("proiettile"), (int) elem.get("salute"), (int) elem.get("riposo"), (int) elem.get("ripososparo"), (int) elem.get("attacco"), (int) elem.get("velocita"));
                tmp.setPosition((int) elem.get("cellaInizioX") * (int) elem.get("dimCella"), (int) elem.get("cellaInizioY") * (int) elem.get("dimCella"));
                break;
            case "collectable":
            case "health":
            case "bullets":
                tmp = new Raccoglibile((String) elem.get("nome"), immagine, (String) elem.get("tipo"), (int) elem.get("valore"), (int) elem.get("punteggio_fornito"));
                break;
            case "npc":
                tmp = new NPC((String) elem.get("nome"), immagine, (String) elem.get("proiettile"), (int) elem.get("attacco"), (int) elem.get("velocita"), (int) elem.get("salute"), (int) elem.get("ripososparo"), (int) elem.get("punteggio_fornito"));
                if ((int) elem.get("xWaypoint1") != (int) elem.get("xWaypoint2")) {
                    ((NPC) tmp).setWaypoint1((int) elem.get("xWaypoint1"));
                    ((NPC) tmp).setWaypoint2((int) elem.get("xWaypoint2"));
                }
                break;
            case "portal":
                tmp = new Portale((String) elem.get("nome"), (int) elem.get("_id"), immagine);
                if (elem.get("associato") != null)
                    Piattaforma.getInstance().associaPortale(sprites, (Portale) tmp, (int) elem.get("associato"));
                break;
            case "alert_point":
                tmp = new AlertPoint((String) elem.get("nome"), immagine, (String) elem.get("testo"));
                break;
            case "block":
            default:
                tmp = new Blocco((String) elem.get("nome"), immagine, (boolean) elem.get("piattaforma"), (int) elem.get("velocita"), (int) elem.get("attacco"), (int) elem.get("salute"), (int) elem.get("punteggio_fornito"));
                if ((int) elem.get("xWaypoint1") != (int) elem.get("xWaypoint2")) {
                    ((Blocco) tmp).setWaypoint1((int) elem.get("xWaypoint1"));
                    ((Blocco) tmp).setWaypoint2((int) elem.get("xWaypoint2"));
                }
                break;
        }
        if (!tipo.equals("player") && elem.get("x") != null && elem.get("y") != null)
            tmp.setPosition((int) elem.get("x"), (int) elem.get("y"));
        return tmp;
    }

    /**
     * Controlla se esiste una mappaEditor successiva a quella attuale e la ritorna al chiamante
     *
     * @param gioco il gioco di cui fa parte la mappaEditor
     * @param mappa la posizione della mappaEditor attuale
     * @return la mappaEditor successiva o null se la mappaEditor attuale è l'ultima
     */
    public HashMap<String, Object> getMappaSuccessiva(String gioco, int mappa) {
        ResultSQL mappaQuery;
        mappaQuery = db.query("SELECT Mappa.* " +
                "FROM Gioco, Mappa " +
                "WHERE Mappa.posizione = " + (mappa + 1) + " AND Gioco.nome = '" + gioco + "' AND Mappa.gioco = Gioco.nome", false);

        return mappaQuery.getLastRow();
    }

    /**
     * Cambia i valori relativi al salvataggio corrente nel database, aggiunge quindi i collezionabili e quanti ne sono stati raccolti
     *
     * @param salvataggio
     */
    public void changeSave(Salvataggio salvataggio, int mappaPrecedente) {
        db.query("UPDATE salvataggio SET mappa=" + salvataggio.getPosizioneMappaAttuale() + ", punteggio= "
                + salvataggio.getPunteggio() +
                " WHERE  gioco='" + salvataggio.getGioco().getNome() +
                "' AND num =" + salvataggio.getNum(), true);
        for (Map.Entry<String, Integer> entry : salvataggio.getCollezionabili(mappaPrecedente).entrySet()) {
            ResultSQL rs = db.query("SELECT * FROM Collezionabile WHERE " +
                    "gioco = '" + salvataggio.getGioco().getNome() + "' AND " +
                    "numsalvataggio = " + salvataggio.getNum() + " AND " +
                    "nome = '" + entry.getKey() + "'", false);
            if (rs.getRowsNum() == 0) { //Vede se è già stato memorizzato nel database un valore per il collezionabile, se non c'è allora inserisce una nuova riga
                db.query("INSERT INTO Collezionabile(gioco, numsalvataggio, posMappa, nome, valore) " +
                        "values ('" + salvataggio.getGioco().getNome() + "'," + salvataggio.getNum() + "," + mappaPrecedente + ",'" + entry.getKey() + "'," + entry.getValue() + ")", true);
            } else //Altrimenti aggiorna la riga del collezionabile
                db.query("UPDATE Collezionabile SET valore=" + entry.getValue() +
                        " WHERE id=" + rs.getFromLast("id"), true);
        }
    }

}
