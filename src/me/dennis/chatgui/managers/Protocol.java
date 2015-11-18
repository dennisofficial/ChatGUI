package me.dennis.chatgui.managers;

import me.dennis.chatgui.protocols.ActionProtocol;
import me.dennis.chatgui.protocols.MessageProtocol;

public abstract class Protocol {

	public abstract void runData(String data);
	
	public static void parsePacket(String msg) {
		for (Enum p : Enum.values()) {
			try {
				String[] vals = msg.split("\t", 2);
				String protocol = vals[0];
				String data = vals[1];
				if (protocol.equals(p.label)) {
					p.protocol.runData(data);
				}
			} catch (Exception ex) {}
		}
	}
	
	enum Enum {
		
		MESSAGE("msg", new MessageProtocol()),
		ACTION("action", new ActionProtocol());
		
		public String label;
		public Protocol protocol;
		
		private Enum(String label, Protocol protocol) {
			this.label = label;
			this.protocol = protocol;
		}
		
	}
	
}
