package cp317;

public class StudentRecord implements Gradable {
	private String studentID;
	private String studentName;
	private String courseCode;
	private double finalGrade;

	public StudentRecord(Student student, CourseGrade courseGrade) {
		this.studentID = student.getStudentID();
		this.studentName = student.getStudentName();
		this.courseCode = courseGrade.getCourseCode();
		this.finalGrade = courseGrade.getFinalGrade();
	}

	public String getStudentID() {
		return studentID;
	}

	public String getStudentName() {
		return studentName;
	}

	public String getCourseCode() {
		return courseCode;
	}

	@Override
	public double getFinalGrade() {
		return finalGrade;
	}
}
