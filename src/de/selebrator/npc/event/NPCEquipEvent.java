package de.selebrator.npc.event;

import de.selebrator.npc.inventory.FakeEquipment;
import de.selebrator.npc.NPC;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class NPCEquipEvent extends NPCEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private FakeEquipment.EquipmentSlot slot;
	private ItemStack item;

	public NPCEquipEvent(NPC npc, FakeEquipment.EquipmentSlot slot, ItemStack item) {
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

	public FakeEquipment.EquipmentSlot getSlot() {
		return slot;
	}

	public void setSlot(FakeEquipment.EquipmentSlot slot) {
		this.slot = slot;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}
}
