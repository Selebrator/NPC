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

	public byte getTameableAnimalInfo() {
		return this.tameableAnimalInfo.get();
	}

	public void setTameableAnimalInfo(byte value) {
		this.tameableAnimalInfo.set(value);
	}

	public Optional<UUID> getOwner() {
		return this.owner.get();
	}

	public void setOwner(Optional<UUID> owner) {
		this.owner.set(owner);
	}
}
