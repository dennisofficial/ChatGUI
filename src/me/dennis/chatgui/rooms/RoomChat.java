package me.dennis.chatgui.rooms;

import static java.awt.event.KeyEvent.KEY_LAST;
import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_BACK_QUOTE;
import static java.awt.event.KeyEvent.VK_BACK_SLASH;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_QUOTE;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.getKeyText;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
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
		if (MessageProtocol.receivedData()) {
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
		else if (Keyboard.isPressed(VK_ENTER)) {
			if (output.length() > 0) {
				NetworkManager.sendMessage(MessageProtocol.generate(nickname, output));
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
		g.setColor(new Color(0xB0B0B0));
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
		font = font.deriveFont(23F);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);

		// Input Bar
		List<String> input = new ArrayList<String>();
		input.add(output + "_");
		List<String> output = wrapList(input, fm);
		g.setColor(new Color(0x707070));
		g.fillRoundRect(0, Display.HEIGHT - (font.getSize() * output.size()) + 2, Display.WIDTH, (font.getSize() * output.size()) - 2, 5, 5);
		g.setColor(new Color(0xFFFFFF));
		int h;
		for (h = 0; h < output.size(); h++) {
			String[] split = output.get(h).split(":", 2);
			String line = split[1];
			g.drawString(line, 10, Display.HEIGHT - (font.getSize() * (output.size() - h - 1)));
		}
		
		// Message Display
		int inputSize = font.getSize();
		font = font.deriveFont(20F);
		g.setFont(font);
		fm = g.getFontMetrics(font);
		List<String> display = wrapList(log, fm);
		for (int i = 0; i < display.size(); i++) {
			String[] split = display.get(i).split(":", 2);
			String line = split[1];
			int indent = new Integer(split[0]);
			g.drawString(line, 10 + indent, Display.HEIGHT - (inputSize * (display.size() - i + h - 1)));
		}
	}

	private void addLine(List<String> display, String line, int lineNum, int lineWidth) {
		if (lineNum > 1) {
			display.add(lineWidth + ":" + line);
		}
		else {
			display.add("0:" + line);
		}
	}
	
	private List<String> wrapList(List<String> log, FontMetrics fm) {
		List<String> output = new ArrayList<String>();
		for (int i = 0; i < log.size(); i++) {
			String[] words = log.get(i).split(" ");
			String line = "";
			int lineNum = 1;
			int lineWidth = fm.stringWidth(words[0] + " ");
			for (int j = 0; j < words.length; j++) {
				String word = words[j];
				int margin = 20;
				if (lineNum > 1) {
					margin += lineWidth;
				}
				if (fm.stringWidth(word) + margin < Display.WIDTH) {
					if (fm.stringWidth(line + word) + margin < Display.WIDTH) {
						line += word + " ";
					}
					else {
						addLine(output, line.trim(), lineNum, lineWidth);
						lineNum++;
						line = "";
						j--;
					}
				}
				else {
					for (int h = 0; h < word.length(); h++) {
						if (fm.stringWidth(line + word.charAt(h)) + margin < Display.WIDTH) {
							line += word.charAt(h);
						}
						else {
							addLine(output, line, lineNum, lineWidth);
							lineNum++;
							line = "";
						}
					}
				}
			}
			addLine(output, line, lineNum, lineWidth);
		}
		return output;
	}

}
