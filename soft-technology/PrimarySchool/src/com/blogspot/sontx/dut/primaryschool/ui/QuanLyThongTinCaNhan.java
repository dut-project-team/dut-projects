package com.blogspot.sontx.dut.primaryschool.ui;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;

public class QuanLyThongTinCaNhan extends Window {
	private JTextField soDienThoai;
	public QuanLyThongTinCaNhan() {
		
		JLabel lblQunLThng = new JLabel("Qu\u1EA3n l\u00FD th\u00F4ng tin c\u00E1 nh\u00E2n");
		lblQunLThng.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblQunLThng.setBounds(283, 11, 217, 48);
		getContentPane().add(lblQunLThng);
		
		JLabel lblHVTn = new JLabel("H\u1ECD v\u00E0 t\u00EAn");
		lblHVTn.setBounds(223, 85, 92, 14);
		getContentPane().add(lblHVTn);
		
		JLabel lblNgySinh = new JLabel("Ng\u00E0y sinh");
		lblNgySinh.setBounds(223, 110, 92, 14);
		getContentPane().add(lblNgySinh);
		
		JLabel lblGiiTnh = new JLabel("Gi\u1EDBi t\u00EDnh");
		lblGiiTnh.setBounds(223, 135, 92, 14);
		getContentPane().add(lblGiiTnh);
		
		JLabel lblSinThoi = new JLabel("S\u1ED1 \u0111i\u1EC7n tho\u1EA1i");
		lblSinThoi.setBounds(223, 163, 92, 14);
		getContentPane().add(lblSinThoi);
		
		JLabel lblaChNh = new JLabel("\u0110\u1ECBa ch\u1EC9 nh\u00E0");
		lblaChNh.setBounds(223, 188, 92, 14);
		getContentPane().add(lblaChNh);
		
		JLabel hoVaTen = new JLabel("New label");
		hoVaTen.setBounds(350, 85, 46, 14);
		getContentPane().add(hoVaTen);
		
		JLabel ngaySinh = new JLabel("New label");
		ngaySinh.setBounds(350, 110, 46, 14);
		getContentPane().add(ngaySinh);
		
		JLabel gioiTinh = new JLabel("New label");
		gioiTinh.setBounds(350, 135, 46, 14);
		getContentPane().add(gioiTinh);
		
		soDienThoai = new JTextField();
		soDienThoai.setBounds(350, 160, 154, 20);
		getContentPane().add(soDienThoai);
		soDienThoai.setColumns(10);
		
		JTextArea diaChiNha = new JTextArea();
		diaChiNha.setText("sfsfsdf");
		diaChiNha.setBounds(350, 183, 217, 60);
		getContentPane().add(diaChiNha);
		
		JButton nutLuuThongTin = new JButton("L\u01B0u th\u00F4ng tin");
		nutLuuThongTin.setBounds(281, 266, 103, 23);
		getContentPane().add(nutLuuThongTin);
		
		JButton nutTroVe = new JButton("Tr\u1EDF v\u1EC1");
		nutTroVe.setBounds(418, 266, 89, 23);
		getContentPane().add(nutTroVe);
	}
}
