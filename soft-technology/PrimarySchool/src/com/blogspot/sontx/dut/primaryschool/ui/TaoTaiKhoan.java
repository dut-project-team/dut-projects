package com.blogspot.sontx.dut.primaryschool.ui;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import com.blogspot.sontx.dut.primaryschool.ui.date.JDateChooser;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JLabel;
//KofD
public class TaoTaiKhoan extends Window{
	private JTextField tFTaiKhoan;
	private JTextField tFHoVaTen;
	private JPasswordField pFMatKhau;
	private JPasswordField pFXacNhanMatKhau;
	public TaoTaiKhoan() {
		setTitle("T\u1EA1o T\u00E0i Kho\u1EA3n \u0110\u0103ng Nh\u1EADp");
		
		tFTaiKhoan = new JTextField();
		tFTaiKhoan.setBounds(397, 46, 186, 20);
		getContentPane().add(tFTaiKhoan);
		tFTaiKhoan.setColumns(10);
		
		tFHoVaTen = new JTextField();
		tFHoVaTen.setColumns(10);
		tFHoVaTen.setBounds(397, 167, 186, 20);
		getContentPane().add(tFHoVaTen);
		
		pFMatKhau = new JPasswordField();
		pFMatKhau.setBounds(397, 77, 186, 20);
		getContentPane().add(pFMatKhau);
		
		pFXacNhanMatKhau = new JPasswordField();
		pFXacNhanMatKhau.setBounds(397, 121, 186, 20);
		getContentPane().add(pFXacNhanMatKhau);
		
		JDateChooser dCNgaySinh = new JDateChooser();
		dCNgaySinh.setBounds(397, 211, 111, 20);
		getContentPane().add(dCNgaySinh);
		
		JButton btnQuayLai = new JButton("Quay L\u1EA1i");
		btnQuayLai.setBounds(306, 307, 89, 23);
		getContentPane().add(btnQuayLai);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(436, 307, 89, 23);
		getContentPane().add(btnOk);
		
		JLabel lblTaiKhoan = new JLabel("T\u00EAn T\u00E0i Kho\u1EA3n");
		lblTaiKhoan.setBounds(196, 49, 131, 14);
		getContentPane().add(lblTaiKhoan);
		
		JLabel lblMatKhau = new JLabel("M\u1EADt Kh\u1EA9u");
		lblMatKhau.setBounds(196, 80, 131, 14);
		getContentPane().add(lblMatKhau);
		
		JLabel lblXacNhanMatKhau = new JLabel("X\u00E1c Nh\u1EADn M\u1EADt Kh\u1EA9u");
		lblXacNhanMatKhau.setBounds(196, 124, 131, 14);
		getContentPane().add(lblXacNhanMatKhau);
		
		JLabel lblHoVaTen = new JLabel("H\u1ECD V\u00E0 T\u00EAn");
		lblHoVaTen.setBounds(196, 170, 131, 14);
		getContentPane().add(lblHoVaTen);
		
		JLabel lblNgaySinh = new JLabel("Ng\u00E0y Sinh");
		lblNgaySinh.setBounds(196, 211, 131, 14);
		getContentPane().add(lblNgaySinh);
		
		JLabel lblGioiTinh = new JLabel("Gi\u1EDBi T\u00EDnh");
		lblGioiTinh.setBounds(196, 262, 131, 14);
		getContentPane().add(lblGioiTinh);
	}
}
