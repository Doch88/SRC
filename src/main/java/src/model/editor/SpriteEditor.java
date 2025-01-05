package src.model.editor;

import src.model.DAO.EditorDAO;
import src.model.DAO.SpriteEditorDAO;
import src.model.Model;
import src.utils.Settings;
import src.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Classe che corrisponde allo sprite inserito all'interno della mappa tramite editor.
 * Lo sprite dell'editor, a differenza di quello della piattaforma, avrà i valori definiti da
 * due HashMap.
 * Queste due HashMap conterranno le informazioni dello sprite, come attacco, salute e tempo di riposo, e il nome e l'ID.
 * Si è preferito ricorrere a questa strategia poiché l'utilizzo di diverse classi o di diversi attributi sarebbe risultato
 * inutilmente ridondante nel primo caso e troppo rigido nel secondo. Dunque si è preferito favorire una elasticità maggiore
 * per permettere eventuali modifiche e aggiunte di tipologie di sprite e caratteristiche.
 */
public class SpriteEditor extends Model {

    private HashMap<String, Object> datiSprite;
    private HashMap<String, Object> datiCollocazione;
    private final String tipoSprite;
    private Image immagine;
    private boolean inMappa = false;

    private final SpriteEditorDAO spriteDAO = SpriteEditorDAO.getInstance();

    private int idCollocazione = -1;

    private Cella associato;

    private final Editor editor;

    private Cella waypoint1 = null;
    private Cella waypoint2 = null;

    /**
     * Crea uno sprite vuoto, completamente da zero.
     * Tale sprite prenderà la struttura generale dal database e avrà valori di base, definiti dalle regole del database.
     *
     * @param nomeSprite nome del nuovo sprite
     * @param tipoSprite tipologia del nuovo sprite
     */
    public SpriteEditor(String nomeSprite, String tipoSprite) {
        editor = Editor.getInstance();
        this.nome = nomeSprite;
        this.tipoSprite = tipoSprite;
        this.datiSprite = spriteDAO.getStructure();
        setImmagine(Utils.getBufferedImage(Settings.CARTELLA_SPRITES + "\\" + nome, nome));
    }

    /**
     * Viene importato dal database lo sprite con il nome corrispondente e quindi le relative proprietà,
     * se il nome non è presente nel database lo sprite viene creato con le proprietà di default
     * PRE: nomeSprite è relativo ad uno sprite già esistente nel database
     */
    public SpriteEditor(String nomeSprite) {
        editor = Editor.getInstance();
        this.nome = nomeSprite;
        if ((spriteDAO.exists(nomeSprite))) {
            datiSprite = spriteDAO.getDati(nome);
            tipoSprite = (String) datiSprite.get("tipo");
        } else {
            this.datiSprite = spriteDAO.getStructure();
            tipoSprite = "block";
        }
        setImmagine(Utils.getBufferedImage(Settings.CARTELLA_SPRITES + "\\" + nome, nome));
    }

    /**
     * Viene importato dal database lo sprite con le proprietà passate come parametri,
     * PRE: le proprietà si riferiscono ad uno sprite già esistente nel database,
     * infatti vengono prelevate grazie ad una query già effettuata
     */
    public SpriteEditor(HashMap<String, Object> datiSprite) {
        editor = Editor.getInstance();
        this.datiSprite = datiSprite;

        nome = (String) datiSprite.get("nome");
        tipoSprite = (String) datiSprite.get("tipo");
        setImmagine(Utils.getBufferedImage(Settings.CARTELLA_SPRITES + "\\" + nome, nome));
    }

    public void modificaSprite(HashMap<String, Object> parametri) {
        this.datiSprite = parametri;
        spriteDAO.updateByNome(this.getNome(), this);
    }

    public void modificaCollocazione(HashMap<String, Object> parametri) {
        datiCollocazione = parametri;
        spriteDAO.updateByNome(this.getNome(), this);
    }

    /**
     * Modifica la posizione attuale dello sprite. Nel caso lo sprite non sia all'interno della mappa allora verrano eseguite
     * delle query per modificare la sua collocazione e per memorizzare la nuova posizione, altrimenti le uniche query eseguite saranno quelle relative
     * alle coordinate x e y dello sprite. Prima di usare il metodo setAssociato() per modificare la cella associata, verrà svuotata la precedente
     * cella, se presente.
     *
     * @param nuovaPosizione
     */
    public void modificaPosizione(Cella nuovaPosizione) {
        int spriteX = getSpriteX(nuovaPosizione);
        int spriteY = getSpriteY(nuovaPosizione);

        if (!isInMappa()) {
            spriteDAO.aggiungiAllaMappa(this, editor.getIdMappa(), spriteX, spriteY);
            updateDatiCollocazione();
        } else {
            datiCollocazione.put("x", spriteX);
            datiCollocazione.put("y", spriteY);
            spriteDAO.updateByNome(nome, this);
        }
        if (associato != null)
            associato.setContenuto(null);
        this.setAssociato(nuovaPosizione);
    }

