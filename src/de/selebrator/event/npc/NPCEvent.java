package de.selebrator.event.npc;

import de.selebrator.npc.FakePlayer;
import org.bukkit.event.Event;

public abstract class NPCEvent extends Event {
	private FakePlayer npc;

	public NPCEvent(FakePlayer npc) {
		this.npc = npc;
	}

	public FakePlayer getNpc() {
		return this.npc;
	}
}
