package me.dennis.chatgui.rooms;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.dennis.chatgui.core.Display;
import me.dennis.chatgui.listeners.Keyboard;
import me.dennis.chatgui.managers.NetworkManager;
import me.dennis.chatgui.protocols.MessageProtocol;
import me.dennis.chatgui.types.Room;

public class RoomChat extends Room {

	public String nickname;

	private int scroll = 0;
	private List<String> log = new ArrayList<String>();
	private String output = "";
	
	@Override
	public void init() {
	}

	@Override
	public void update() {
		if (MessageProtocol.recievedData()) {
			String from = MessageProtocol.getFrom();
			String message = MessageProtocol.getMessage();
			log.add(from + ": " + message);
		}
		for (int i = 0; i < KeyEvent.KEY_LAST; i++) {
			if (Keyboard.isDirect(KeyEvent.VK_SHIFT)) {
				if (Keyboard.isPressed(i)) {
					if (KeyEvent.getKeyText(i).length() == 1) {
						output += KeyEvent.getKeyText(i).toUpperCase();
					}
					otherKeyboardCheck(i);
				}
			}
			else {
				if (Keyboard.isPressed(i)) {
					if (KeyEvent.getKeyText(i).length() == 1) {
						output += KeyEvent.getKeyText(i).toLowerCase();
					}
					otherKeyboardCheck(i);
				}
			}
		}
	}

	public void otherKeyboardCheck(int i) { // ADD SPACES, SLASHES, ETC.
		if (Keyboard.isPressed(KeyEvent.VK_BACK_SPACE)) {
			if (output.length() > 0) {
				output = output.substring(0, output.length() - 1);
			}
		}
		if (Keyboard.isPressed(KeyEvent.VK_ENTER)) {
			if (output.length() > 0) {
				NetworkManager.sendMessage(output);
				output = "";
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(new Color(0xBBBBBB));
		g.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Font font = null;
		try {
			font = Font.createFont(Font.PLAIN, new File("fonts/main.ttf"));
		}
		catch (FontFormatException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		font = font.deriveFont(20F);
		g.setFont(font);
		
		g.setColor(new Color(0xFFFFFF));
		int inputNum = 0;
		for (int i = 0; i < log.size(); i++) {
			g.drawString(log.get(i), 10, font.getSize() + (font.getSize() * i));
			inputNum++;
		}
		g.drawString(nickname + ": " + output, 10, font.getSize() + (font.getSize() * inputNum));
	}

}