    /**
     * Ottiene la coordinata x assoluta dello sprite.
     * Sarà la posizione che verrà inserita nel database, ossia quella che permetterà di disegnare lo sprite nella posizione giusta.
     *
     * @param posizione la cella dove è posizionato lo sprite
     * @return la coordinata x assoluta
     */
    public int getSpriteX(Cella posizione) {
        int adjValue = immagine.getWidth(null) / 2 + editor.getDimCella() / 2;
        if (adjValue > editor.getDimCella() / 2) adjValue = 0;
        return posizione.getX() * editor.getDimCella() - adjValue;
    }

    /**
     * Ottiene la coordinata y assoluta dello sprite.
     * Sarà la posizione che verrà inserita nel database, ossia quella che permetterà di disegnare lo sprite nella posizione giusta.
     *
     * @param posizione la cella dove è posizionato lo sprite
     * @return la coordinata y assoluta
     */
    public int getSpriteY(Cella posizione) {
        int adjValue = immagine.getWidth(null) / 2 + editor.getDimCella() / 2;
        if (adjValue > editor.getDimCella() / 2) adjValue = 0;
        return posizione.getY() * editor.getDimCella() - adjValue;
    }

    /**
     * Aggiorna i dati in possesso dal programma relativi alla collocazione dello sprite all'interno della mappa.
     * Tali informazioni verranno estratte dal database e nel caso di uno sprite di tipo Player verranno ignorate.
     */
    public void updateDatiCollocazione() {
        if (datiSprite != null && datiSprite.get("tipo").equals("player")) return;
        if (idCollocazione == -1 && this.associato != null)
            idCollocazione = spriteDAO.getIDCollocazione(this, editor.getIdMappa());
        if (spriteDAO.existsCollocazione(idCollocazione))
            datiCollocazione = spriteDAO.getDatiCollocazione(idCollocazione);
        if (associato != null) {
            Point waypoint1 = EditorDAO.getInstance().getWaypoint(this, 1);
            Point waypoint2 = EditorDAO.getInstance().getWaypoint(this, 2);
            this.waypoint1 = editor.getCella(waypoint1.x, getSpriteY(associato), false);
            this.waypoint2 = editor.getCella(waypoint2.x, getSpriteY(associato), false);
        }
    }

    public HashMap<String, Object> getDatiCollocazione() {
        if (idCollocazione == -1) updateDatiCollocazione();
        return datiCollocazione;
    }

    /**
     * Ottiene la cella associata allo sprite, ossia quella corrispondente alla sua posizione attuale
     *
     * @return la cella corrispondente
     */
    public Cella getAssociato() {
        return associato;
    }

    /**
     * Imposta la cella associata allo sprite, ossia quella corrispondente alla sua posizione attuale.
     *
     * @param associato la cella dove verrà collocato lo sprite
     */
    public void setAssociato(Cella associato) {
        this.associato = associato;
        if (associato != null && associato.getContenuto() != this)
            associato.setContenuto(this);
    }

    public Image getImmagine() {
        return immagine;
    }

    public HashMap<String, Object> getDatiSprite() {
        return datiSprite;
    }

    public void setDatiSprite(HashMap<String, Object> m) {
        datiSprite = m;
    }

    public int getIdCollocazione() {
        return idCollocazione;
    }

    public String[] getNomeAttributi() {
        return spriteDAO.getColumns();
    }

    public void setIdCollocazione(int idCollocazione) {
        this.idCollocazione = idCollocazione;
        if (idCollocazione != -1)
            inMappa = true;
    }

    /**
     * Controlla se lo sprite è attualmente collocato all'interno della mappa.
     */
    public boolean isInMappa() {
        if (idCollocazione == -1) inMappa = false;
        return inMappa;
    }

    /**
     * Imposta l'immagine corrispondente allo sprite.
     * Se non è associata nessuna immagine verrà associata una immagine monocolore per evitare problemi quando si andrà a cercare l'oggetto
     * all'interno dell'editor.
     *
     * @param immagine l'immagine da associare allo sprite
     */
    public void setImmagine(Image immagine) {
        if (immagine == null) {
            this.immagine = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = ((BufferedImage) this.immagine).createGraphics();
            g.setColor(Color.RED);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            g.fillRect(0, 0, 10, 10);
            g.dispose();
        } else
            this.immagine = immagine;
    }

    public String getTipoSprite() {
        return tipoSprite;
    }

    public Cella getWaypoint1() {
        return waypoint1;
    }

    public void setWaypoint1(Cella waypoint1) {
        this.waypoint1 = Editor.getInstance().getCella(waypoint1.getX(), associato.getY());
    }

    public Cella getWaypoint2() {
        return waypoint2;
    }

    public void setWaypoint2(Cella waypoint2) {
        this.waypoint2 = Editor.getInstance().getCella(waypoint2.getX(), associato.getY());
    }

    @Override
    public String toString() {
        return "SpriteEditor{" +
                "tipoSprite='" + tipoSprite + '\'' +
                ", inMappa=" + inMappa +
                ", idCollocazione=" + idCollocazione +
                '}';
    }
}