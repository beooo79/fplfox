package com.github.beooo79;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FoxPropertiesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4409021459702732296L;
	private FoxProperties properties;
	private JLabel lblFS200x;
	private JLabel lblPMDG;
	private JTextField txtFS200x;
	private JTextField txtPMDG;
	private JButton btnPMDG;
	private JButton btnFS200x;
	private JButton btnSave;
	private JButton btnCancel;

	private class CustomFileAction extends AbstractAction {

		private JTextField txt;

		public CustomFileAction(String string, JTextField txt) {
			super(string);
			this.txt = txt;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int opt = fc.showOpenDialog(txt);
			if (opt == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				txt.setText(f.getAbsolutePath());
			}

		}

	}

	public FoxPropertiesDialog(Frame f, FoxProperties properties) {
		super(f);
		this.properties = properties;
		setModal(true);
		initUI();
		loadValues();
		pack();
		setLocationRelativeTo(f);
		setVisible(true);

	}

	private void initUI() {
		final FoxPropertiesDialog myself = this;
		double size[][] = {
				{ TableLayoutConstants.PREFERRED, TableLayoutConstants.FILL,
						TableLayoutConstants.PREFERRED },
				{ TableLayoutConstants.FILL, TableLayoutConstants.FILL, TableLayoutConstants.FILL,
						TableLayoutConstants.FILL } };
		JPanel p = new JPanel(new TableLayout(size));

		lblFS200x = new JLabel("FS200x Flightplan Directory:");
		txtFS200x = new JTextField(50);
		txtFS200x.setText(properties.getProperty("FS200x"));
		btnFS200x = new JButton(new CustomFileAction("Select...", txtFS200x));

		lblPMDG = new JLabel("PMDG Flightplan Directory:");
		txtPMDG = new JTextField(50);
		txtPMDG.setText(properties.getProperty("PMDG"));
		btnPMDG = new JButton(new CustomFileAction("Select...", txtPMDG));

		btnSave = new JButton("Save");
		btnCancel = new JButton("Cancel");

		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				properties.setProperty("FS200x", txtFS200x.getText());
				properties.setProperty("PMDG", txtPMDG.getText());
				properties.storeConf();
				myself.dispose();

			}
		});

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				myself.dispose();
			}
		});

		p.add(lblFS200x, "0,0,r,c");
		p.add(txtFS200x, "1,0");
		p.add(btnFS200x, "2,0");

		p.add(lblPMDG, "0,1,r,c");
		p.add(txtPMDG, "1,1");
		p.add(btnPMDG, "2,1");

		p.add(new JLabel(" "), "0,2,2,2");

		p.add(btnCancel, "0,3");
		p.add(btnSave, "2,3");

		p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(p, BorderLayout.CENTER);

	}

	private void loadValues() {
		// TODO Auto-generated method stub

	}
}
