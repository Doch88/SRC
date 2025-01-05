package src.controller.piattaforma;

import src.controller.editor.EditorController;
import src.controller.menu.MenuPiattaformaMappeController;
import src.model.DAO.PiattaformaDAO;
import src.model.DAO.SalvataggioDAO;
import src.model.Mappa;
import src.model.Salvataggio;
import src.model.piattaforma.Animazione;
import src.model.piattaforma.Piattaforma;
import src.model.piattaforma.Sprite.AlertPoint;
import src.model.piattaforma.Sprite.Sprite;
import src.utils.Settings;
import src.utils.Utils;
import src.view.piattaforma.PiattaformaGameOverView;
import src.view.piattaforma.PiattaformaView;
import src.view.piattaforma.PiattaformaWinView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Il controller della piattaforma, gestirà l'esecuzione del gioco e l'avanzare del ciclo di update.
 */
public class PiattaformaController {

    private static PiattaformaController instance = null;

    private final PiattaformaKeyboardController piattaformaKeyboardController;

    private final Piattaforma piattaforma;
    private final PiattaformaDAO piattaformaDAO;

    private final PiattaformaView view;
    private final PiattaformaGameOverView gameOverView;
    private final PiattaformaWinView winView;

    private int contatoreFrames;

    /**
     * carica il listener della tastiera, i model e le view
     */
    public PiattaformaController() {
        piattaformaKeyboardController = PiattaformaKeyboardController.getInstance();

        piattaforma = Piattaforma.getInstance();
        piattaformaDAO = PiattaformaDAO.getInstance();

        view = PiattaformaView.getInstance();
        gameOverView = PiattaformaGameOverView.getInstance();
        winView = PiattaformaWinView.getInstance();
    }

    public static PiattaformaController getInstance() {
        if (instance == null)
            instance = new PiattaformaController();
        return instance;
    }

    /**
     * carica il salvataggio dall'ultima mappa
     *
     * @param salvataggio
     */
    public void init(Salvataggio salvataggio) {
        this.init(salvataggio, salvataggio.getMappa(salvataggio.getPosizioneMappaAttuale()));
    }

    /**
     * carica una mappa di un salvataggio, quindi:
     * - azzera i collezionabili della mappa selezionata
     * - (re)inizializza la view
     * - avvia il loop contenuto nel metodo update()
     *
     * @param salvataggio
     * @param mappa
     */
    public void init(Salvataggio salvataggio, Mappa mappa) {
        if (salvataggio.getCollezionabili().size() >= mappa.getPosizione())
            salvataggio.getCollezionabili().set(mappa.getPosizione() - 1, new HashMap<>());

        piattaforma.init(salvataggio, mappa, piattaformaDAO.getPlayer(salvataggio.getGioco(), mappa));

        salvataggio.caricaPunteggio();

        Animazione.setFramerate(salvataggio.getGioco().getFramerate());

        contatoreFrames = 0;
        if (piattaforma.getPlayer() != null) {
            Utils.addKeyListener(piattaformaKeyboardController);

            piattaforma.setSprites(piattaformaDAO.getSpritesInMappa(piattaforma.getMappa()));

            view.createBackgroundAnimation(SalvataggioDAO.getInstance().getMappaCorrente(salvataggio.getGioco(), mappa.getPosizione()).getNome());

            piattaforma.setAnimazionePuntoIniziale(piattaformaDAO.getAnimazioneInizioFine(salvataggio.getGioco().getNome(), piattaforma.getMappa().getPosizione(), false));
            piattaforma.setAnimazionePuntoFinale(piattaformaDAO.getAnimazioneInizioFine(salvataggio.getGioco().getNome(), piattaforma.getMappa().getPosizione(), true));

            piattaforma.setStatoPartitaInCorso();

            Thread loop = new Thread(this::update);
            loop.start();
        } else
            piattaforma.setStatoPartitaGameover();
    }

    /**
     * entra nella piattaforma dall'editor in modalità test, ciò servirà per controllare se una determinata mappa modificata
     * dall'editor funziona come dovrebbe
     *
     * @param mappa la mappa da far partire nella piattaforma
     */
    public void initTest(Mappa mappa) {
        piattaforma.setStatoPartitaTest();

        piattaforma.setMappa(mappa);
        Salvataggio salvataggio = new Salvataggio(mappa.getGioco(), mappa.getPosizione(),
                -1, 0, new ArrayList<>());

        this.init(salvataggio);
    }

    /**
     * Ciclo di aggiornamento dello schermo
     */
    public void update() {
        int millisec = 1000 / piattaforma.getSalvataggio().getGioco().getFramerate();
        view.init();

        while (piattaforma.isStatoPartitaInCorso()) {
            if (contatoreFrames++ == Settings.PIATTAFORMA_FISICA_RITARDO) {
                piattaforma.getPlayer().applicaFisica();
                contatoreFrames = 0;
            }

            synchronized (piattaforma.getSprites()) {
                for (Sprite sprite : piattaforma.getSprites()) {
                    sprite.muovi();
                    sprite.update();
                }
                for (Sprite sprite : piattaforma.getSprites()) {
                    if (sprite.isDead()) {
                        for (Sprite spr : sprite.getOggettiDaDroppare()) {
                            spr.setPosition(sprite.getX(), sprite.getY());
                            piattaforma.aggiungiSprite(spr);
                        }
                        piattaforma.getSalvataggio().addPunteggio(sprite.getPunteggioFornito());
                        piattaforma.rimuoviSprite(sprite);
                    }
                    if (sprite instanceof AlertPoint && ((AlertPoint) sprite).isTextVisible())
                        view.setMessaggio(((AlertPoint) sprite).getText());
                }
            }

            piattaforma.checkGameover();

            piattaforma.updateListaSprites();

            view.repaint();

            try {
                Thread.sleep(millisec);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (piattaforma.getStatoPartita() == Piattaforma.STATO_PARTITA_GAMEOVER)
            gameOverView.init();
        else if (piattaforma.getStatoPartita() == Piattaforma.STATO_PARTITA_LIVELLO_COMPLETATO)
            winView.init(!this.livelloSuccessivo(piattaforma.getSalvataggio()));
        else if (piattaforma.getStatoPartita() == Piattaforma.STATO_PARTITA_FINE_TEST)
            EditorController.getInstance().init(piattaforma.getMappa());
        else if (piattaforma.getStatoPartita() == Piattaforma.STATO_PARTITA_USCITA_MANUALE) {
            MenuPiattaformaMappeController.getInstance().init(piattaforma.getSalvataggio());
        }
    }

    /**
     * Cambia il livello attuale del salvataggio
     *
     * @param salvataggio salvataggio da modificare
     * @return false se la mappa è l'ultima del gioco, true se esiste un'altra mappa successiva a quella attuale
     */
    private boolean livelloSuccessivo(Salvataggio salvataggio) {
        HashMap<String, Object> result = piattaformaDAO.getMappaSuccessiva(salvataggio.getGioco().getNome(), piattaforma.getMappa().getPosizione());
        if (result != null) {
            if (salvataggio.getPosizioneMappaAttuale() <= piattaforma.getMappa().getPosizione())
                salvataggio.setPosizioneMappaAttuale((int) result.get("posizione"));

            piattaformaDAO.changeSave(salvataggio, piattaforma.getMappa().getPosizione());
            return true;
        } else {
            salvataggio.setPosizioneMappaAttuale(piattaforma.getMappa().getPosizione() + 1);
            piattaformaDAO.changeSave(salvataggio, piattaforma.getMappa().getPosizione());
            piattaforma.setStatoPartitaGameover();
            return false;
        }
    }
}
