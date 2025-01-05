package src.model.editor;

import src.model.DAO.EditorDAO;
import src.model.Gioco;
import src.model.Mappa;
import src.utils.ResultSQL;
import src.utils.Settings;
import src.utils.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Gestisce tutte le informazioni riguardanti la mappa attualmente in fase di editing.
 * Contiene inoltre tutte le impostazioni attuali dell'editor, come modalità, costanti
 * e dimensione di anteprima e zoom.
 * Qualunque modifica viene effettuata alla mappa o ai suoi sprite passa prima per questa classe.
 */
public class Editor {

    private static Editor instance = null;

    /**
     * Modalità che ferma il thread di aggiornamento dell'anteprima
     */
    public static final int MOD_CHIUSURA_EDITOR = -1;
    /**
     * Modalità normalmente attiva, che permette la selezione delle celle
     */
    public static final int MOD_STANDARD = 0;
    /**
     * Modalità di importazione degli sprites dal database
     */
    public static final int MOD_IMPORTAZIONE = 1;
    /**
     * Modalità di posizionamento o spostamento di uno sprite all'interno della mappa
     */
    public static final int MOD_POSIZIONAMENTO = 2;
    /**
     * Modalità di eliminazione di uno sprite dalla mappa
     */
    public static final int MOD_ELIMINAZIONE = 3;
    /**
     * Modalità di creazione di uno sprite all'interno del database
     */
    public static final int MOD_CREAZIONE = 4;
    /**
     * Modalità di posizionamento del primo waypoint
     */
    public static final int MOD_WAYPOINT1 = 5;
    /**
     * Modalità di posizionamento del secondo waypoint
     */
    public static final int MOD_WAYPOINT2 = 6;
    /**
     * Modalità di associazione dei portali
     */
    public static final int MOD_ASSOCIA_PORTALE = 7;
    /**
     * Modalità di impostazione della cella di inizio
     */
    public static final int MOD_PUNTO_INIZIO = 8;
    /**
     * Modalità di impostazione della cella di fine
     */
    public static final int MOD_PUNTO_FINE = 9;

    public static final int KEY_POSIZIONA = KeyEvent.VK_P;
    public static final int KEY_DUPLICA = KeyEvent.VK_D;
    public static final int KEY_CREA = KeyEvent.VK_C;
    public static final int KEY_IMPORTA = KeyEvent.VK_I;
    public static final int KEY_ELIMINA = KeyEvent.VK_E;
    public static final int KEY_AVVIA = KeyEvent.VK_A;
    public static final int KEY_INDIETRO = KeyEvent.VK_N;

    public static final String IMMAGINE_BACKGROUND = "background";
    public static final String IMMAGINE_INIZIO_LIVELLO = "inizioLivello";
    public static final String IMMAGINE_FINE_LIVELLO = "fineLivello";

    private int modalitaAttuale = MOD_STANDARD;

    private final EditorDAO editorDAO;

    private int idMappa;
    private Image immagine;
    private int dimCella, numCelleAsse;

    private Mappa mappa;
    private Gioco gioco;
    private Cella cellaInizio, cellaFine;

    private int xAnteprima = 0, yAnteprima = 0;
    private int dimCellaAnteprima, altezzaAnteprima, lunghezzaAnteprima, zoomAnteprima = 30;

    private String nomeImmagineInizio, nomeImmagineFine;
    private Image immagineInizio, immagineFine;

    private List<SpriteEditor> listaSprites;
    private List<Cella> listaCelle;

    private SpriteEditor player;
    private SpriteEditor spriteSelezionato;
    private Cella cellaSelezionata;

    private Editor() {
        editorDAO = EditorDAO.getInstance();
    }

    public static Editor getInstance() {
        if (instance == null)
            instance = new Editor();
        return instance;
    }

