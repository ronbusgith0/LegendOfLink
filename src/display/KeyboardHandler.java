package display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;


public class KeyboardHandler implements KeyListener {

	private volatile HashMap<Character, Boolean> keysPressed = new HashMap<Character, Boolean>();

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'e' && !isKeyPressed('e')
				&& Tick.getPlayerControl()) {
			World.getThePlayer().turnLeft();
		}
		keysPressed.put(e.getKeyChar(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.put(e.getKeyChar(), false);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public boolean isKeyPressed(char key) {
		boolean pressed = false;
		try {
			pressed = keysPressed.get(key);
		} catch (NullPointerException e) {}
		return pressed;
	}
}