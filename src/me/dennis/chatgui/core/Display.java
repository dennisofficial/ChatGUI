package me.dennis.chatgui.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import me.dennis.chatgui.listeners.Keyboard;
import me.dennis.chatgui.listeners.Mouse;
import me.dennis.chatgui.managers.RoomManager;

@SuppressWarnings("serial")
public class Display extends JPanel implements ActionListener {

	public static int HEIGHT;
	public static int WIDTH;
	
	public Display() {
		setFocusable(true);
		requestFocus();
		
		addKeyListener(new Keyboard());
		addMouseListener(new Mouse());
		
		RoomManager.init();
		
		new Timer(1, this).start();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		HEIGHT = getHeight();
		WIDTH = getWidth();
		RoomManager.update();
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(0xEEEEEE));
		g.fillRect(0, 0, getWidth(), getHeight());
		RoomManager.draw(g);
		repaint();
	}
	
}
