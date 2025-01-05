package src.controller.piattaforma;

import src.controller.menu.MenuPiattaformaMappeController;
import src.model.piattaforma.Piattaforma;
import src.model.piattaforma.Sprite.Player;
import src.model.piattaforma.Sprite.Proiettile;
import src.model.piattaforma.Sprite.Sprite;
import src.utils.Utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controller che gestisce l'input nella piattaforma.
 */
public class PiattaformaKeyboardController implements KeyListener {

    private static PiattaformaKeyboardController instance = null;

    private final Piattaforma piattaforma;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private boolean xPressed = false;

    private PiattaformaKeyboardController() {
        piattaforma = Piattaforma.getInstance();
    }

    public static PiattaformaKeyboardController getInstance() {
        if (instance == null)
            instance = new PiattaformaKeyboardController();
        return instance;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Player player = piattaforma.getPlayer();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setVelocita(Sprite.Direction.RIGHT, player.getVelocita(), -player.getVelocita(), player.getVelocita());
            rightPressed = true;
            player.setMovimento(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.setVelocita(Sprite.Direction.LEFT, player.getVelocita(), -player.getVelocita(), player.getVelocita());
            leftPressed = true;
            player.setMovimento(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Proiettile proiettile = player.sparaProiettile();
            if (proiettile != null)
                piattaforma.aggiungiSprite(proiettile);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && player.isOnGround())
            player.salta();
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE &&
                (piattaforma.getStatoPartita() == Piattaforma.STATO_PARTITA_IN_CORSO ||
                        piattaforma.getStatoPartita() == Piattaforma.STATO_PARTITA_TEST)) {
            piattaforma.setStatoPartitaUscitaManuale();
            Utils.removeKeyListener(this);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && piattaforma.getStatoPartita() == Piattaforma.STATO_PARTITA_GAMEOVER) {
            MenuPiattaformaMappeController.getInstance().init(piattaforma.getSalvataggio());
            Utils.removeKeyListener(this);
        }
        if (e.getKeyCode() == KeyEvent.VK_X && piattaforma.collisionePuntoFine())
            piattaforma.setStatoPartitaLivelloCompletato();
        if (e.getKeyCode() == KeyEvent.VK_X && piattaforma.collisionePortale() != null && !xPressed) {
            piattaforma.collisionePortale().teletrasporta(player);
            xPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && piattaforma.getStatoPartita() == Piattaforma.STATO_PARTITA_LIVELLO_COMPLETATO) {
            Utils.removeKeyListener(this);
            PiattaformaController.getInstance().init(piattaforma.getSalvataggio());
        }
    }

    public void keyReleased(KeyEvent e) {
        Player player = piattaforma.getPlayer();
        if (piattaforma.isStatoPartitaInCorso()) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                rightPressed = false;
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
                leftPressed = false;
            if (!rightPressed && !leftPressed)
                player.setMovimento(false);
            if (e.getKeyCode() == KeyEvent.VK_SPACE)
                player.setSparando(false);
            if (e.getKeyCode() == KeyEvent.VK_X)
                xPressed = false;
        }
    }

}
