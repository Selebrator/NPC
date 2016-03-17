package de.selebrator.event.npc;

import de.selebrator.npc.EnumEquipmentSlot;
import de.selebrator.npc.FakePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class NPCEquipEvent extends NPCEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private EnumEquipmentSlot slot;
	private ItemStack item;

	public NPCEquipEvent(FakePlayer npc, EnumEquipmentSlot slot, ItemStack item) {
		super(npc);
		this.slot = slot;
		this.item = item;
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

	public EnumEquipmentSlot getSlot() {
		return slot;
	}

	public void setSlot(EnumEquipmentSlot slot) {
		this.slot = slot;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}
}
