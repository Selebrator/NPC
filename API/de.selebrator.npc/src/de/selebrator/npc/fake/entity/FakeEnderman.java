package de.selebrator.npc.fake.entity;

import com.google.common.base.Optional;
import de.selebrator.npc.entity.EndermanNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.*;

public class FakeEnderman extends FakeMonster implements EndermanNPC {

	MetadataObject<Optional> carry;
	MetadataObject<Boolean> screaming;

	private static Optional getOptBlockID(int id, int data) {
		if(id == 0)
			return Optional.absent();
		else {
			int number = ((data << 12) & 0b1111) | (id & 0b111111111111);
			Object blockData = METHOD_Block_getByCombinedId.invoke(null, number);
			return Optional.of(blockData);
		}
	}

	@Override
	void initMetadata() {
		super.initMetadata();
		this.carry = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityEnderman_bx, getOptBlockID(0, 0)); //12
		this.screaming = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityEnderman_by, false); //13
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7.0D);
		this.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64.0D);
	}

	@Override
	public int getCarriedCombinedId() {
		return this.carry.get().isPresent() ? METHOD_Block_getCombinedId.invoke(null, this.carry.get()) : 0;
	}

	public void setCarriedMaterial(int id, int data) {
		this.carry.set(getOptBlockID(id, data));
	}

	public boolean isScreaming() {
		return this.screaming.get();
	}

	public void setScreaming(boolean screaming) {
		this.screaming.set(screaming);
	}
}
