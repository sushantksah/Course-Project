package cp317;

public class Student {
	private String studentID;
	private String studentName;

	public Student(String studentID, String studentName) {
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
