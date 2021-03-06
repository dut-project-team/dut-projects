package com.blogspot.sontx.dut.primaryschool.ui;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.blogspot.sontx.dut.primaryschool.bean.Staff;
import com.blogspot.sontx.dut.primaryschool.bo.DataManager;
import com.blogspot.sontx.dut.primaryschool.bo.WildcardMatcher;

import java.awt.Font;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author trong
 *
 */
public class QuanLyThongTinCaNhan extends Window {
	private static final long serialVersionUID = 1L;
	private JTextField soDienThoai;
	private JTextArea diaChiNha;
	
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
		hoVaTen.setBounds(350, 85, 217, 14);
		getContentPane().add(hoVaTen);
		
		JLabel ngaySinh = new JLabel("New label");
		ngaySinh.setBounds(350, 110, 217, 14);
		getContentPane().add(ngaySinh);
		
		JLabel gioiTinh = new JLabel("New label");
		gioiTinh.setBounds(350, 135, 46, 14);
		getContentPane().add(gioiTinh);
		
		soDienThoai = new JTextField();
		soDienThoai.setBounds(350, 160, 154, 20);
		getContentPane().add(soDienThoai);
		soDienThoai.setColumns(10);
		
		diaChiNha = new JTextArea();
		diaChiNha.setText("sfsfsdf");
		diaChiNha.setBounds(350, 183, 217, 60);
		getContentPane().add(diaChiNha);
		
		JButton nutLuuThongTin = new JButton("L\u01B0u th\u00F4ng tin");
		nutLuuThongTin.setBounds(281, 266, 103, 23);
		nutLuuThongTin.addActionListener(this);
		nutLuuThongTin.setActionCommand("LuuThongTin");
		getContentPane().add(nutLuuThongTin);
		
		JButton nutTroVe = new JButton("Tr\u1EDF v\u1EC1");
		nutTroVe.setBounds(418, 266, 89, 23);
		nutTroVe.addActionListener(this);
		nutTroVe.setActionCommand("TroVe");
		getContentPane().add(nutTroVe);
		
		// show data
		Staff nv = DataManager.layNhanVien(0);
		hoVaTen.setText(nv.getName());
		ngaySinh.setText(dateToString(nv.getDateOfBirth()));
		gioiTinh.setText(nv.isFemale() ? "Nữ" : "Nam");
		soDienThoai.setText(nv.getPhoneNumbers());
		diaChiNha.setText(nv.getAddress());
		
		JButton dangXuat = new JButton("Đăng xuất");
		dangXuat.setBounds(673, 26, 89, 23);
		dangXuat.addActionListener(this);
		dangXuat.setActionCommand("DangXuat");
		getContentPane().add(dangXuat);
		
		this.setVisible(true);
	}
	
	public static String dateToString(Date date) {
		return date.getDate() + "-" + date.getMonth() + "-" + date.getYear();
	}
	
	public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
	
	private static Pattern phoneNumberPattern = Pattern.compile("\\d{11}");
	private static boolean checkPhoneNumber(String phoneNumber) {
		return phoneNumberPattern.matcher(phoneNumber).matches();
	}

	@Override
	protected void onClicked(String whoClicked) {
		if ("LuuThongTin".equals(whoClicked)) {
			if (checkPhoneNumber(soDienThoai.getText()) && soDienThoai.getText().length() == 11 && diaChiNha.getText().length() > 10) {
				infoBox("nhap dung", "nhap dung");
			} else if(!checkPhoneNumber(soDienThoai.getText()) || soDienThoai.getText().length() != 11) {
				infoBox("Số điện thoại gồm 11 chữ số từ 0 đến 9", "Số điện thoại sai định dạng");
			} else if (diaChiNha.getText().length() <= 10) {
				infoBox("Địa chỉ quá ngắn thiếu thông tin", "Địa chỉ không hợp lệ");
			}
		} else if ("TroVe".equals(whoClicked)) {
			
		} else if ("DangXuat".equals(whoClicked)) {
			
		}
	}
	
	public static void main(String[] args) {
		new QuanLyThongTinCaNhan();
	}
}
