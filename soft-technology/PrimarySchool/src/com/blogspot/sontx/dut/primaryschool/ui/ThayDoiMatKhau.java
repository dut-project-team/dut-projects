package com.blogspot.sontx.dut.primaryschool.ui;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class ThayDoiMatKhau extends Window {
	private JPasswordField matKhauMoi;
	private JPasswordField xacNhanMatKhauMoi;
	public ThayDoiMatKhau() {
		
		JLabel lblThayiMt = new JLabel("Thay \u0111\u1ED5i m\u1EADt kh\u1EA9u");
		lblThayiMt.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblThayiMt.setBounds(308, 11, 167, 31);
		getContentPane().add(lblThayiMt);
		
		JLabel lblMtKhuMi = new JLabel("M\u1EADt kh\u1EA9u m\u1EDBi");
		lblMtKhuMi.setBounds(250, 151, 110, 14);
		getContentPane().add(lblMtKhuMi);
		
		JLabel lblXcNhnMt = new JLabel("X\u00E1c nh\u1EADn m\u1EADt kh\u1EA9u m\u1EDBi");
		lblXcNhnMt.setBounds(250, 176, 128, 14);
		getContentPane().add(lblXcNhnMt);
		
		JButton nutDongY = new JButton("\u0110\u1ED3ng \u00FD");
		nutDongY.setBounds(347, 220, 89, 23);
		getContentPane().add(nutDongY);
		
		matKhauMoi = new JPasswordField();
		matKhauMoi.setBounds(393, 148, 128, 20);
		getContentPane().add(matKhauMoi);
		
		xacNhanMatKhauMoi = new JPasswordField();
		xacNhanMatKhauMoi.setBounds(393, 173, 128, 20);
		getContentPane().add(xacNhanMatKhauMoi);
	}
}
