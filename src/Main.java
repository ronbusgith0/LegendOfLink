import display.*;

import javax.swing.*;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame();
		JPanel panel = new LinkPanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setSize(512, 448);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setTitle("LOL");
	}
}