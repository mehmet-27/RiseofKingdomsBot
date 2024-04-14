package dev.mehmet27.rokbot;

import dev.mehmet27.rokbot.utils.AdbUtils;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DisplayButtonCellEditor extends DefaultCellEditor {
	private JButton button;
	private String label;
	private JTable table;
	private int row, col;
	private boolean isPushed;

	public DisplayButtonCellEditor() {
		super(new JTextField());
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped(); // Düzenlemeyi durdur
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		this.table = table;
		this.row = row;
		this.col = column;
		isPushed = true;
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		if (isPushed) {
			String deviceInput = (String) table.getValueAt(row, 0);
			try {
				for (JadbDevice device : Main.getInstance().getAdbManager().getJadbConnection().getDevices()){
					if (device.getSerial().equals(deviceInput)){
						if (!AdbUtils.isRokRunning(device)){
							JOptionPane.showMessageDialog(Main.getInstance().getMainForm(), "Please start the game.", "Warning!", JOptionPane.WARNING_MESSAGE);
						}else {
							Main.getInstance().getMainForm().setDevice(device);
							Main.getInstance().getMainForm().getTabbedPane1().setSelectedIndex(1);
						}
					}
				}
			} catch (IOException | JadbException e) {
				e.printStackTrace();
			}
		}
		isPushed = false;
		return label;
	}

	@Override
	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	@Override
	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}