    /**
     * estrae i parametri della mappaEditor dal database in base al nome del gioco e al nome della mappaEditor,
     * se non esiste ne viene creata una con i parametri di default e il nome dato e viene salvata nel database,
     * l'immagine ha lo stesso nome della mappaEditor ed è contenuta in una cartella con lo stesso nome del gioco
     */
    public void init(Mappa mappa) {
        this.mappa = mappa;
        gioco = mappa.getGioco();
        if (!editorDAO.exists(gioco.getNome(), mappa.getNome()))
            editorDAO.insertMappa(mappa);
        ResultSQL datiMappa = editorDAO.getDatiMappa(mappa);
        dimCella = (int) datiMappa.getFromLast("dimCella");
        numCelleAsse = (int) datiMappa.getFromLast("numCelleAsse");
        idMappa = (int) datiMappa.getFromLast("id");

        nomeImmagineInizio = (String) datiMappa.getFromLast("immagineCellaInizio");
        nomeImmagineFine = (String) datiMappa.getFromLast("immagineCellaFine");

        immagineInizio = Utils.getBufferedImage(Settings.CARTELLA_SPRITES, nomeImmagineInizio);
        immagineFine = Utils.getBufferedImage(Settings.CARTELLA_SPRITES, nomeImmagineFine);

        listaCelle = Collections.synchronizedList(new ArrayList<Cella>());
        inizializzaCelle();
        this.setCellaInizio(new Cella((int) datiMappa.getFromLast("cellaInizioX"), (int) datiMappa.getFromLast("cellaInizioY")));
        this.setCellaFine(new Cella((int) datiMappa.getFromLast("cellaFineX"), (int) datiMappa.getFromLast("cellaFineY")));
        immagine = Utils.getBufferedImage(Settings.CARTELLA_MAPPE, mappa.getNome());
        listaSprites = editorDAO.getSprites(this);
        player = editorDAO.getPlayerFromDatabase(idMappa, cellaInizio);
        spriteSelezionato = player;
    }

    /**
     * Associa lo sprite attualmente selezionato, se esso è un portale, con lo sprite passato come parametro.
     *
     * @param spriteDaAssociare sprite con cui associare lo sprite selezionato (deve essere anche esso un portale)
     */
    public void associaPortale(SpriteEditor spriteDaAssociare) {
        if (spriteSelezionato != null && spriteDaAssociare != null && spriteSelezionato.getDatiSprite().get("tipo").equals("Portale") &&
                spriteDaAssociare.getDatiSprite().get("tipo").equals("portal")) {
            HashMap<String, Object> datiCollocazioneSprite1 = spriteSelezionato.getDatiCollocazione();
            HashMap<String, Object> datiCollocazioneSprite2 = spriteDaAssociare.getDatiCollocazione();
            datiCollocazioneSprite1.put("associato", spriteDaAssociare.getIdCollocazione());
            spriteSelezionato.modificaCollocazione(datiCollocazioneSprite1);
            datiCollocazioneSprite2.put("associato", spriteSelezionato.getIdCollocazione());
            spriteDaAssociare.modificaCollocazione(datiCollocazioneSprite2);
        }
    }

    public void updateImages() {
        for (SpriteEditor spriteEditor : listaSprites) {
            spriteEditor.setImmagine(Utils.getBufferedImage(Settings.CARTELLA_SPRITES +
                    "\\" + spriteEditor.getNome(), spriteEditor.getNome()));
        }
        immagineInizio = Utils.getBufferedImage(Settings.CARTELLA_SPRITES, nomeImmagineInizio);
        immagineFine = Utils.getBufferedImage(Settings.CARTELLA_SPRITES, nomeImmagineFine);
    }

    /**
     * Restituisce la cella nell'editor presente alle coordinate passate come parametro e relativamente all'anteprima
     *
     * @param x coordinata x relativa della cella da ottenere
     * @param y coordinata y relativa della cella da ottenere
     * @return la cella alle posizioni relative x e y
     */
    public Cella getCella(int x, int y) {
        return getCella(x, y, true);
    }

