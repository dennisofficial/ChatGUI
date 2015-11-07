package me.dennis.chatgui.rooms;

import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import me.dennis.chatgui.core.Display;
import me.dennis.chatgui.listeners.Keyboard;
import me.dennis.chatgui.managers.NetworkManager;
import me.dennis.chatgui.protocols.MessageProtocol;
import me.dennis.chatgui.types.Room;

public class RoomChat extends Room {

	public String nickname;

	private HashMap<Integer, String[]> chars = new HashMap<Integer, String[]>();
	private List<String> log = new ArrayList<String>();
	private String output = "";

	public RoomChat() {
		chars.put(VK_SPACE, new String[] {" ", " "});
		chars.put(VK_COMMA, new String[] {",", "<"});
		chars.put(VK_PERIOD, new String[] {".", ">"});
		chars.put(VK_SLASH, new String[] {"/", "?"});
		chars.put(VK_SEMICOLON, new String[] {";", ":"});
		chars.put(VK_QUOTE, new String[] {"'", "\""});
		chars.put(VK_OPEN_BRACKET, new String[] {"[", "{"});
		chars.put(VK_CLOSE_BRACKET, new String[] {"]", "}"});
		chars.put(VK_BACK_SLASH, new String[] {"\\", "|"});
		chars.put(VK_MINUS, new String[]{"-", "_"});
		chars.put(VK_EQUALS, new String[]{"=", "+"});
		chars.put(VK_BACK_QUOTE, new String[]{"`", "~"});
		chars.put(VK_1, new String[]{"1", "!"});
		chars.put(VK_2, new String[]{"2", "@"});
		chars.put(VK_3, new String[]{"3", "#"});
		chars.put(VK_4, new String[]{"4", "$"});
		chars.put(VK_5, new String[]{"5", "%"});
		chars.put(VK_6, new String[]{"6", "^"});
		chars.put(VK_7, new String[]{"7", "&"});
		chars.put(VK_8, new String[]{"8", "*"});
		chars.put(VK_9, new String[]{"9", "("});
		chars.put(VK_0, new String[]{"0", ")"});
	}

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
		for (int i = 0; i < KEY_LAST; i++) {
			if (!(i >= VK_0 && i <= VK_9)) {
				if (Keyboard.isDirect(VK_SHIFT)) {
					if (Keyboard.isPressed(i)) {
						if (getKeyText(i).length() == 1) {
							output += getKeyText(i).toUpperCase();
						}
					}
				}
				else {
					if (Keyboard.isPressed(i)) {
						if (getKeyText(i).length() == 1) {
							output += getKeyText(i).toLowerCase();
						}
					}
				}
			}
		}
		otherKeyboardCheck();
	}

	public void otherKeyboardCheck() {
		if (Keyboard.isPressed(VK_BACK_SPACE)) {
			if (output.length() > 0) {
				output = output.substring(0, output.length() - 1);
			}
		}
		if (Keyboard.isPressed(VK_ENTER)) {
			if (output.length() > 0) {
				NetworkManager.sendMessage(output);
				output = "";
			}
		}
		// Special characters
		for (Entry<Integer, String[]> entry : chars.entrySet()) {
			if (Keyboard.isPressed(entry.getKey())) {
				if (Keyboard.isDirect(VK_SHIFT)) {
					output += entry.getValue()[1];
				}
				else {
					output += entry.getValue()[0];
				}
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
		for (int i = 0; i < log.size(); i++) {
			g.drawString(log.get(i), 10, Display.HEIGHT - font.getSize() - (font.getSize() * i));
		}
		g.drawString(nickname + ": " + output + "_", 10, Display.HEIGHT);
	}

}
