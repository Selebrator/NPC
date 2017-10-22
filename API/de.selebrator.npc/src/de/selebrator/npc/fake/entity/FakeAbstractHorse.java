package de.selebrator.npc.fake.entity;

import com.google.common.base.Optional;
import de.selebrator.npc.entity.AbstractHorseNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import java.util.UUID;

import static de.selebrator.npc.fake.Imports.FIELD_EntityHorseAbstract_bI;
import static de.selebrator.npc.fake.Imports.FIELD_EntityHorseAbstract_bJ;

public abstract class FakeAbstractHorse extends FakeAnimal implements AbstractHorseNPC {

	MetadataObject<Byte> abstractHorseInfo;
	MetadataObject<Optional<UUID>> owner; //TODO check again if Mojang uses java.util.Optional

	protected float getModifiedMaxHealth() {
		return 15.0F + this.random.nextInt(8) + this.random.nextInt(9);
	}

	protected double getModifiedMovementSpeed() {
		return (0.44999998807907104D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
	}

	protected double getModifiedJumpStrength() {
		return 0.4000000059604645D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
	}

	@Override
	void initMetadata() {
		super.initMetadata();
		this.abstractHorseInfo = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHorseAbstract_bI, (byte) 0); //13
		this.owner = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHorseAbstract_bJ, Optional.absent()); //14
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.addAttribute(Attribute.HORSE_JUMP_STRENGTH);
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(53.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.22499999403953552D);
	}

	public boolean getAbstractHorseInfo(AbstractHorseInfo target) {
		return MetadataObject.getBitmaskValue(this.abstractHorseInfo, target.getId());
	}

	public void setAbstractHorseInfo(AbstractHorseInfo target, boolean state) {
		MetadataObject.setBitmaskValue(this.abstractHorseInfo, target.getId(), state);
	}

	public Optional<UUID> getOwner() {
		return this.owner.get();
	}

	public void setOwner(Optional<UUID> owner) {
		this.owner.set(owner);
	}
}