    /**
     * Restituisce la cella alle coordinate passate come parametro, se anteprima è true allora tali coordinate saranno relative
     * alla grandezza e alla posizione della parte visualizzata nell'anteprima
     *
     * @param x         coordinata x della cella da ottenere
     * @param y         coordinata y della cella da ottenere
     * @param anteprima se true allora prenderà la posizione relativa, altrimenti quella assoluta
     * @return la cella alla posizione indicata
     */
    public Cella getCella(int x, int y, boolean anteprima) {
        int dim = dimCella;
        if (anteprima) {
            dim = dimCellaAnteprima;
            x += xAnteprima;
            y += yAnteprima;
        }
        synchronized (listaCelle) {
            for (Cella cella : listaCelle) {
                if (cella.getX() == x / dim && cella.getY() == y / dim) return cella;
                if (cella.getX() > x / dim && cella.getX() < x / dim + 1 /*dim*/ &&
                        cella.getY() > y / dim && cella.getY() < y / dim + 1 /*+ dim*/) {
                    return cella;
                }
            }
        }
        return null;
    }

    private Cella getCellaRelativa(int x, int y) {
        synchronized (listaCelle) {
            for (Cella cella : listaCelle) {
                if (cella.getX() == x && cella.getY() == y) return cella;
            }
        }
        return null;
    }

    /**
     * il designer ha appena cliccato su una cella dell'anteprima,
     * se la "modalità posizionamento spriteSelezionato" è attiva lo spriteSelezionato attualmente selezionato viene spostato in questa cella,
     * altrimenti non succede nulla
     */
    public void posizionaSpriteSelezionato() {
        //System.out.println((spriteSelezionato != null) + " " +  (spriteSelezionato != player) + " " + (cellaSelezionata.getContenuto() == null));
        if (spriteSelezionato != null && spriteSelezionato != player && cellaSelezionata.getContenuto() == null) {
            spriteSelezionato.modificaPosizione(cellaSelezionata);
            if (!spriteSelezionato.isInMappa())
                inserisciSpriteSelezionatoNellaMappa();
        }
    }

    /**
     * il designer ha appena cliccato su una cella dell'anteprima,
     * se la "modalità posizionamento sprite" è attiva lo spriteSelezionato attualmente selezionato viene spostato in questa cella,
     * altrimenti non succede nulla
     */
    public void posizionaSpriteSelezionato(Cella cella) {
        if (cella != null && spriteSelezionato != null) {
            this.setCellaSelezionata(cella, false);
            posizionaSpriteSelezionato();
        }
    }

    /**
     * Crea una nuova istanza dello sprite attualmente selezionato e lo imposta come sprite attuale
     */
    public void duplicaSpriteSelezionato() {
        spriteSelezionato = new SpriteEditor(spriteSelezionato.getDatiSprite());
    }

    /**
     * lo spriteSelezionato selezionato viene inserito nella lista degli sprites contenuti nella mappa
     */
    private void inserisciSpriteSelezionatoNellaMappa() {
        synchronized (listaSprites) {
            listaSprites.add(spriteSelezionato);
        }
        if (spriteSelezionato.getDatiCollocazione() == null)
            spriteSelezionato.updateDatiCollocazione();
        spriteSelezionato.setIdCollocazione((int) spriteSelezionato.getDatiCollocazione().get("id"));

    }

    /**
     * elimina uno specifico sprite (quello selezionato) dalla mappa, ma non dal database
     */
    public void eliminaSpriteDallaMappa() {
        SpriteEditor sprite = cellaSelezionata.getContenuto();
        if (sprite != null && sprite.isInMappa()) {
            editorDAO.rimuoviCollocazioneSprite(sprite.getIdCollocazione());
            sprite.getAssociato().setContenuto(null);
            listaSprites.remove(sprite);
        }
    }

    /**
     * Elimina lo sprite attualmente selezionata dal database, richiamando il metodo di EditorDAO
     */
    public void eliminaSpriteDalDatabase() {
        eliminaSpriteUguali(spriteSelezionato.getNome());
        editorDAO.rimuoviSprite(spriteSelezionato);
    }

    public void eliminaSpriteUguali(String nome) {
        for (Cella cella : listaCelle) {
            if (cella.getContenuto() != null && cella.getContenuto().getNome().equals(nome)) {
                cella.setContenuto(null);
            }
        }
        for (SpriteEditor sprite : listaSprites) {
            if (sprite.getNome().equals(nome)) {
                listaSprites.remove(sprite);
            }
        }
    }

