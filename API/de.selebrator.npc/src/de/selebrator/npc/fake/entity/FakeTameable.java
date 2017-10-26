package de.selebrator.npc.fake.entity;

import com.google.common.base.Optional;
import de.selebrator.npc.entity.TameableNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;

import java.util.UUID;

import static de.selebrator.npc.fake.Imports.FIELD_EntityTameableAnimal_bx;
import static de.selebrator.npc.fake.Imports.FIELD_EntityTameableAnimal_by;

public abstract class FakeTameable extends FakeAnimal implements TameableNPC {

	MetadataObject<Byte> tameableAnimalInfo;
	MetadataObject<Optional<UUID>> owner;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.tameableAnimalInfo = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityTameableAnimal_bx, (byte) 0); //13
		this.owner = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityTameableAnimal_by, Optional.absent()); //14 TODO check again if Mojang uses java.util.Optional
	}

	public boolean getTameableAnimalInfo(TameableAnimalInfo target) {
		return MetadataObject.getBitmaskValue(this.tameableAnimalInfo, target.getId());
	}

	public void setTameableAnimalInfo(TameableAnimalInfo target, boolean state) {
		MetadataObject.setBitmaskValue(this.tameableAnimalInfo, target.getId(), state);
	}

	public Optional<UUID> getOwner() {
		return this.owner.get();
	}

	public void setOwner(Optional<UUID> owner) {
		this.owner.set(owner);
	}
}
