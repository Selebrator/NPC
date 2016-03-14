package de.selebrator.events;

import de.selebrator.npc.FakePlayer;
import org.bukkit.event.HandlerList;

public class NPCDespawnEvent extends NPCEvent {
	private static final HandlerList handlers = new HandlerList();

	public NPCDespawnEvent(FakePlayer npc) {
		super(npc);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
