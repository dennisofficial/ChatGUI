package me.dennis.chatgui.managers;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import me.dennis.chatgui.enums.RoomEnum;

public class NetworkManager implements Runnable {

	public static Socket connection;
	public static DataInputStream input;
	public static DataOutputStream output;
	
	public static boolean connect(InetAddress ip) {
		try {
			connection = new Socket(ip, 8231);
			input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
			output = new DataOutputStream(connection.getOutputStream());
			new Thread(new NetworkManager()).start();
			return true;
		} catch (ConnectException ex) {
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public static void sendMessage(String msg) {
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Protocol.parsePacket(NetworkManager.input.readUTF());
			} catch (SocketException e) {
				RoomManager.setRoom(RoomEnum.MAIN);
				try {
					connection.close();
					input.close();
					output.close();
				} catch (IOException e1) {}
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
