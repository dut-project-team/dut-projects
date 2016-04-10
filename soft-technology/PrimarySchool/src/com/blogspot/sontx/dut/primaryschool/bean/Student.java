package com.blogspot.sontx.dut.primaryschool.bean;

public class Student extends Person {
	private String className;
	private String learningCapacity;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLearningCapacity() {
		return learningCapacity;
	}

	public void setLearningCapacity(String learningCapacity) {
		this.learningCapacity = learningCapacity;
	}
}
