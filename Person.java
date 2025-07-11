package cp317;

public class Person {
	protected String studentID;
	protected String studentName;

	public Person(String studentID, String studentName) {
		this.studentID = studentID;
		this.studentName = studentName;
	}

	public String getStudentID() {
		return studentID;
	}

	public String getStudentName() {
		return studentName;
	}
}
