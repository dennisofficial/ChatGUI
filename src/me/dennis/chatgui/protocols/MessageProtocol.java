package me.dennis.chatgui.protocols;

import me.dennis.chatgui.managers.Protocol;

public class MessageProtocol extends Protocol {

	private static String from = null;
	private static String to = null;
	private static String message = null;
	
	@Override
	public void runData(String data) {
		String[] vals = data.split("\t");
		from = vals[0];
		to = vals[1];
		message = vals[2];
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

	public static void reset() {
		from = null;
		to = null;
		message = null;
	}
	
}
