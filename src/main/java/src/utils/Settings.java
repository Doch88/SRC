package src.utils;

import java.awt.*;

/**
 * Classe contenente alcuni costanti utilizzate in tutto il progetto
 */
public class Settings {

    public static final int MENU_MAIN = 0;
    public static final int MENU_EDITOR_GIOCHI = 1;
    public static final int MENU_EDITOR_MAPPE = 2;
    public static final int MENU_PIATTAFORMA_GIOCHI = 3;
    public static final int MENU_PIATTAFORMA_SALVATAGGI = 4;
    public static final int MENU_PIATTAFORMA_MAPPE = 5;

    public static final String CARTELLA_GIOCHI = "giochi";
    public static final String CARTELLA_MAPPE = "mappe";
    public static final String CARTELLA_SPRITES = "sprites";

    public static final int PRIORITA_ALERTPOINT = 1;
    public static final int PRIORITA_BLOCCO = 1;
    public static final int PRIORITA_PORTALE = 1;
    public static final int PRIORITA_RACCOGLIBILE = 2;
    public static final int PRIORITA_NPC = 3;
    public static final int PRIORITA_PLAYER = 4;
    public static final int PRIORITA_PROIETTILE = 5;

    public static final int STANDARD_FRAMERATE = 60;
    public static final int SPOSTAMENTO_ANTEPRIMA = 15;
    public static final int SCROLLPANE_VELOCITA = 15;

    public static final String FRAME_TITOLO = "SRC";
    public static final int FRAME_LARGHEZZA = 1200;
    public static final int FRAME_ALTEZZA = 800;

    public static final int ELEM_PANEL_LARGHEZZA = 600;
    public static final int ELEM_PANEL_ALTEZZA = 300;
    public static final int ELEM_PANEL_HEADER_LARGHEZZA = (int) (ELEM_PANEL_LARGHEZZA * 0.8);
    public static final int ELEM_PANEL_HEADER_ALTEZZA = 30;

    public static final int MENU_NUM_COLONNE = 2;

    public static final Image STILE_MENU_SFONDO = Utils.getBufferedImage("", "Sky");
    public static final Font STILE_MENU_FONT = new Font("Ar Julian", Font.BOLD, 200);

    public static final Font STILE_PIATTAFORMA_FONT = new Font("Arial Black", Font.BOLD, 10);
    public static final Font STILE_PIATTAFORMA_MSG_FONT = new Font("Arial Black", Font.BOLD, 40);

    public static final int PIATTAFORMA_FISICA_RITARDO = 10;
}
