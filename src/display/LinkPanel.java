package display;

import entity.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class LinkPanel extends JPanel {
	private static final long serialVersionUID = 4170582039177761054L;
	KeyboardHandler kb;
	private JButton startButton;

	public LinkPanel() {
		super();
		startButton = new JButton(new ImageIcon(
				LinkPanel.class.getResource("/assets/title.gif")));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				World.setGameState(1);
			}
		});
		kb = new KeyboardHandler();
		addKeyListener(kb);
		setFocusable(true);
		World.setPanel(this);
		World.setGameState(0);
	}

	@Override
	public void paintComponent(Graphics g) {
		BufferedImage buffer = new BufferedImage(this.getWidth(),
				this.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D bufferGraphics = buffer.createGraphics();
		World.renderTiles(bufferGraphics);
		HUD.drawHud(bufferGraphics);
		for (Entity e : World.getAllEntities()) {
			e.draw(bufferGraphics);
		}
		g.drawImage(buffer, 0, 0, null);
	}

	public void hideStartButton() {
		this.remove(startButton);
	}

	public void showStartButton() {
		this.add(startButton);
	}

}
