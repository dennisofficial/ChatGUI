package me.dennis.chatgui.protocols;

import me.dennis.chatgui.managers.Protocol;

public class MessageProtocol extends Protocol {

	private static String from = null;
	private static String to = null;
	private static String message = null;
	
	@Override
	public void runData(String data) {
		String[] vals = data.split("\n");
		from = vals[0];
		to = vals[0];
		message = vals[0];
	}
	
	public static boolean recievedData() {
		if (message != null) {
			return true;
		}
		return false;
	}
	
	public static String getFrom() {
		return from;
	}
	public static String getTo() {
		return to;
	}
	public static String getMessage() {
		return message;
	}
	
}
