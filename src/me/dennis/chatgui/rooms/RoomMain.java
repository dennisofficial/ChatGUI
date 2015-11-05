package me.dennis.chatgui.rooms;

import java.awt.Graphics;

import me.dennis.chatgui.core.Display;
import me.dennis.chatgui.types.Room;

public class RoomMain extends Room {

	@Override
	public void init() {
	}

	@Override
	public void update() {
		System.out.println(Display.WIDTH + " - " + Display.HEIGHT);
	}

	@Override
	public void draw(Graphics g) {
	}

}
