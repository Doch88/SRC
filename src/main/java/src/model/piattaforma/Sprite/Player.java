package src.model.piattaforma.Sprite;


import src.model.DAO.PiattaformaDAO;
import src.model.piattaforma.Animazione;
import src.model.piattaforma.Collisione;
import src.model.piattaforma.ListaCollisioni;
import src.utils.Settings;
import src.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Classe che indicher&agrave; il player principale di una partita
 */

public class Player extends Sprite {

    /**
     * La salute massima del giocatore, sarà sempre maggiore di zero
     */
    private final int saluteMassima;

    /**
     * La salute attuale del giocatore, raggiunto un valore minore o uguale a zero si avrà il gameover
     */
    private int saluteAttuale;

    /**
     * Il danno che farà il player saltando sopra un NPC
     */
    private int attacco;

    /**
     * L'oggetto che conterrà l'animazione del salto e relativo timer
     */
    private Animazione animazioneSalto;
    /**
     * L'oggetto che conterrà l'animazione dello sparo e relativo timer
     */
    private Animazione animazioneSparo;

    private Animazione animazioneDannoSubito;

    /**
     * Numero di proiettili attualmente a disposizione
     */
    private int numeroProiettili;

    /**
     * Direzione in cui il player è rivolto, sarà la direzione in cui sarà sparato un eventuale proiettile
     */
    private Sprite.Direction direzione;

    /**
     * Il periodo di riposo è il tempo che deve passare affinché il player possa essere ferito nuovamente
     */
    private final int periodoDiRiposo;
    private int riposoAttuale;

    private Proiettile proiettile;

    private final String nomeProiettile;

    private int periodoDiRiposoSparo;
    private int riposoAttualeSparo;

    /**
     * Se si sta prendendo un tasto di movimento oppure no
     */
    private boolean movimento = false;

    /**
     * Indica se il player sta sparando attualmente
     */
    private boolean sparando = false;


    /**
     * Gravit&agrave; a cui è soggetto il giocatore
     */
    private int gravita;

    private int velocita;

    public Player(String nome, Image immagine, String proiettile, int saluteMassima, int periodoDiRiposo, int periodoDiRiposoSparo, int attacco, int velocita) {
        super.init(nome, immagine);
        this.saluteMassima = saluteMassima;
        this.saluteAttuale = saluteMassima;
        this.periodoDiRiposo = periodoDiRiposo;
        this.riposoAttuale = periodoDiRiposo;
        this.riposoAttualeSparo = this.periodoDiRiposoSparo = periodoDiRiposoSparo;
        animazioneSalto = new Animazione(nome + "_salto", percorso + "\\salto");
        animazioneSparo = new Animazione(nome + "_sparo", percorso + "\\sparo");
        animazioneDannoSubito = new Animazione(nome + "_colpito", percorso + "\\colpito");
        animazioneDannoSubito.setContinuous(false);
        this.attacco = attacco;
        this.nomeProiettile = proiettile;
        this.velocita = velocita;
        numeroProiettili = 0;
        movimento = false;
        sparando = false;
        priorita = Settings.PRIORITA_PLAYER;
    }

    public Player(String nome) {
        this(nome, Utils.getBufferedImage(Settings.CARTELLA_SPRITES, nome),
                null, 100, 100, 30, 100, 2);
    }

    /**
     * Cambia la salute attuale in modo che sia sempre compresa fra 0 e saluteMassima
     *
     * @param value valore da togliere o aggiungere
     */
    public void changeLife(int value) {
        if (!inRiposo() && value < 0) animazioneDannoSubito.avviaAnimazione();
        if (!inRiposo() || value > 0) {
            if (this.saluteAttuale + value <= this.saluteMassima)
                this.saluteAttuale += value;
            else if (this.saluteAttuale + value > this.saluteMassima)
                this.saluteAttuale = saluteMassima;
            if (value < 0)
                riposoAttuale = 0;
        }
    }

    @Override
    public void onCollision(Collisione collisione) {
        Sprite other = collisione.getPrimoSprite();
        if (other instanceof Proiettile && ((Proiettile) other).getProprietario() instanceof NPC) {
            this.changeLife(-((Proiettile) other).getAttacco());
            other.setDead();
        }
    }

