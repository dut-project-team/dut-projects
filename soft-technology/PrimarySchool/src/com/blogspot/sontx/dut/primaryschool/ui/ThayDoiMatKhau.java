package com.blogspot.sontx.dut.primaryschool.ui;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPasswordField;

/**
 * @author trong
 *
 */
public class ThayDoiMatKhau extends Window {
	private static final long serialVersionUID = 1L;
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
		nutDongY.addActionListener(this);
		nutDongY.setActionCommand("DongY");
		getContentPane().add(nutDongY);
		
		matKhauMoi = new JPasswordField();
		matKhauMoi.setBounds(393, 148, 128, 20);
		getContentPane().add(matKhauMoi);
		
		xacNhanMatKhauMoi = new JPasswordField();
		xacNhanMatKhauMoi.setBounds(393, 173, 128, 20);
		getContentPane().add(xacNhanMatKhauMoi);
		
		JButton dangXuat = new JButton("Đăng xuất");
		dangXuat.setBounds(667, 17, 89, 23);
		dangXuat.addActionListener(this);
		dangXuat.setActionCommand("DangXuat");
		getContentPane().add(dangXuat);
		
		this.setVisible(true);
	}
	
	@Override
	protected void onClicked(String whoClicked) {
		if ("DongY".equals(whoClicked)) {
			String pass1 = matKhauMoi.getText();
			String pass2 = xacNhanMatKhauMoi.getText();
			int minLenOfPass = 6;
			if (pass1.length() >= 6 && pass1.equals(pass2)) {
				QuanLyThongTinCaNhan.infoBox("Doi mat khau thanh cong", "Thanh cong");
			} else if (pass1.length() < 6) {
				QuanLyThongTinCaNhan.infoBox("Mật khẩu ít nhất " + minLenOfPass + " ký tự", "Mật khẩu không hợp lệ");
			} else if (!pass1.equals(pass2)) {
				QuanLyThongTinCaNhan.infoBox("Mật khẩu xác nhận không khớp", "Mật khẩu không khớp");
			}
		} else if ("DangXuat".equals(whoClicked)) {
			
		}
	}
	
	public static void main(String[] args) {
		new ThayDoiMatKhau();
	}
}
