/*
 * Student.java
 * Description: This file contains the definition of the Student class with attributes: prn, name, dob, and marks.
 */

public class Student {
	private String prn;
	private String name;
	private String dob;
	private double marks;

	// Constructor to initialize Student object
	public Student(String prn, String name, String dob, double marks) {
		this.prn = prn;
		this.name = name;
		this.dob = dob;
		this.marks = marks;
	}

	// Getter and Setter methods for each attribute

	public String getPrn() {
		return prn;
	}

	public void setPrn(String prn) {
		this.prn = prn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public double getMarks() {
		return marks;
	}

	public void setMarks(double marks) {
		this.marks = marks;
	}

	// Overriding toString() to display student details
	@Override
	public String toString() {
		return "PRN: " + prn + ", Name: " + name + ", DoB: " + dob + ", Marks: " + marks;
	}
}
