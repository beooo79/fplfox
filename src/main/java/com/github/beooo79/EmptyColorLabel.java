package com.github.beooo79;
import java.awt.Color;

import javax.swing.JLabel;

public class EmptyColorLabel extends JLabel {

	public EmptyColorLabel(Color color) {
		super(" ");
		setOpaque(true);
		setBackground(color);
	}

}
