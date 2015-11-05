package me.dennis.chatgui.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Keyboard implements KeyListener {

	private static List<Boolean> keys = new ArrayList<Boolean>();
	private static List<Boolean> pkeys = new ArrayList<Boolean>();
	private static List<Boolean> rkeys = new ArrayList<Boolean>();
	
	public static void setupKeys() {
		for (int i = 0; i < KeyEvent.KEY_LAST; i++) {
			keys.add(false);
			pkeys.add(false);
			rkeys.add(false);
		}
	}
	
	public static void reset() {
		for (int i = 0; i < keys.size(); i++) {
			pkeys.set(i, false);
			rkeys.set(i, false);
		}
	}
	
	public static Boolean isDirect(Integer key) {
		return keys.get(key);
	}
	
	public static Boolean isPressed(Integer key) {
		return pkeys.get(key);
	}
	
	public static Boolean isReleased(Integer key) {
		return rkeys.get(key);
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
		keys.set(event.getKeyCode(), true);
		pkeys.set(event.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		keys.set(event.getKeyCode(), false);
		rkeys.set(event.getKeyCode(), true);
	}

	@Override
	public void keyTyped(KeyEvent event) {}

}
