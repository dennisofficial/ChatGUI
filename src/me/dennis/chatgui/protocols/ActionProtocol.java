package me.dennis.chatgui.protocols;

import me.dennis.chatgui.managers.Protocol;
import me.dennis.chatgui.types.Action;

public class ActionProtocol extends Protocol {

	private static String from;
	private static Action action;
	private static String data;
	
	@Override
	public void runData(String data) {
		String[] vals = data.split("\t", 3);
		from = vals[0];
		action = Action.parseString(vals[1]);
		data = vals[2];
	}
	
	public static boolean receivedData() {
		if (from != null) {
			return true;
		}
		return false;
	}
	
	public static String getFrom() {
		return from;
	}
	
	public static Action getAction() {
		return action;
	}
	
	public static String getData() {
		return data;
	}
	
	public static String generateString(String from, Action action, String data) {
		return from + "\t" + action.name() + "\t" + data;
	}

}
