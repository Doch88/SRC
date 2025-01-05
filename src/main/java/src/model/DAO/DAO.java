package src.model.DAO;

import src.model.Model;

import java.util.ArrayList;

/**
 * Permette di interfacciarsi col database, potendo quindi aggiungere, modificare o eliminare i dati all'interno.
 * Ogni DAO sarà relativo ad una determinata tabella del Database, definita dal Model associato ad esso
 *
 * @param <E> il tipo di Model che il DAO andrà a modificare all'interno del Database
 */
public interface DAO<E extends Model> {

    /**
     * Aggiunge un elemento al database.
     *
     * @param elem l'elemento da aggiungere
     */
    void add(E elem);

    /**
     * Aggiorna un elemento all'interno del database basandosi sul nome.
     *
     * @param nome il nome dell'elemento all'interno del database
     * @param elem l'istanza contenente i dati da modificare
     * @return true se il processo è andato a buon fine, false altrimenti
     */
    boolean updateByNome(String nome, E elem);

    /**
     * Rimuove un elemento dal database
     *
     * @param elem l'elemento da rimuovere
     */
    void remove(E elem);

    /**
     * Rimuove un elemento basandosi sul nome
     *
     * @param nome  il nome dell'elemento da rimuovere
     * @param model
     */
    void removeByNome(String nome, Model... model);

    /**
     * Controlla se un elemento esiste all'interno del database
     *
     * @param elem l'elemento da ricercare
     * @return true se esiste, false altrimenti
     */
    boolean exists(E elem);

    /**
     * Ottiene tutti gli elementi di tipo E relativi ad un determinato Model
     *
     * @param model il model a cui sono relativi
     * @return un ArrayList contenente gli elementi
     */
    ArrayList<E> getAll(Model model);

    /**
     * Ottiene un unico elemento dal database relativo ad altri model, basandosi sul nome
     *
     * @param nome  il nome dell'elemento da ottenere
     * @param model i model relativi all'elemento
     * @return l'elemento da ottenere
     */
    E getByNome(String nome, Model... model);

}