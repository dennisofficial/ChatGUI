package me.dennis.chatgui.managers;

import java.awt.Graphics;
import java.awt.Graphics2D;

import me.dennis.chatgui.enums.RoomEnum;
import me.dennis.chatgui.types.Room;

public class RoomManager {

	public static Room room = RoomEnum.MAIN.room;
	
	public static void setRoom(RoomEnum room) {
		RoomManager.room = room.room;
		room.room.init();
	}

	public static void init() {
		room.init();
	}
	
	public static void update() {
		room.update();
	}
	
	public static void draw(Graphics g) {
		room.draw((Graphics2D) g);
	}
	
}
