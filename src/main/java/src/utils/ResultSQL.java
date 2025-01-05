package src.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe le cui istanze vengono restituite dal metodo query di Database,
 * permette la gestione dei risultati di una query
 */
public class ResultSQL {
    private final ArrayList<HashMap<String, Object>> data;

    /**
     * Inizializza ResultSQL inizializzando la struttura dati contenente la tabella
     */
    public ResultSQL() {
        data = new ArrayList<>();
    }

    /**
     * Aggiunge all'ultima riga la colonna passata come parametro
     *
     * @param str chiave da aggiungere
     * @param obj valore relativo
     */
    public void addToLast(String str, Object obj) {
        if (!data.isEmpty()) {
            data.get(data.size() - 1).put(str, obj);
        }

    }

    /**
     * Aggiunge una riga vuota alla fine
     */
    public void addRow() {
        data.add(new HashMap<>());
    }

    /**
     * Restituisce tutti gli elementi di ogni riga corrispondenti colonna passata come parametro
     *
     * @param str chiave della colonna
     * @return lista di generici oggetti indicanti i valori della colonna
     */
    public ArrayList<Object> getColumn(String str) {
        ArrayList<Object> values = new ArrayList<>();
        for (HashMap<String, Object> row : data) {
            if (row.containsKey(str))
                values.add(row.get(str));
        }
        return values;
    }

    /**
     * Estrae un determinato valore dalla riga e colonna corrispondente
     *
     * @param rowNum numero di riga da cui estrarre il valore
     * @param str    colonna da cui estrarre il valore
     * @return un Object indicante il valore ricercato o null se non è stato trovato
     */
    public Object getValue(int rowNum, String str) {
        int i = 0;
        for (HashMap<String, Object> row : data) {
            if (i++ == rowNum && row.containsKey(str))
                return row.get(str);
        }
        return null;
    }

    /**
     * Rimuove un determinato valore dalla rigae colonna corrispondente
     *
     * @param rowNum numero di riga da cui rimuovere il valore
     * @param str    colonna da cui rimuovere il valore
     */
    public void removeValue(int rowNum, String str) {
        int i = 0;
        for (HashMap<String, Object> row : data) {
            if (i++ == rowNum)
                row.remove(str);
        }
    }

    /**
     * Restituisce i valori corrispondenti alla riga passata come parametro
     *
     * @param num numero della riga da estrarre
     * @return un HashMap indicante tutti i valori della riga
     */
    public HashMap<String, Object> getRow(int num) {
        return data.get(num);
    }

    /**
     * @return il numero di righe all'interno
     */
    public int getRowsNum() {
        return data.size();
    }

    /**
     * @return il numero di colonne della prima riga, si da per scontato che ogni riga abbia le stesse colonne
     */
    public int getColumnsNum() {
        return data.isEmpty() ? 0 : data.get(0).size();
    }

    /**
     * Prende dall'ultima riga la colonna passata come parametro
     *
     * @param str colonna da ricercare
     * @return un Object corrispondente al valore dell'ultima riga alla colonna str
     */
    public Object getFromLast(String str) {
        if (!data.isEmpty()) {
            return data.get(data.size() - 1).get(str);
        } else
            return null;
    }

    /**
     * @return un HashMap contenente l'ultima riga del risultato
     */
    public HashMap<String, Object> getLastRow() {
        if (!data.isEmpty()) {
            return data.get(data.size() - 1);
        } else
            return null;
    }

    /**
     * Controlla se esiste la colonna passata come parametro in almeno una riga
     *
     * @param str la colonna da verificare
     * @return true se esiste, false altrimenti
     */
    public boolean existsColumn(String str) {
        for (HashMap<String, Object> row : data) {
            if (row.containsKey(str))
                return true;
        }
        return false;
    }

}
