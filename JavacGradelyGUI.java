package cp317;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class JavacGradelyGUI extends JFrame {
	private JTextField nameFileField;
	private JTextField courseFileField;
	private JTable resultTable;
	private DefaultTableModel tableModel;
	private JLabel errorLabel;

	public JavacGradelyGUI() {
		setTitle("Gradely");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 500);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(new Color(30, 30, 47));
		mainPanel.setBorder(BorderFactory.createLineBorder(new Color(52, 39, 90), 2));

		// Logo placeholder
		JLabel logoLabel = new JLabel(new ImageIcon("https://via.placeholder.com/100x100.png?text=A+"));
		logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(logoLabel);

		// Title
		JLabel titleLabel = new JLabel("GRADELY");
		titleLabel.setFont(new Font("Graduate", Font.BOLD, 24));
		titleLabel.setForeground(new Color(224, 224, 224));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(titleLabel);

		// File input panel
		JPanel filePanel = new JPanel();
		filePanel.setBackground(new Color(30, 30, 47));
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

		nameFileField = new JTextField(20);
		nameFileField.setBackground(new Color(63, 44, 110));
		nameFileField.setForeground(Color.WHITE);

		courseFileField = new JTextField(20);
		courseFileField.setBackground(new Color(63, 44, 110));
		courseFileField.setForeground(Color.WHITE);

		JButton nameBrowse = new JButton("Browse...");
		nameBrowse.setBackground(new Color(63, 44, 110));
		nameBrowse.setForeground(Color.WHITE);
		nameBrowse.addActionListener(e -> browseFile(nameFileField));

		JButton courseBrowse = new JButton("Browse...");
		courseBrowse.setBackground(new Color(63, 44, 110));
		courseBrowse.setForeground(Color.WHITE);
		courseBrowse.addActionListener(e -> browseFile(courseFileField));

		filePanel.add(new JLabel("Name File:"));
		filePanel.add(nameFileField);
		filePanel.add(nameBrowse);
		filePanel.add(Box.createRigidArea(new Dimension(0, 10)));

		filePanel.add(new JLabel("Course File:"));
		filePanel.add(courseFileField);
		filePanel.add(courseBrowse);

		mainPanel.add(filePanel);

		// Process button
		JButton processButton = new JButton("Process");
		processButton.setBackground(new Color(63, 44, 110));
		processButton.setForeground(Color.WHITE);
		processButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		processButton.addActionListener(e -> processFiles());

		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		mainPanel.add(processButton);

		// Table setup
		tableModel = new DefaultTableModel(new String[] { "Student ID", "Student Name", "Course Code", "Final Grade" },
				0);
		resultTable = new JTable(tableModel);
		resultTable.setBackground(new Color(30, 30, 47));
		resultTable.setForeground(Color.WHITE);
		resultTable.setGridColor(new Color(52, 39, 90));
		JScrollPane scrollPane = new JScrollPane(resultTable);
		scrollPane.setPreferredSize(new Dimension(550, 250));
		mainPanel.add(scrollPane);

		// Error label
		errorLabel = new JLabel();
		errorLabel.setForeground(Color.RED);
		errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		mainPanel.add(errorLabel);

		add(mainPanel);
	}

	private void browseFile(JTextField field) {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			field.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	private void processFiles() {
		List<String> errors = new ArrayList<>();
		String nameFilePath = nameFileField.getText();
		String courseFilePath = courseFileField.getText();

		if (nameFilePath.isEmpty() || courseFilePath.isEmpty()) {
			errorLabel.setText("Please select both NameFile.txt and CourseFile.txt.");
			return;
		}

		try {
			List<Student> students = FileHandler.readNameFile(nameFilePath, errors);
			List<CourseGrade> courses = FileHandler.readCourseFile(courseFilePath, errors);

			if (!errors.isEmpty()) {
				errorLabel.setText("<html>Errors found:<br>" + String.join("<br>", errors) + "</html>");
				return;
			}

			Map<String, String> studentMap = new HashMap<>();
			for (Student student : students) {
				studentMap.put(student.getStudentID(), student.getStudentName());
			}

			List<StudentRecord> records = new ArrayList<>();
			for (CourseGrade course : courses) {
				String studentName = studentMap.get(course.getStudentID());
				if (studentName != null) {
					records.add(new StudentRecord(new Student(course.getStudentID(), studentName), course));
				} else {
					errors.add("Student ID " + course.getStudentID() + " not found in NameFile.txt");
				}
			}

			if (!errors.isEmpty()) {
				errorLabel.setText("<html>Errors during processing:<br>" + String.join("<br>", errors) + "</html>");
				return;
			}

			records.sort(Comparator.comparing(StudentRecord::getStudentID));
			updateTable(records);

			errorLabel.setText("Processed " + records.size() + " student course records successfully.");

		} catch (IOException e) {
			errorLabel.setText("Error: " + e.getMessage());
		}
	}

	private void updateTable(List<StudentRecord> records) {
		tableModel.setRowCount(0);
		DecimalFormat df = new DecimalFormat("#.0");
		for (StudentRecord record : records) {
			tableModel.addRow(new Object[] { record.getStudentID(), record.getStudentName(), record.getCourseCode(),
					df.format(record.getFinalGrade()) });
		}
	}
}