    public void muovi() {
        //if(isBlocked()) verticalVelocity -= 1;
        ListaCollisioni movimentoLista = Collisione.rilevaCollisioni(this, horizontalVelocity, 0);
        if (horizontalVelocity != 0 && movimentoLista != null && movimentoLista.toIgnore()) {
            this.xPosition += horizontalVelocity;
            if (horizontalVelocity < 0 && isMovimento())
                this.direzione = Direction.LEFT;
            else if (horizontalVelocity > 0 && isMovimento())
                this.direzione = Direction.RIGHT;
        }
        if (horizontalVelocity != 0 && movimentoLista != null && !movimentoLista.toIgnore()) {
            int valoreAggiunto = 0;
            if (horizontalVelocity > 0) {
                for (int i = 1; i <= horizontalVelocity && Collisione.rilevaCollisioni(this, i, 0).toIgnore(); i++)
                    valoreAggiunto += 1;
            } else if (horizontalVelocity < 0) {
                for (int i = -1; i >= horizontalVelocity && Collisione.rilevaCollisioni(this, i, 0).toIgnore(); i--)
                    valoreAggiunto -= 1;
            }
            this.xPosition += valoreAggiunto;
            if (valoreAggiunto != horizontalVelocity && valoreAggiunto != 0) {
                horizontalVelocity = 0;
                movimento = false;
            }
        }
        if (verticalVelocity != 0 && Collisione.rilevaCollisioni(this, 0, verticalVelocity).toIgnore()) {
            this.yPosition += verticalVelocity;
            onGround = false;
        }
        if (verticalVelocity > 0 && !Collisione.rilevaCollisioni(this, 0, verticalVelocity).toIgnore()) {
            int valoreAggiunto = 0;
            for (int i = 1; i <= verticalVelocity && Collisione.rilevaCollisioni(this, 0, i).toIgnore(); i++)
                valoreAggiunto += 1;
            this.yPosition += valoreAggiunto;
            verticalVelocity = 0;
            onGround = true;
        } else if (!Collisione.rilevaCollisioni(this, 0, verticalVelocity).toIgnore()) verticalVelocity = 0;

        if (Collisione.rilevaCollisioni(this, 0, 1).toIgnore())
            onGround = false;
    }

    public int getSaluteMassima() {
        return saluteMassima;
    }

    public int getSaluteAttuale() {
        return saluteAttuale;
    }

    /**
     * A differenza dello sprite avremo due tipi di animazione in più, per il salto e per l'azione di sparo.
     *
     * @return immagine dell'animazione corrispondente
     */
    @Override
    public Image getImmagine() {
        Image immagine = this.immagine;

        if (this.animazioneDannoSubito != null && this.animazioneDannoSubito.isRunning()) {
            immagine = this.animazioneDannoSubito.getImage(direzione);
            animazioneMovimento.stopAnimazione();
            animazioneSparo.stopAnimazione();
            animazioneStatica.stopAnimazione();
            animazioneSalto.stopAnimazione();
        } else if (sparando && !this.animazioneSparo.isEmpty()) {
            immagine = animazioneSparo.getImage(direzione);
            animazioneMovimento.stopAnimazione();
            animazioneSalto.stopAnimazione();
            animazioneStatica.stopAnimazione();
        } else if (!this.animazioneStatica.isEmpty() && this.animazioneStatica.size() != 1 && this.onGround && !this.movimento) {
            immagine = animazioneStatica.getImage(direzione);
            animazioneMovimento.stopAnimazione();
            animazioneSalto.stopAnimazione();
            animazioneSparo.stopAnimazione();
        } else if (!this.animazioneMovimento.isEmpty() && this.movimento && this.onGround) {
            immagine = animazioneMovimento.getImage(direzione);
            animazioneStatica.stopAnimazione();
            animazioneSalto.stopAnimazione();
            animazioneSparo.stopAnimazione();
        } else if (!this.animazioneSalto.isEmpty() && !this.onGround) {
            immagine = animazioneSalto.getImage(direzione);
            animazioneStatica.stopAnimazione();
            animazioneMovimento.stopAnimazione();
            animazioneSparo.stopAnimazione();
        } else if (direzione == Direction.LEFT) {
            immagine = Utils.mirrorImage(immagine);
        }
        if (inRiposo()) {
            BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = tmp.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            g.drawImage(immagine, 0, 0, width, height, null);
            g.dispose();
            return tmp;
        }
        return immagine;
    }

    public Direction getDirezione() {
        return direzione;
    }

    public void setDirezione(Direction direzione) {
        this.direzione = direzione;
    }

    public int getPeriodoDiRiposo() {
        return periodoDiRiposo;
    }

    public void changeRiposoAttuale(int value) {
        if (inRiposo())
            this.riposoAttuale += value;
    }

    public void changeRiposoAttualeSparo(int value) {
        if (riposoAttualeSparo < periodoDiRiposoSparo)
            this.riposoAttualeSparo += value;
    }

    public boolean inRiposo() {
        return (riposoAttuale < periodoDiRiposo);
    }

    /**
     * Esegue un salto verso l'alto
     */
    public void salta() {
        verticalVelocity = 0;
        this.setVelocita(Sprite.Direction.UP, 8, -8, 8);
    }