    public Image getImmagine() {
        return immagine;
    }

    public void setImmagine(Image immagine) {
        this.immagine = immagine;
    }

    public int getDimCella() {
        return dimCella;
    }

    public void setDimCella(int dimCella) {
        this.dimCella = dimCella;
    }

    public int getIdMappa() {
        return idMappa;
    }

    public SpriteEditor getPlayer() {
        return player;
    }

    public int getNumCelleAsse() {
        return numCelleAsse;
    }

    /**
     * Imposta le dimensioni della mappa, può alzare degli errori se nelle nuove dimensioni vengono esclusi alcuni sprite presenti nella mappa
     *
     * @param numCelleAsse altezza e larghezza della mappa
     * @return true se il ridimensionamento è andato a buon fine, false altrimenti
     */
    public boolean setNumCelleAsse(int numCelleAsse) {
        if (this.numCelleAsse == 0)
            this.numCelleAsse = (int) editorDAO.getDatiMappa(this.getMappa()).getFromLast("numCelleAsse");
        if (numCelleAsse < this.numCelleAsse)
            for (int i = numCelleAsse; i < this.numCelleAsse; i++) {
                for (int j = 0; j < this.numCelleAsse; j++) {
                    if (getCellaRelativa(i, j) != null && getCellaRelativa(i, j).getContenuto() != null) {
                        Utils.raiseError("Impossibile ridimensionare la mappa, sono presenti degli sprite al di fuori delle dimensioni scelte");
                        return false;
                    }
                }
            }
        if (numCelleAsse < cellaInizio.getX() || numCelleAsse < cellaInizio.getY()) {
            Utils.raiseError("La cella iniziale si trova oltre i limiti appena scelti");
            return false;
        }
        if (numCelleAsse < cellaFine.getX() || numCelleAsse < cellaFine.getY()) {
            Utils.raiseError("La cella finale si trova oltre i limiti appena scelti");
            return false;
        }
        this.numCelleAsse = numCelleAsse;
        inizializzaCelle();
        return true;
    }

    /**
     * Crea tutte le istanze delle celle all'interno dell'editor a seconda di quante celle ha la mappa
     * attualmente avviata
     */
    private void inizializzaCelle() {
        for (int i = 0; i < numCelleAsse; i++) {
            for (int j = 0; j < numCelleAsse; j++) {
                if (getCellaRelativa(i, j) == null) {
                    Cella cella = new Cella(i, j);
                    listaCelle.add(cella);
                }
            }
        }
    }

    /**
     * Imposta il waypoint (destro o sinitro) dello sprite attualmente selezionato.
     * Questa azione è possibile solo se lo sprite è di tipo Blocco o NPC.
     *
     * @param sprite    lo sprite di cui impostare il waypoint
     * @param waypoint1 true se il waypoint da impostare è il sinistro, false altrimenti
     */
    public void setWaypoint(SpriteEditor sprite, boolean waypoint1) {
        if (sprite != null) {
            HashMap<String, Object> datiCollocazione = sprite.getDatiCollocazione();
            datiCollocazione.put("xWaypoint" + (waypoint1 ? "1" : "2"), cellaSelezionata.getX() * dimCella);
            if (waypoint1) sprite.setWaypoint1(cellaSelezionata);
            else sprite.setWaypoint2(cellaSelezionata);
            sprite.modificaCollocazione(datiCollocazione);
        }
    }

    public Cella getCellaInizio() {
        return cellaInizio;
    }

    /**
     * Imposta la cella iniziale, quindi quella dove il player comparirà all'avvio della mappa
     *
     * @param cella la cella scelta da impostare
     */
    private void setCellaInizio(Cella cella) {
        Cella tmp;
        for (int i = 0; i < (numCelleAsse - 1) * (numCelleAsse - 1); i++) {
            tmp = listaCelle.get(i);
            if (tmp.getX() == cella.getX() && tmp.getY() == cella.getY())
                cellaInizio = tmp;
        }
        if (player != null) {
            if (player.getAssociato() != null)
                player.getAssociato().setContenuto(null);
            player.setAssociato(cellaInizio);
        }
    }

