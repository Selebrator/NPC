package de.selebrator.event.npc;

import de.selebrator.npc.NPC;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NPCDespawnEvent extends NPCEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;

	public NPCDespawnEvent(NPC npc) {
		super(npc);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
