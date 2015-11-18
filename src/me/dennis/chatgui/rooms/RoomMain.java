package me.dennis.chatgui.rooms;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import static java.awt.event.KeyEvent.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import me.dennis.chatgui.core.Display;
import me.dennis.chatgui.enums.RoomEnum;
import me.dennis.chatgui.listeners.Keyboard;
import me.dennis.chatgui.managers.NetworkManager;
import me.dennis.chatgui.managers.RoomManager;
import me.dennis.chatgui.protocols.ActionProtocol;
import me.dennis.chatgui.protocols.MessageProtocol;
import me.dennis.chatgui.types.Action;
import me.dennis.chatgui.types.Room;

public class RoomMain extends Room {

	public String nickname = "";
	public String ip = "";
	public State state = State.CONNECTING;

	private boolean deniedToNickname = false;
	private boolean verifiedToDenied = false;
	private boolean verifiedToAccept = false;
	private boolean errorToIP = false;

	@Override
	public void init() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			boolean b = NetworkManager.connect(InetAddress.getLocalHost());
			if (b) {
				state = State.NICKNAME;
			} else {
				state = State.ERROR;
			}
		} catch (UnknownHostException e) {
			state = State.ERROR;
		}
	}

	@Override
	public void update() {
		if (state.equals(State.NICKNAME)) {
			if (nickname.length() <= 20) {
				if (Keyboard.isDirect(VK_SHIFT)) {
					for (char c = 'A'; c <= 'Z'; c++) {
						if (Keyboard.isPressed(getExtendedKeyCodeForChar(c))) {
							nickname += new Character(c).toString();
						}
					}
				} else {
					for (char c = 'a'; c <= 'z'; c++) {
						if (Keyboard.isPressed(getExtendedKeyCodeForChar(c))) {
							nickname += new Character(c).toString();
						}
					}
				}
				for (char c = '0'; c <= '9'; c++) {
					if (Keyboard.isPressed(getExtendedKeyCodeForChar(c))) {
						nickname += new Character(c).toString();
					}
				}
			}
			if (nickname.length() > 0) {
				if (Keyboard.isPressed(VK_BACK_SPACE)) {
					nickname = nickname.substring(0, nickname.length() - 1);
				}
				if (Keyboard.isPressed(VK_ENTER)) {
					NetworkManager.sendMessage(ActionProtocol.generateString(null, Action.USERNAME_VERIFY, nickname));
					state = State.VERIFYING;
				}
			}
		}
		else if (state.equals(State.IP)) {
			for (int i = VK_NUMPAD0; i <= VK_NUMPAD9; i++) {
				if (Keyboard.isPressed(i)) {
					String numpad = getKeyText(i);
					ip += numpad.substring(numpad.length() - 1, numpad.length());
				}
			}
			for (char c = '0'; c <= '9'; c++) {
				if (Keyboard.isPressed(getExtendedKeyCodeForChar(c))) {
					ip += new Character(c).toString();
				}
			}
			if (Keyboard.isPressed(VK_PERIOD) || Keyboard.isPressed(VK_DECIMAL) && !Keyboard.isDirect(VK_SHIFT)) {
				ip += '.';
			}
			if (ip.length() > 0) {
				if (Keyboard.isPressed(VK_BACK_SPACE)) {
					ip = ip.substring(0, ip.length() - 1);
				}
			}
			if (Keyboard.isPressed(VK_ENTER)) {
				state = State.CONNECTING;
				try {
					if (NetworkManager.connect(InetAddress.getByName(ip))) {
						state = State.NICKNAME;
					}
					else {
						state = State.ERROR;
					}
				} catch (UnknownHostException e) {
					state = State.ERROR;
				}
				ip = "";
			}
		}
		else if (state.equals(State.ERROR)) {
			errorToIP();
		}
		else if (state.equals(State.VERIFYING)) {
			if (!verifiedToAccept && !verifiedToDenied) {
				if (ActionProtocol.receivedData()) {
					if (ActionProtocol.getAction().equals(Action.USERNAME_DENY)) {
						verifyingToDenied();
					} else if (ActionProtocol.getAction().equals(Action.USERNAME_ACCEPT)) {
						verifyingToAccept();
					}
				}
			}
		}
		else if (state.equals(State.DENIED)) {
			deniedToNickname();
		}
		else if (state.equals(State.ACCEPT)) {
			RoomManager.setRoom(RoomEnum.CHAT);
			NetworkManager.sendMessage("joined");
		}
	}

	public void deniedToNickname() {
		try {
			if (!deniedToNickname) {
				deniedToNickname = true;
				Thread.sleep(2500);
				state = State.NICKNAME;
				nickname = "";
				deniedToNickname = false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void verifyingToDenied() {
		try {
			if (!verifiedToDenied) {
				verifiedToDenied = true;
				Thread.sleep(500);
				state = State.DENIED;
				verifiedToDenied = false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void verifyingToAccept() {
		try {
			if (!verifiedToAccept) {
				verifiedToAccept = true;
				((RoomChat) RoomEnum.CHAT.room).nickname = nickname;
				Thread.sleep(500);
				state = State.ACCEPT;
				verifiedToAccept = false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void errorToIP() {
		try {
			if (!errorToIP) {
				errorToIP = true;
				Thread.sleep(2000);
				state = State.IP;
				errorToIP = false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(new Color(0xB0B0B0));
		g.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Font font = null;
		try {
			font = Font.createFont(Font.PLAIN, new File("fonts/main.otf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		font = font.deriveFont(50F);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);

		g.setColor(new Color(0xFFFFFF));
		if (state.equals(State.CONNECTING)) {
			g.drawString("Connecting...", (Display.WIDTH - fm.stringWidth("Connecting...")) / 2, Display.HEIGHT / 2);
		}
		else if (state.equals(State.IP)) {
			g.drawString("Enter IP:", (Display.WIDTH - fm.stringWidth("Enter IP:")) / 2, Display.HEIGHT / 2 - font.getSize() / 2);
			font = font.deriveFont(30F);
			fm = g.getFontMetrics(font);
			g.setFont(font);
			g.drawString(ip, (Display.WIDTH - fm.stringWidth(ip)) / 2, Display.HEIGHT / 2);
		}
		else if (state.equals(State.ERROR)) {
			g.drawString("Error: Cannot connect!", (Display.WIDTH - fm.stringWidth("Error: Cannot connect!")) / 2, Display.HEIGHT / 2);
		}
		else if (state.equals(State.NICKNAME)) {
			g.drawString("Enter nickname:", (Display.WIDTH - fm.stringWidth("Enter nickname:")) / 2, Display.HEIGHT / 2 - font.getSize() / 2);
			font = font.deriveFont(30F);
			fm = g.getFontMetrics(font);
			g.setFont(font);
			g.drawString(nickname, (Display.WIDTH - fm.stringWidth(nickname)) / 2, Display.HEIGHT / 2);
		}
		else if (state.equals(State.VERIFYING)) {
			g.drawString("Verifying...", (Display.WIDTH - fm.stringWidth("Verifying...")) / 2, Display.HEIGHT / 2);
		}
		else if (state.equals(State.DENIED)) {
			g.drawString("Username taken!", (Display.WIDTH - fm.stringWidth("Username taken!")) / 2, Display.HEIGHT / 2);
		}
	}

	enum State {
		CONNECTING, IP, NICKNAME, ERROR, VERIFYING, DENIED, ACCEPT;
	}

}
