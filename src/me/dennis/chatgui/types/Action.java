package me.dennis.chatgui.types;

public enum Action {

	USERNAME_VERIFY,
	USERNAME_DENY,
	USERNAME_ACCEPT,
	JOINED;
	
	public static Action parseString(String string) {
		for (Action action : values()) {
			if (string.equals(action.name())) {
				return action;
			}
		}
		return null;
	}
	
}
