package de.selebrator.event.npc;

import de.selebrator.npc.EnumAnimation;
import de.selebrator.npc.NPC;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NPCAnimationEvent extends NPCEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private EnumAnimation animation;

	public NPCAnimationEvent(NPC npc, EnumAnimation animation) {
		super(npc);
		this.animation = animation;
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

	public EnumAnimation getAnimation() {
		return animation;
	}

	public void setAnimation(EnumAnimation animation) {
		this.animation = animation;
	}
}
