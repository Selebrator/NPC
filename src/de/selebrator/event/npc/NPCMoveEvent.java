package de.selebrator.event.npc;

import de.selebrator.npc.NPC;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NPCMoveEvent extends NPCEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private Location destination;

	public NPCMoveEvent(NPC npc, Location destination) {
		super(npc);
		this.destination = destination;
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

	public Location getDestination() {
		return this.destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}
}
