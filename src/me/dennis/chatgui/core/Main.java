package me.dennis.chatgui.core;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new Display());
		f.setMinimumSize(new Dimension(480, 360));
		f.setVisible(true);
		f.setResizable(true);
		f.setSize(640, 480);
		f.setLocationRelativeTo(null);
	}
	
}
