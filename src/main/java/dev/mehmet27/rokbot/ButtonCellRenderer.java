package dev.mehmet27.rokbot;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonCellRenderer extends JButton implements TableCellRenderer {
	public ButtonCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}
		if (hasFocus) {
			setBackground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
		}
		setText((value == null) ? "" : value.toString());
		return this;
	}
}
