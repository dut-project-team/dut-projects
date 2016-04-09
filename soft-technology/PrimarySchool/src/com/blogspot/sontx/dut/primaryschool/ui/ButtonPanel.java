package com.blogspot.sontx.dut.primaryschool.ui;

import java.awt.Component;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ButtonPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ButtonGroup ButtonGroup = new ButtonGroup();

	public ButtonPanel() {
		setLayout(null);
	}
	
	@Override
	public Component add(Component comp) {
		if (comp instanceof JRadioButton)
			ButtonGroup.add((JRadioButton) comp);
		return super.add(comp);
	}
}
