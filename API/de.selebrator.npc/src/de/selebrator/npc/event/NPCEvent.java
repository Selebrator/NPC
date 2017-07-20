package de.selebrator.npc.event;

import de.selebrator.npc.NPC;
import org.bukkit.event.Event;

public abstract class NPCEvent extends Event {
	private NPC npc;

	public NPCEvent(NPC npc) {
		this.npc = npc;
	}

	public NPC getNpc() {
		return this.npc;
	}
}
