package cp317;

public class CourseGrade implements Gradable {
	private String studentID;
	private String courseCode;
	private double test1, test2, test3, finalExam, finalGrade;

	public CourseGrade(String studentID, String courseCode, double test1, double test2, double test3,
			double finalExam) {
		this.studentID = studentID;
		this.courseCode = courseCode;
		this.test1 = test1;
		this.test2 = test2;
		this.test3 = test3;
		this.finalExam = finalExam;
		calculateFinalGrade();
	}

	private void calculateFinalGrade() {
		this.finalGrade = (test1 * 0.2) + (test2 * 0.2) + (test3 * 0.2) + (finalExam * 0.4);
	}

	public String getStudentID() {
		return studentID;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public double getFinalGrade() {
		return finalGrade;
	}
}