    public void setCellaInizio() {
        setCellaInizio(cellaSelezionata);
    }

    /**
     * Elimina lo sprite contenuto all'interno della cella attualmente selezionata
     */
    public void eliminaSpriteCella() {
        SpriteEditor sprite;
        if (cellaSelezionata != null && (sprite = cellaSelezionata.getContenuto()) != null) {
            editorDAO.rimuoviCollocazioneSprite(sprite.getIdCollocazione());
            cellaSelezionata.setContenuto(null);
            listaSprites.remove(sprite);
        }
    }

    public List<SpriteEditor> getListaSprites() {
        return listaSprites;
    }

    public Cella getCellaFine() {
        return cellaFine;
    }

    private void setCellaFine(Cella cella) {
        Cella tmp;
        for (int i = 0; i < numCelleAsse * numCelleAsse; i++) {
            tmp = listaCelle.get(i);
            if (tmp.getX() == cella.getX() && tmp.getY() == cella.getY()) {
                cellaFine = tmp;
            }
        }
    }

    public void setCellaFine() {
        setCellaFine(cellaSelezionata);
    }

    public List<Cella> getListaCelle() {
        return listaCelle;
    }

    public String getNomeImmagineInizio() {
        return nomeImmagineInizio;
    }

    /**
     * Imposta l'immagine del luogo di partenza della mappa
     *
     * @param nomeImmagineInizio
     */
    public void setNomeImmagineInizio(String nomeImmagineInizio) {
        this.nomeImmagineInizio = nomeImmagineInizio;
        immagineInizio = Utils.getBufferedImage(Settings.CARTELLA_SPRITES, nomeImmagineInizio);
        editorDAO.setImmagineInizioFine(nomeImmagineInizio, mappa, false);

    }

    public String getNomeImmagineFine() {
        return nomeImmagineFine;
    }

    public void setNomeImmagineFine(String nomeImmagineFine) {
        this.nomeImmagineFine = nomeImmagineFine;
        immagineFine = Utils.getBufferedImage(Settings.CARTELLA_SPRITES, nomeImmagineFine);
        editorDAO.setImmagineInizioFine(nomeImmagineFine, mappa, true);
    }

    public Cella getCellaSelezionata() {
        return cellaSelezionata;
    }

    /**
     * Imposta la cella attualmente selezionata con quella passata come parametro.
     *
     * @param cellaSelezionata la cella da selezionare
     * @param selezionaSprite  se true allora verrà impostato anche lo sprite attualmente selezionato con il contenuto della cella
     */
    public void setCellaSelezionata(Cella cellaSelezionata, boolean selezionaSprite) {
        if (cellaSelezionata != null) {
            if (this.cellaSelezionata != null) {
                this.cellaSelezionata.setSelezionato(false);
                if (this.cellaSelezionata.getContenuto() != null &&
                        this.cellaSelezionata.getContenuto().getWaypoint1() != null &&
                        this.cellaSelezionata.getContenuto().getWaypoint2() != null) {
                    this.cellaSelezionata.getContenuto().getWaypoint1().setWaypoint(false);
                    this.cellaSelezionata.getContenuto().getWaypoint2().setWaypoint(false);
                }
            }
            this.cellaSelezionata = cellaSelezionata;
            this.cellaSelezionata.setSelezionato(true);
            if (selezionaSprite && this.cellaSelezionata.getContenuto() != null) {
                this.cellaSelezionata.getContenuto().updateDatiCollocazione();
                if (this.cellaSelezionata.getContenuto().getWaypoint1() != null &&
                        this.cellaSelezionata.getContenuto().getWaypoint2() != null) {
                    if (this.cellaSelezionata.getContenuto().getWaypoint1() != this.cellaSelezionata.getContenuto().getWaypoint2()) {
                        this.cellaSelezionata.getContenuto().getWaypoint1().setWaypoint(true);
                        this.cellaSelezionata.getContenuto().getWaypoint2().setWaypoint(true);
                    }
                }
                this.setSpriteSelezionato(cellaSelezionata.getContenuto());
            }
        }
    }

