package cp317;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Student {
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

class CourseGrade {
    private String studentID;
    private String courseCode;
    private double test1;
    private double test2;
    private double test3;
    private double finalExam;
    private double finalGrade;

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

class StudentRecord {
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

    @Override
    public String toString() {
	DecimalFormat df = new DecimalFormat("#.0");
	return String.format("%-10s | %-20s | %-10s | %6s", studentID, studentName, courseCode, df.format(finalGrade));
    }
}

class FileHandler {
    public static List<Student> readNameFile(String filePath, List<String> errors) throws IOException {
	List<Student> students = new ArrayList<>();
	BufferedReader reader = new BufferedReader(new FileReader(filePath));
	String line;
	int lineNumber = 0;

	while ((line = reader.readLine()) != null) {
	    lineNumber++;
	    String[] data = line.split(",\\s*");
	    if (data.length != 2) {
		errors.add("Invalid format in NameFile.txt at line " + lineNumber + ": Expected 2 fields, found "
			+ data.length);
		continue;
	    }

	    if (!data[0].matches("\\d{9}")) {
		errors.add("Invalid Student ID in NameFile.txt at line " + lineNumber + ": " + data[0]);
	    }

	    if (data[1].trim().isEmpty()) {
		errors.add("Invalid Student Name in NameFile.txt at line " + lineNumber + ": Name cannot be empty");
	    }

	    students.add(new Student(data[0], data[1]));
	}
	reader.close();
	return students;
    }

    public static List<CourseGrade> readCourseFile(String filePath, List<String> errors) throws IOException {
	List<CourseGrade> courses = new ArrayList<>();
	BufferedReader reader = new BufferedReader(new FileReader(filePath));
	String line;
	int lineNumber = 0;

	while ((line = reader.readLine()) != null) {
	    lineNumber++;
	    String[] data = line.split(",\\s*");
	    if (data.length != 6) {
		errors.add("Invalid format in CourseFile.txt at line " + lineNumber + ": Expected 6 fields, found "
			+ data.length);
		continue;
	    }

	    if (!data[0].matches("\\d{9}")) {
		errors.add("Invalid Student ID in CourseFile.txt at line " + lineNumber + ": " + data[0]);
	    }

	    if (!data[1].matches("[A-Z]{2}\\d{3}")) {
		errors.add("Invalid Course Code in CourseFile.txt at line " + lineNumber + ": " + data[1]);
	    }

	    try {
		double test1 = Double.parseDouble(data[2]);
		double test2 = Double.parseDouble(data[3]);
		double test3 = Double.parseDouble(data[4]);
		double finalExam = Double.parseDouble(data[5]);

		if (test1 < 0 || test1 > 100 || test2 < 0 || test2 > 100 || test3 < 0 || test3 > 100 || finalExam < 0
			|| finalExam > 100) {
		    errors.add("Invalid grade value in CourseFile.txt at line " + lineNumber);
		}

		courses.add(new CourseGrade(data[0], data[1], test1, test2, test3, finalExam));
	    } catch (NumberFormatException e) {
		errors.add("Invalid grade format in CourseFile.txt at line " + lineNumber);
	    }
	}
	reader.close();
	return courses;
    }

    public static void writeOutputFile(String filePath, List<StudentRecord> records) throws IOException {
	BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
	writer.write("Student ID | Student Name        | Course Code | Final Grade\n");
	writer.write("-----------+----------------------+-------------+------------\n");
	for (StudentRecord record : records) {
	    writer.write(record.toString() + "\n");
	}
	writer.close();
    }
}

public class StudentGradeProcessor {
    public static void main(String[] args) {
	List<String> errors = new ArrayList<>();

	try {
	    System.out.println("Reading input files...");
	    List<Student> students = FileHandler.readNameFile("NameFile.txt", errors);
	    List<CourseGrade> courses = FileHandler.readCourseFile("CourseFile.txt", errors);

	    if (!errors.isEmpty()) {
		System.err.println("\nErrors found in input files:");
		for (String error : errors) {
		    System.err.println(error);
		}
		return;
	    }

	    System.out.println("Processing data...");
	    Map<String, String> studentMap = new HashMap<>();
	    for (Student student : students) {
		studentMap.put(student.getStudentID(), student.getStudentName());
	    }

	    List<StudentRecord> records = new ArrayList<>();
	    for (CourseGrade course : courses) {
		String studentName = studentMap.get(course.getStudentID());
		if (studentName == null) {
		    errors.add("Student ID " + course.getStudentID() + " not found in NameFile.txt");
		    continue;
		}
		records.add(new StudentRecord(new Student(course.getStudentID(), studentName), course));
	    }

	    if (!errors.isEmpty()) {
		System.err.println("\nErrors found during processing:");
		for (String error : errors) {
		    System.err.println(error);
		}
		return;
	    }

	    records.sort(Comparator.comparing(StudentRecord::getStudentID));

	    System.out.println("Writing output file...");
	    FileHandler.writeOutputFile("StudentGrades.txt", records);

	    System.out.println("\nSuccess! Output written to StudentGrades.txt");
	    System.out.println("Processed " + records.size() + " student course records.");

	} catch (IOException e) {
	    System.err.println("Error: " + e.getMessage());
	}
    }
}