package com.blogspot.sontx.dut.primaryschool.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public abstract class Window extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 450;

	public Window() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setMinimumSize(getSize());
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		getContentPane().setLayout(null);
	}

	protected abstract void onClicked(String whoClicked);

	@Override
	public void setSize(Dimension d) {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	@Override
	public final void actionPerformed(ActionEvent e) {
		onClicked(e.getActionCommand() != null ? e.getActionCommand() : "someone");
	}

	private static void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}
	}

	static {
		setSystemLookAndFeel();
	}
}