    /**
     * Applica attrito e gravità
     */
    public void applicaFisica() {
        if (!this.isOnGround())//Se il giocatore non è a terra allora applica una velocità verso il basso
            this.setVelocita(Sprite.Direction.DOWN, this.getGravita(), -8, 8, true);
        if (!this.isMovimento()) { //Se il giocatore non è in movimento allora azzera la sua velocità
            int value = 5;
            boolean prog = false;
            if (!this.isOnGround()) {
                value = 1; //Se il giocatore sta saltando allora l'attrito è minore
                prog = true;
            }

            if (this.getHorizontalVelocity() > 0)
                this.setVelocita(Sprite.Direction.LEFT, value, 0, value, prog);
            else if (this.getHorizontalVelocity() < 0)
                this.setVelocita(Sprite.Direction.RIGHT, value, -value, 0, prog);
        }
        this.changeRiposoAttuale(10);
        this.changeRiposoAttualeSparo(10);
    }

    @Override
    public void setVelocita(Direction direzione, int value, int min, int max, boolean progressive) {
        super.setVelocita(direzione, value, min, max, progressive);
    }

    /**
     * Spara un proiettile nella direzione del Player
     *
     * @return true se si hanno abbastanza proiettili, false altrimenti
     */
    public Proiettile sparaProiettile() {
        if (proiettile == null && nomeProiettile != null)
            proiettile = PiattaformaDAO.getInstance().getProiettile(this.nomeProiettile, this);
        this.setSparando(true);
        if (proiettile != null && periodoDiRiposoSparo > -1 && numeroProiettili > 0 && periodoDiRiposoSparo <= riposoAttualeSparo) {
            Proiettile sparato = (Proiettile) proiettile.clone();
            boolean dir = direzione == Direction.RIGHT;
            sparato.setPosition(this.getX() + (dir ? this.getWidth() : 0), this.getY() + this.getHeight() / 2);
            sparato.iniziaMovimento();
            this.riposoAttualeSparo = 0;
            numeroProiettili--;
            return sparato;
        }
        return null;
    }

    @Override
    public void onDeath() {
        super.onDeath();
        animazioneDannoSubito.stopAnimazione();
        animazioneSparo.stopAnimazione();
        animazioneSalto.stopAnimazione();
    }

    /**
     * Il Player ad ogni ciclo di update rivelerà se esso collide con qualcosa.
     * Se succede verrà richiamato il relativo metodo onCollision.
     */
    public void update() {
        Collisione.rilevaCollisioni(this, 0, 0);
    }

    public void addProiettili(int numero) {
        this.numeroProiettili += numero;
    }

    public boolean isMovimento() {
        return movimento;
    }

    public void setMovimento(boolean movimento) {
        this.movimento = movimento;
    }

    public int getAttacco() {
        return attacco;
    }

    public void setAttacco(int attacco) {
        this.attacco = attacco;
    }

    public int getNumeroProiettili() {
        return numeroProiettili;
    }

    public void setNumeroProiettili(int numeroProiettili) {
        this.numeroProiettili = numeroProiettili;
    }

    public int getGravita() {
        return gravita;
    }


    public void setGravita(int gravita) {
        this.gravita = gravita;
    }

    public void addXPosition(int value) {
        if (Collisione.rilevaCollisioni(this, value, 0).toIgnore()) {
            this.xPosition += value;
        }
    }


    public Animazione getAnimazioneSalto() {
        return animazioneSalto;
    }

    public void setAnimazioneSalto(Animazione animazioneSalto) {
        this.animazioneSalto = animazioneSalto;
    }

    public Animazione getAnimazioneSparo() {
        return animazioneSparo;
    }

    public void setAnimazioneSparo(Animazione animazioneSparo) {
        this.animazioneSparo = animazioneSparo;
    }

    public Animazione getAnimazioneDannoSubito() {
        return animazioneDannoSubito;
    }

    public void setAnimazioneDannoSubito(Animazione animazioneDannoSubito) {
        this.animazioneDannoSubito = animazioneDannoSubito;
    }

    public int getPeriodoDiRiposoSparo() {
        return periodoDiRiposoSparo;
    }

    public void setPeriodoDiRiposoSparo(int periodoDiRiposoSparo) {
        this.periodoDiRiposoSparo = periodoDiRiposoSparo;
    }

    public int getRiposoAttualeSparo() {
        return riposoAttualeSparo;
    }

    public void setRiposoAttualeSparo(int riposoAttualeSparo) {
        this.riposoAttualeSparo = riposoAttualeSparo;
    }

    public boolean isSparando() {
        return sparando;
    }

    public void setSparando(boolean sparando) {
        this.sparando = sparando;
    }

    public int getVelocita() {
        return velocita;
    }

    public void setVelocita(int velocita) {
        this.velocita = velocita;
    }

    public String getNomeProiettile() {
        return nomeProiettile;
    }


}

