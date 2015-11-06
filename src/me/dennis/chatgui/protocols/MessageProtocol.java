package me.dennis.chatgui.protocols;

import me.dennis.chatgui.managers.Protocol;

public class MessageProtocol extends Protocol {

	private static String from = null;
	private static String message = null;
	
	@Override
	public void runData(String data) {
		String[] vals = data.split("\t");
		from = vals[0];
		message = vals[1];
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
	public static String getMessage() {
		return message;
	}

	public static void reset() {
		from = null;
		message = null;
	}
	
}
