package src.view.editor;

import src.model.editor.SpriteEditor;
import src.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Classe utilizzata per permettere di associare uno sprite ad ogni elemento della lista della sidebar sinistra.
 */
public class CellRenderer extends JLabel implements ListCellRenderer<SpriteEditor> {

    private static CellRenderer instance = null;

    public Component getListCellRendererComponent(
            JList<? extends SpriteEditor> list,
            SpriteEditor value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        setText(value.getNome());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        Font font = list.getFont();
        font = font.deriveFont(Font.PLAIN);
        if (value.getDatiSprite().get("tipo").equals("player")) {
            font = font.deriveFont(Font.BOLD);
        }
        setFont(font);
        setOpaque(true);
        return this;
    }

    public static CellRenderer getInstance() {
        if (instance == null)
            instance = new CellRenderer();
        return instance;
    }

}