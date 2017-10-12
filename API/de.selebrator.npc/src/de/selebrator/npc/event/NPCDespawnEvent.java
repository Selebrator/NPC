package de.selebrator.npc.event;

import de.selebrator.npc.NPC;
import org.bukkit.event.*;

public class NPCDespawnEvent extends NPCEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;

	public NPCDespawnEvent(NPC npc) {
		super(npc);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
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
