package de.selebrator.events;

import de.selebrator.npc.FakePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NPCSpawnEvent extends NPCEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;

	public NPCSpawnEvent(FakePlayer npc) {
		super(npc);
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
