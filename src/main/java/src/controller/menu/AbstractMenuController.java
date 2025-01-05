package src.controller.menu;

import src.model.DAO.DAO;
import src.model.Model;
import src.model.Salvataggio;
import src.utils.Utils;
import src.view.menu.AbstractMenuView;
import src.view.menu.elemPanelView.ElemPanelView;
import src.view.menu.nuovoElemView.AbstractNuovoElemView;

import java.util.ArrayList;

/**
 * classe astratta che viene estesa dal controller del menu dell'editor
 * e dal controller del menu della piattaforma
 */
public abstract class AbstractMenuController {

    protected DAO dao;
    protected ArrayList<Model> listaElem = null;
    private Model elemMenuCorrente;

    protected AbstractMenuView view;
    protected AbstractNuovoElemView nuovoElemView;

    /**
     * inizializza il menu visualizzando l'apposita finestra con la lista di giochi, mappe o salvataggi
     * la view deve essere stata inizializzata per eseguire questo metodo
     */
    public void init(Model elemMenuCorrente) {
        this.elemMenuCorrente = elemMenuCorrente;

        //inizializziamo la lista degli elementi
        listaElem = null;
        try {
            listaElem = dao.getAll(elemMenuCorrente instanceof Salvataggio ? ((Salvataggio) elemMenuCorrente).getGioco() : elemMenuCorrente);
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.setHeaderPanel(elemMenuCorrente);
        view.clearListaElemPanel();
        //inizializziamo il menu
        for (Model elem : listaElem) {
            ElemPanelView elemPanel = view.addElemPanel(elemMenuCorrente, elem);
            this.addElemButtonListeners(elemPanel);
        }
        view.updateListaElemPanel();
        Utils.updateView(view);
    }

    /**
     * aggiorna la finestra corrente richiamando il metodo init
     */
    protected void update() {
        this.init(elemMenuCorrente);
    }

    /**
     * aggiunge ulteriori listener ad eventuali bottoni contenuti negli elementi
     * della lista visualizzata (che dipendono dal particolare tipo di menu)
     *
     * @param elemPanel elemento della lista al quale vanno aggiunti i listener
     */
    protected abstract void addElemButtonListeners(ElemPanelView elemPanel);

    /**
     * torna al menu precedente al click sull'apposito bottone
     */
    protected abstract void indietroAction();

    /**
     * elimina un elemento della lista dal database al click su un apposito bottone, quindi ricarica il menu
     *
     * @param nomeButton nome del bottone appena cliccato, da cui dipende l'elemento da eliminare
     */
    protected void removeElemAction(String nomeButton) {
        try {
            dao.removeByNome(nomeButton, elemMenuCorrente);
            this.update();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected Model getElemMenuCorrente() {
        return elemMenuCorrente;
    }

    protected void setElemMenuCorrente(Model elemMenuCorrente) {
        this.elemMenuCorrente = elemMenuCorrente;
    }

}