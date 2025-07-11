package cp317;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

	public static List<Student> readNameFile(String filePath, List<String> errors) throws IOException {
		List<Student> students = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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

				String studentID = data[0];
				String studentName = data[1];

				if (!studentID.matches("\\d{9}")) {
					errors.add("Invalid Student ID in NameFile.txt at line " + lineNumber + ": " + studentID);
					continue;
				}

				if (studentName.trim().isEmpty()) {
					errors.add("Empty name in NameFile.txt at line " + lineNumber);
					continue;
				}

				students.add(new Student(studentID, studentName));
			}
		}
		return students;
	}

	public static List<CourseGrade> readCourseFile(String filePath, List<String> errors) throws IOException {
		List<CourseGrade> courses = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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

				String studentID = data[0];
				String courseCode = data[1];

				if (!studentID.matches("\\d{9}")) {
					errors.add("Invalid Student ID in CourseFile.txt at line " + lineNumber + ": " + studentID);
					continue;
				}

				if (!courseCode.matches("[A-Z]{2}\\d{3}")) {
					errors.add("Invalid Course Code in CourseFile.txt at line " + lineNumber + ": " + courseCode);
					continue;
				}

				try {
					double test1 = Double.parseDouble(data[2]);
					double test2 = Double.parseDouble(data[3]);
					double test3 = Double.parseDouble(data[4]);
					double finalExam = Double.parseDouble(data[5]);

					boolean validRange = (test1 >= 0 && test1 <= 100) && (test2 >= 0 && test2 <= 100)
							&& (test3 >= 0 && test3 <= 100) && (finalExam >= 0 && finalExam <= 100);

					if (!validRange) {
						errors.add("Invalid grade value(s) in CourseFile.txt at line " + lineNumber
								+ ": Values must be between 0–100");
						continue;
					}

					courses.add(new CourseGrade(studentID, courseCode, test1, test2, test3, finalExam));

				} catch (NumberFormatException e) {
					errors.add("Invalid number format in CourseFile.txt at line " + lineNumber);
				}
			}
		}
		return courses;
	}
}
