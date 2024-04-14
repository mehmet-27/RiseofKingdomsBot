package dev.mehmet27.rokbot.frames;

import dev.mehmet27.rokbot.ButtonCellRenderer;
import dev.mehmet27.rokbot.DisplayButtonCellEditor;
import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;
import dev.mehmet27.rokbot.tasks.AllianceHelpTask;
import dev.mehmet27.rokbot.tasks.GatherResourcesTask;
import dev.mehmet27.rokbot.tasks.KingdomScannerTask;
import dev.mehmet27.rokbot.tasks.PeerlessScholarTask;
import dev.mehmet27.rokbot.utils.AdbUtils;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.net.InetSocketAddress;

public class MainForm extends JFrame {

	private final Main main = Main.getInstance();
	private final FileConfiguration config = main.getConfigManager().getConfig();

	private JadbDevice device;

	private JPanel mainPanel;
	private JTabbedPane tabbedPane1;
	private JTextField ipField;
	private JButton connectButton;
	private JButton refreshDevicesButton;
	private JTable table1;
	private JPanel settingsPanel;
	private JPanel displayDevicePanel;
	private JPanel deviceListPanel;
	private JScrollPane tableScrollPane;
	private JTextField taskField;
	private JTextArea textArea;
	private JScrollPane textAreaScrollPane;
	private JCheckBox gatherResourceCheckBox;
	private JPanel featuresPanel;
	private JCheckBox useGatheringBoostsCheckBox;
	private JComboBox foodBox;
	private JComboBox woodBox;
	private JComboBox stoneBox;
	private JComboBox goldBox;
	private JCheckBox attackBarbariansCheckBox;
	private JLabel deviceSerialLabel;
	private JTextField portField;
	private JButton startButton;
	private JPanel gatherResourcePanel;
	private JCheckBox useSecondaryCommanderCheckBox;
	private JCheckBox allianceHelpCheckBox;
	private JPanel kingdomScannerPanel;
	private JButton scanButton;

	public MainForm() {
		setTitle("Rise of Kingdoms Bot");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		//setSize(config.getInt("screenSize.width", 470), config.getInt("screenSize.height", 700));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);
		DefaultTableModel model = new DefaultTableModel();
		table1.setModel(model);
		table1.setDefaultEditor(Object.class, null);
		model.addColumn("Device");
		model.addColumn("Display");
		table1.getColumnModel().getColumn(1).setCellRenderer(new ButtonCellRenderer());
		table1.getColumnModel().getColumn(1).setCellEditor(new DisplayButtonCellEditor());
		add(mainPanel);

		tabbedPane1.setEnabledAt(1, false);

		refreshDevices(model);
		refreshDevicesButton.addActionListener(event -> {
			refreshDevices(model);
		});
		connectButton.addActionListener(event -> {
			String ip = ipField.getText();
			String port = portField.getText();
			if (ip == null || ip.isEmpty()) {
				JOptionPane.showMessageDialog(connectButton, "Please enter ip.", "Warning!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (port == null || port.isEmpty()) {
				JOptionPane.showMessageDialog(connectButton, "Please enter port.", "Warning!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				main.getAdbManager().getJadbConnection().connectToTcpDevice(new InetSocketAddress(ip, Integer.parseInt(port)));
				refreshDevices(model);
				JOptionPane.showMessageDialog(connectButton, "Successfully connected to the device.", "Success!", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException | JadbException | ConnectionToRemoteDeviceException e) {
				JOptionPane.showMessageDialog(connectButton, "Can't connect to device.", "Error!", JOptionPane.ERROR_MESSAGE);
			}
		});

		startButton.addActionListener(event -> {
			main.getTaskManager().getTaskList().clear();
			//Get selected tasks
			if (allianceHelpCheckBox.isSelected()) {
				main.getTaskManager().getTaskList().add(new AllianceHelpTask(device));
			}
			if (gatherResourceCheckBox.isSelected()) {
				main.getTaskManager().getTaskList().add(new GatherResourcesTask(device));
			}
			new PeerlessScholarTask(device).run(device);
			/*Main.getInstance().setRunningTask(Main.getInstance().getTaskExecutorService().scheduleAtFixedRate(() -> {
				if (!main.isTaskRunning()) {
					//Map<String, Task> taskMap = main.getTaskManager().getTaskMap();
					List<Task> taskList = main.getTaskManager().getTaskList();
					if (!taskList.isEmpty()) {
						main.setTaskRunning(true);
						for (Task task : taskList) {
							try {
								main.getTaskManager().setCurrentTask(task);
								updateTaskField(task.getClass().getSimpleName());
								appendToTextArea("Start Task: " + task.getClass().getSimpleName());
								task.run(device);
								appendToTextArea("End Task: " + task.getClass().getSimpleName());
							} catch (Exception e) {
								e.printStackTrace();
								main.getTaskManager().setCurrentTask(null);
							}
						}
						main.setTaskRunning(false);
					}
				}
			}, 0, 3, TimeUnit.SECONDS));*/
		});

		gatherResourceCheckBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				for (Component component : gatherResourcePanel.getComponents()) {
					component.setEnabled(true);
				}
			} else {
				for (Component component : gatherResourcePanel.getComponents()) {
					component.setEnabled(false);
				}
			}
		});

		gatherResourceCheckBox.addItemListener(e -> {
			for (Component component : gatherResourcePanel.getComponents()) {
				component.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		gatherResourceCheckBox.setSelected(config.getBoolean("feature.gather-resource.enable"));
		useSecondaryCommanderCheckBox.setSelected(config.getBoolean("feature.gather-resource.use-secondary-commander"));
		allianceHelpCheckBox.setSelected(config.getBoolean("feature.alliance-help.enable"));


		scanButton.addActionListener(event -> {
			new KingdomScannerTask(device).run(device);
		});
	}

	public void refreshDevices(DefaultTableModel model) {
		main.getAdbManager().restartAdbServer();
		try {
			model.setRowCount(0);
			AdbUtils.getDevices().forEach(device -> {
				model.addRow(new String[]{device.getSerial(), "Display"});
			});
		} catch (IOException | JadbException e) {
			JOptionPane.showMessageDialog(refreshDevicesButton, "An error occurred while refreshing.", "Hata!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public JTabbedPane getTabbedPane1() {
		return tabbedPane1;
	}

	public JadbDevice getDevice() {
		return device;
	}

	public void setDevice(JadbDevice device) {
		this.device = device;
		updateDeviceSerialLabel(device.getSerial());
	}

	public void updateTaskField(String task) {
		if (taskField != null) {
			taskField.setText("Task: " + task);
		}
	}

	public void updateDeviceSerialLabel(String serial) {
		if (deviceSerialLabel != null) {
			deviceSerialLabel.setText(serial);
		}
	}

	public void appendToTextArea(String text) {
		textArea.append(text);
		textArea.append("\n");
	}

	public JComboBox getFoodBox() {
		return foodBox;
	}

	public JComboBox getWoodBox() {
		return woodBox;
	}

	public JComboBox getStoneBox() {
		return stoneBox;
	}

	public JComboBox getGoldBox() {
		return goldBox;
	}

	public JCheckBox getUseSecondaryCommanderCheckBox() {
		return useSecondaryCommanderCheckBox;
	}
}
