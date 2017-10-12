package de.selebrator.npc.metadata;

import com.google.common.base.Optional;

import java.util.UUID;

public class FakeTameableAnimalMetadata extends FakeAnimalMetadata {
	private MetadataObject<Byte> tameableAnimalInfo = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityTameableAnimal", "bx", 0); //13
	private MetadataObject<Optional<UUID>> owner = new MetadataObject<>(this.getDataWatcher(), Optional.absent(), "EntityHorseAbstract", "by", 1); //14 TODO check again if Mojang uses java.util.Optional

	public FakeTameableAnimalMetadata() {
		super();
	}

	public boolean getTameableAnimalInfo(TameableAnimalInfo target) {
		return FakeMetadata.getBitmaskValue(this.tameableAnimalInfo, target.getId());
	}

	public void setTameableAnimalInfo(TameableAnimalInfo target, boolean state) {
		FakeMetadata.setBitmaskValue(this.tameableAnimalInfo, target.getId(), state);
	}

	public boolean isSitting() {
		return this.getTameableAnimalInfo(TameableAnimalInfo.SITTING);
	}

	public void setSitting(boolean state) {
		this.setTameableAnimalInfo(TameableAnimalInfo.SITTING, state);
	}

	public boolean isAngry() {
		return this.getTameableAnimalInfo(TameableAnimalInfo.ANGRY);
	}

	public void setAngry(boolean state) {
		this.setTameableAnimalInfo(TameableAnimalInfo.ANGRY, state);
	}

	public boolean isTamed() {
		return this.getTameableAnimalInfo(TameableAnimalInfo.TAMED);
	}

	public void setTamed(boolean state) {
		this.setTameableAnimalInfo(TameableAnimalInfo.TAMED, state);
	}

	public boolean hasOwner() {
		return this.getOwner().isPresent();
	}

	public Optional<UUID> getOwner() {
		return this.owner.get();
	}

	public void setOwner(Optional<UUID> owner) {
		this.owner.set(owner);
	}

	public void setOwner(UUID owner) {
		this.setOwner(Optional.fromNullable(owner));
	}

	public enum TameableAnimalInfo {
		SITTING(0),
		ANGRY(1),
		TAMED(2);

		private byte id;

		TameableAnimalInfo(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return this.id;
		}
	}
}
