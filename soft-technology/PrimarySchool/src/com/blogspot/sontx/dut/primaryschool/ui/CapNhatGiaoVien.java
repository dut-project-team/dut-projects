package com.blogspot.sontx.dut.primaryschool.ui;
import javax.swing.JTextField;

import com.blogspot.sontx.dut.primaryschool.ui.date.JCalendar;
import com.blogspot.sontx.dut.primaryschool.ui.date.JDateChooser;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
//KofD
public class CapNhatGiaoVien extends Window{
	private JTextField tFHoVaTen;
	private JTextField tFDiaChi;
	private JTextField tFQueQuan;
	private JTextField tFSoDienThoai;
	private JButton btnHuy;
	public CapNhatGiaoVien() {
		setTitle("C\u1EADp Nh\u1EADt Gi\u00E1o Vi\u00EAn");
		
		tFHoVaTen = new JTextField();
		tFHoVaTen.setBounds(382, 50, 167, 20);
		getContentPane().add(tFHoVaTen);
		tFHoVaTen.setColumns(10);
		
		JDateChooser dCNgaySinh = new JDateChooser();
		dCNgaySinh.setBounds(382, 80, 111, 20);
		getContentPane().add(dCNgaySinh);
		
		tFDiaChi = new JTextField();
		tFDiaChi.setColumns(10);
		tFDiaChi.setBounds(382, 110, 167, 20);
		getContentPane().add(tFDiaChi);
		
		tFQueQuan = new JTextField();
		tFQueQuan.setColumns(10);
		tFQueQuan.setBounds(382, 140, 167, 20);
		getContentPane().add(tFQueQuan);
		
		tFSoDienThoai = new JTextField();
		tFSoDienThoai.setColumns(10);
		tFSoDienThoai.setBounds(382, 170, 167, 20);
		getContentPane().add(tFSoDienThoai);
		
		JButton btnCapNhat = new JButton("C\u1EADp Nh\u1EADt");
		btnCapNhat.setBounds(295, 250, 89, 23);
		getContentPane().add(btnCapNhat);
		
		btnHuy = new JButton("H\u1EE7y");
		btnHuy.setBounds(421, 250, 89, 23);
		getContentPane().add(btnHuy);
		
		JLabel lblHoVaTen = new JLabel("H\u1ECD V\u00E0 T\u00EAn");
		lblHoVaTen.setBounds(201, 50, 111, 14);
		getContentPane().add(lblHoVaTen);
		
		JLabel lblNgaySinh = new JLabel("Ng\u00E0y Sinh");
		lblNgaySinh.setBounds(201, 80, 111, 14);
		getContentPane().add(lblNgaySinh);
		
		JLabel lblDiaChi = new JLabel("\u0110\u1ECBa Ch\u1EC9");
		lblDiaChi.setBounds(201, 110, 111, 14);
		getContentPane().add(lblDiaChi);
		
		JLabel lblQueQuan = new JLabel("Qu\u00EA Qu\u00E1n");
		lblQueQuan.setBounds(201, 140, 111, 14);
		getContentPane().add(lblQueQuan);
		
		JLabel lblSoDienThoai = new JLabel("S\u1ED1 \u0110i\u1EC7n Tho\u1EA1i");
		lblSoDienThoai.setBounds(201, 170, 111, 14);
		getContentPane().add(lblSoDienThoai);
		
		JLabel lblGioiTinh = new JLabel("Gi\u1EDBi T\u00EDnh");
		lblGioiTinh.setBounds(201, 200, 111, 14);
		getContentPane().add(lblGioiTinh);
		
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.setBounds(382, 200, 177, 23);
		getContentPane().add(buttonPanel);
		
		JRadioButton rdbtnNam = new JRadioButton("Nam");
		rdbtnNam.setBounds(6, 0, 52, 23);
		buttonPanel.add(rdbtnNam);
		
		JRadioButton rdbtnNu = new JRadioButton("N\u1EEF");
		rdbtnNu.setBounds(86, 0, 52, 23);
		buttonPanel.add(rdbtnNu);
	}
	@Override
	protected void onClicked(String whoClicked) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args){
		CapNhatGiaoVien kofd = new CapNhatGiaoVien();
		kofd.setVisible(true);
	}
}
