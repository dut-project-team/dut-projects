package com.blogspot.sontx.dut.primaryschool.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.blogspot.sontx.dut.primaryschool.bean.Account;
import com.blogspot.sontx.dut.primaryschool.bean.Staff;
import com.blogspot.sontx.dut.primaryschool.bean.Student;

public final class DataSample {
	DataSample() {
	}

	public static List<Account> getAccounts() {
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account("sontx", "1234567"));
		accounts.add(new Account("hoang", "abc12345"));
		accounts.add(new Account("khanh", "69696969"));
		accounts.add(new Account("thach", "1234567"));
		accounts.add(new Account("trong", "098765432"));
		return accounts;
	}

	public static List<Staff> getStaffs() {
		List<Staff> staffs = new ArrayList<>();
		Staff staff = new Staff();
		staff.setAddress("K52. Nguyễn Lương Bằng");
		staff.setDateOfBirth(new Date(1988, 3, 3));
		staff.setFemale(false);
		staff.setHomeTown("Trà My - Quảng Ngãi");
		staff.setName("Nguyễn Văn A");
		staff.setPhoneNumbers("01647891437");
		staffs.add(staff);
		
		staff = new Staff();
		staff.setAddress("K53. Nguyễn Lương Bằng");
		staff.setDateOfBirth(new Date(1987, 2, 2));
		staff.setFemale(true);
		staff.setHomeTown("Trà My - Quảng Bình");
		staff.setName("Nguyễn Thị B");
		staff.setPhoneNumbers("01647891437");
		staffs.add(staff);
		
		staff = new Staff();
		staff.setAddress("K54. Nguyễn Lương Bằng");
		staff.setDateOfBirth(new Date(1985, 2, 2));
		staff.setFemale(false);
		staff.setHomeTown("Trà My - Quảng Trị");
		staff.setName("Nguyễn Văn C");
		staff.setPhoneNumbers("01647891437");
		staffs.add(staff);
		
		staff = new Staff();
		staff.setAddress("K55. Nguyễn Lương Bằng");
		staff.setDateOfBirth(new Date(1980, 2, 2));
		staff.setFemale(true);
		staff.setHomeTown("Trà My - Quảng Ninh");
		staff.setName("Nguyễn Thị D");
		staff.setPhoneNumbers("01647891437");
		staffs.add(staff);
		
		staff = new Staff();
		staff.setAddress("K56. Nguyễn Lương Bằng");
		staff.setDateOfBirth(new Date(1990, 2, 2));
		staff.setFemale(false);
		staff.setHomeTown("Trà My - Quảng Nam");
		staff.setName("Nguyễn Văn E");
		staff.setPhoneNumbers("01647891437");
		staffs.add(staff);
		
		staff = new Staff();
		staff.setAddress("K57. Nguyễn Lương Bằng");
		staff.setDateOfBirth(new Date(1990, 2, 2));
		staff.setFemale(false);
		staff.setHomeTown("Trà My - Quảng Nam");
		staff.setName("Nguyễn Văn F");
		staff.setPhoneNumbers("01647891437");
		staffs.add(staff);
		
		return staffs;
	}
	
	public static List<Student> getStudents() {
		List<Student> students = new ArrayList<>();
		
		Student student = new Student();
		student.setClassName("5A1");
		student.setDateOfBirth(new Date(2007, 2, 2));
		student.setFemale(true);
		student.setLearningCapacity("Khá");
		student.setName("Nguyễn Thị Nhản");
		students.add(student);
		
		student = new Student();
		student.setClassName("4A2");
		student.setDateOfBirth(new Date(2008, 2, 2));
		student.setFemale(true);
		student.setLearningCapacity("Khá");
		student.setName("Nguyễn Thị Bưởi");
		students.add(student);
		
		student = new Student();
		student.setClassName("3A2");
		student.setDateOfBirth(new Date(2009, 2, 2));
		student.setFemale(false);
		student.setLearningCapacity("Giỏi");
		student.setName("Nguyễn Văn Cam");
		students.add(student);

		return students;
	}
}
