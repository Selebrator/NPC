package de.selebrator.npc.metadata;

import com.google.common.base.Optional;
import de.selebrator.npc.entity.FakeEntity;

import java.util.UUID;

public class FakeAbstractHorseMetadata extends FakeAnimalMetadata {
	private MetadataObject<Byte> abstractHorseInfo = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityHorseAbstract", "bI", 0); //13
	private MetadataObject<Optional<UUID>> owner = new MetadataObject<>(this.getDataWatcher(), Optional.absent(), "EntityHorseAbstract", "bJ", 1); //14 TODO check again if Mojang uses java.util.Optional

	public FakeAbstractHorseMetadata() {
		super();
	}

	public boolean getAbstractHorseInfo(AbstractHorseInfo target) {
		return FakeEntity.getBitmaskValue(this.abstractHorseInfo, target.getId());
	}

	public void setAbstractHorseInfo(AbstractHorseInfo target, boolean state) {
		FakeEntity.setBitmaskValue(this.abstractHorseInfo, target.getId(), state);
	}

	public boolean isTamed() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.TAMED);
	}

	public void setTamed(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.TAMED, state);
	}

	public boolean isSaddled() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.SADDLED);
	}

	public void setSaddled(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.SADDLED, state);
	}

	public boolean isBred() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.BRED);
	}

	public void setBred(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.BRED, state);
	}

	public boolean isEating() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.EATING);
	}

	public void setEating(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.EATING, state);
	}

	public boolean isRearing() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.REARING);
	}

	public void setRearing(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.REARING, state);
	}

	public boolean isMouthOpen() {
		return this.getAbstractHorseInfo(AbstractHorseInfo.MOUTH_OPEN);
	}

	public void setMouthOpen(boolean state) {
		this.setAbstractHorseInfo(AbstractHorseInfo.MOUTH_OPEN, state);
	}

	public boolean hasOwner() {
		return this.getOwner().isPresent();
	}

	public Optional<UUID> getOwner() {
		return this.owner.get();
	}

	public void setOwner(UUID owner) {
		this.setOwner(Optional.fromNullable(owner));
	}

	public void setOwner(Optional<UUID> owner) {
		this.owner.set(owner);
	}

	public enum AbstractHorseInfo {
		TAMED(1),
		SADDLED(2),
		BRED(3),
		EATING(4),
		REARING(5),
		MOUTH_OPEN(6);

		private byte id;

		AbstractHorseInfo(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return this.id;
		}
	}
}
