package de.selebrator.npc.event;

import de.selebrator.npc.NPC;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NPCAnimationEvent extends NPCEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private Animation animation;

	public NPCAnimationEvent(NPC npc, Animation animation) {
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

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public enum Animation {
		SWING_ARM(0),
		TAKE_DAMAGE(1),
		LEAVE_BED(2),
		EAT_FOOD(3),
		CRITICAL_EFFECT(4),
		MAGIC_CRITICAL_EFFECT(5),
		CROUCH(104),
		UNCROUCH(105);

		private byte id;

		Animation(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return this.id;
		}
	}
}