    public SpriteEditor getSpriteSelezionato() {
        return spriteSelezionato;
    }

    public void setSpriteSelezionato(SpriteEditor spriteSelezionato) {
        this.spriteSelezionato = spriteSelezionato;
    }

    public Mappa getMappa() {
        return mappa;
    }

    public int getModalitaAttuale() {
        return modalitaAttuale;
    }

    public void setModalitaAttuale(int modalitaAttuale) {
        this.modalitaAttuale = modalitaAttuale;
    }

    public void setModStandard() {
        this.modalitaAttuale = MOD_STANDARD;
    }

    public int getxAnteprima() {
        return xAnteprima;
    }

    public void setxAnteprima(int xAnteprima) {
        this.xAnteprima = xAnteprima;
    }

    public int addxAnteprima(int value) {
        return this.xAnteprima += value;
    }

    public int getyAnteprima() {
        return yAnteprima;
    }

    public void setyAnteprima(int yAnteprima) {
        this.yAnteprima = yAnteprima;
    }

    public int addyAnteprima(int value) {
        return this.yAnteprima += value;
    }

    public int getDimCellaAnteprima() {
        return dimCellaAnteprima;
    }

    public void setDimCellaAnteprima(int dimCellaAnteprima) {
        this.dimCellaAnteprima = dimCellaAnteprima;
    }

    public int getAltezzaAnteprima() {
        return altezzaAnteprima;
    }

    public int getLunghezzaAnteprima() {
        return lunghezzaAnteprima;
    }

    public void setAltezzaAnteprima(int dimAnteprima) {
        this.altezzaAnteprima = dimAnteprima;
    }

    public void setLunghezzaAnteprima(int dimAnteprima) {
        this.lunghezzaAnteprima = dimAnteprima;
    }

    public int getZoomAnteprima() {
        return zoomAnteprima;
    }

    public Image getImmagineInizio() {
        return immagineInizio;
    }

    public void setImmagineInizio(Image immagineInizio) {
        this.immagineInizio = immagineInizio;
    }

    public Image getImmagineFine() {
        return immagineFine;
    }

    public void setImmagineFine(Image immagineFine) {
        this.immagineFine = immagineFine;
    }

    private void setZoomAnteprima(int zoomAnteprima) {
        if (zoomAnteprima <= 0) this.zoomAnteprima = 0;
        else if (zoomAnteprima <= 100) this.zoomAnteprima = zoomAnteprima;
        else this.zoomAnteprima = 100;
    }

    /**
     * Permette di sistemare lo zoom e di evitare quindi problemi grafici evidenti nell'editor
     * come posizione della parte visualizzabile dell'anteprima
     */
    public void adjustZoomAnteprima() {
        int posXMax = this.getDimCellaAnteprima() * this.getNumCelleAsse() - this.getLunghezzaAnteprima();
        int posYMax = this.getDimCellaAnteprima() * this.getNumCelleAsse() - this.getAltezzaAnteprima();

        if (this.getxAnteprima() >= posXMax)
            this.setxAnteprima(posXMax);
        if (this.getyAnteprima() >= posYMax)
            this.setyAnteprima(posYMax);
        if (this.getxAnteprima() < 0)
            this.setxAnteprima(0);
        if (this.getyAnteprima() < 0)
            this.setyAnteprima(0);
    }

    /**
     * Ordina la lista delle celle in base al loro contenuto, per ottimizzare le prestazioni
     * durante il paintComponent() di EditorAnteprimaView
     */
    public void ordinaListaCelle() {
        synchronized (listaCelle) {
            Collections.sort(listaCelle);
        }

    }

    /**
     * Aggiunge un valore allo zoom dell'anteprima
     *
     * @param value
     */
    public void addZoomAnteprima(int value) {
        setZoomAnteprima(zoomAnteprima + value);
    }
}