package de.selebrator.npc.event;

import de.selebrator.npc.NPC;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class NPCDamageEvent extends NPCEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private int amount;
	private EntityDamageEvent.DamageCause cause;

	public NPCDamageEvent(NPC npc, int amount, EntityDamageEvent.DamageCause cause) {
		super(npc);
		this.amount = amount;
		this.cause = cause;
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
		return this.cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public EntityDamageEvent.DamageCause getCause() {
		return this.cause;
	}

	public void setCause(EntityDamageEvent.DamageCause cause) {
		this.cause = cause;
	}
}
