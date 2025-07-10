package cp317;

public class Main {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			JavacGradelyGUI gui = new JavacGradelyGUI();
			gui.setVisible(true);
		});
	}
}
