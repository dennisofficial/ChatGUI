package me.dennis.chatgui.rooms;

import java.awt.Graphics;

import me.dennis.chatgui.managers.NetworkManager;
import me.dennis.chatgui.types.Room;

public class RoomMain extends Room {

	@Override
	public void init() {
		System.out.println("Connecting to server...");
		if (NetworkManager.connect()) {
			System.out.println("Connected to server!");
		}
		else {
			System.out.println("Could not connect to server...");
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void draw(Graphics g) {
	}

}
