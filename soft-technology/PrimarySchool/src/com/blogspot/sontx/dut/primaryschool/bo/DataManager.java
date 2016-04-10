package com.blogspot.sontx.dut.primaryschool.bo;

import java.util.ArrayList;
import java.util.List;

import com.blogspot.sontx.dut.primaryschool.bean.Account;
import com.blogspot.sontx.dut.primaryschool.bean.Person;
import com.blogspot.sontx.dut.primaryschool.bean.Staff;
import com.blogspot.sontx.dut.primaryschool.bean.Student;
import com.blogspot.sontx.dut.primaryschool.dao.DataSample;

public class DataManager {
	DataManager() {
	}

	public static boolean kiemTraDangNhap(String usernam, String password) {
		List<Account> accounts = DataSample.getAccounts();
		for (Account account : accounts) {
			if (account.getUsername().equals(usernam) && account.getPassword().equals(password))
				return true;
		}
		return false;
	}

	public static Staff layNhanVien(int who) {
		return DataSample.getStaffs().get(who);
	}

	private static List<Person> timNguoi(String who, Person[] all) {
		List<Person> result = new ArrayList<>();
		WildcardMatcher matcher = new WildcardMatcher(who);
		for (Person person : all) {
			if (matcher.matches(person.getName()))
				result.add(person);
		}
		return result;
	}

	public static Staff[] timKiemGiaoVien(String who) {
		Staff[] staffs = (Staff[]) DataSample.getStaffs().toArray();
		return (Staff[]) timNguoi(who, staffs).toArray();
	}

	public static Student[] timKiemHocSinh(String who) {
		Student[] students = (Student[]) DataSample.getStudents().toArray();
		return (Student[]) timNguoi(who, students).toArray();
	}
}
