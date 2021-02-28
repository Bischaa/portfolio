public class Main {

	public static void main(String[] args) {
		Menu menu = new Menu();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				menu.createMenu();
			}
		});
	}
}