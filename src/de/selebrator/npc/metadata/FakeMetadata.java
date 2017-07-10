package de.selebrator.npc.metadata;

import de.selebrator.NPCPlugin;
import de.selebrator.npc.MathHelper;
import de.selebrator.reflection.ConstructorAccessor;
import de.selebrator.reflection.FieldAccessor;
import de.selebrator.reflection.MethodAccessor;
import de.selebrator.reflection.Reflection;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.inventory.MainHand;

//TODO remove version dependent import

public class FakeMetadata {

	private static final Class CLASS_DataWatcher = Reflection.getMinecraftClass("DataWatcher");
	private static final Class CLASS_Entity = Reflection.getMinecraftClass("Entity");
	private static final Class<?> CLASS_DataWatcherObject = Reflection.getMinecraftClass("DataWatcherObject");
	private static final ConstructorAccessor CONSTRUCTOR_DataWatcher = Reflection.getConstructor(CLASS_DataWatcher, CLASS_Entity);
	private static final MethodAccessor METHOD_DataWatcher_registerObject = Reflection.getMethod(CLASS_DataWatcher, null, "registerObject", CLASS_DataWatcherObject, Object.class);
	private Object dataWatcher;

	//Entity
	private byte status;
	private int air;
	private String name;
	private boolean nameVisible;
	private boolean silent;
	private boolean noGravity;
	//Living
	private byte activeHand;
	private float health;
	private int potionColor;
	private boolean potionAmbient;
	private int arrows;
	//Human
	private float absorption;
	private int score;
	private byte skinFlags;
	private MainHand mainHand;
	private NBTTagCompound leftShoulder;
	private NBTTagCompound rightShoulder;

	private boolean defaultInvisible;
	private boolean defaultGlowing;

	public FakeMetadata() {
		this.dataWatcher = CONSTRUCTOR_DataWatcher.newInstance(new Object[] { null });
	}

	public Object getDataWatcher() {
		return this.dataWatcher;
	}

	// ##### STATUS #####
	public boolean isOnFire() {
		return MathHelper.getBit(this.status, Status.FIRE.getId());
	}

	public void setOnFire(boolean state) {
		this.status = MathHelper.setBit(this.status, Status.FIRE.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public void set(DataWatcherObject dataWatcherObject, Object value) {
		METHOD_DataWatcher_registerObject.invoke(this.dataWatcher, dataWatcherObject.getObject(), value);
	}

	public boolean isSneaking() {
		return MathHelper.getBit(this.status, Status.SNEAK.getId());
	}

	public void setSneaking(boolean state) {
		this.status = MathHelper.setBit(this.status, Status.SNEAK.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public boolean isSprinting() {
		return MathHelper.getBit(this.status, Status.SPRINT.getId());
	}

	public void setSprinting(boolean state) {
		this.status = MathHelper.setBit(this.status, Status.SPRINT.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public boolean isInvisible() {
		return MathHelper.getBit(this.status, Status.INVISIBLE.getId());
	}

	public void setInvisible(boolean state) {
		this.status = MathHelper.setBit(this.status, Status.INVISIBLE.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
		this.defaultInvisible = state;
	}

	public boolean isDefaultInvisible() {
		return this.defaultInvisible;
	}

	public void setInvisibleTemp(boolean state) {
		this.status = MathHelper.setBit(this.status, Status.INVISIBLE.getId(), state || this.defaultInvisible);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public boolean isGlowing() {
		return MathHelper.getBit(this.status, Status.GLOW.getId());
	}

	public void setGlowing(boolean state) {
		this.status = MathHelper.setBit(this.status, Status.GLOW.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
		this.defaultGlowing = state;
	}

	public boolean isDefaultGlowing() {
		return this.defaultGlowing;
	}

	public void setGlowingTemp(boolean state) {
		this.status = MathHelper.setBit(this.status, Status.GLOW.getId(), state || this.defaultGlowing);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public boolean isElytraUsed() {
		return MathHelper.getBit(this.status, Status.ELYTRA.getId());
	}

	public void useElytra(boolean state) {
		this.status = MathHelper.setBit(this.status, Status.ELYTRA.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public void setStatus(boolean fire, boolean sneak, boolean sprint, boolean invisible, boolean glow, boolean elytra) {
		this.status = MathHelper.setBit(this.status, Status.FIRE.getId(), fire);
		this.status = MathHelper.setBit(this.status, Status.SNEAK.getId(), sneak);
		this.status = MathHelper.setBit(this.status, Status.SPRINT.getId(), sprint);
		this.status = MathHelper.setBit(this.status, Status.INVISIBLE.getId(), invisible);
		this.status = MathHelper.setBit(this.status, Status.GLOW.getId(), glow);
		this.status = MathHelper.setBit(this.status, Status.ELYTRA.getId(), elytra);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	// ##### AIR #####
	public int getAir() {
		return this.air;
	}

	public void setAir(int air) {
		this.set(DataWatcherObject.ENTITY_AIR_01, air);
		this.air = air;
	}

	// ##### NAME #####
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.set(DataWatcherObject.ENTITY_NAME_02, name);
		this.name = name;
	}

	// ##### NAME_VISIBLE #####
	public boolean isNameVisible() {
		return this.nameVisible;
	}

	public void setNameVisible(boolean nameVisible) {
		this.set(DataWatcherObject.ENTITY_NAME_VISIBLE_03, nameVisible);
		this.nameVisible = nameVisible;
	}

	// ##### SILENT #####
	public boolean isSilent() {
		return this.silent;
	}

	public void setSilent(boolean silent) {
		this.set(DataWatcherObject.ENTITY_SILENT_04, silent);
		this.silent = silent;
	}

	// ##### NO GRAVITY #####
	public boolean hasGravity() {
		return !this.noGravity;
	}

	public void setGravity(boolean gravity) {
		this.set(DataWatcherObject.ENTITY_NO_GRAVITY_05, !gravity);
		this.noGravity = !gravity;
	}

	// ##### HEALTH #####
	public float getHealth() {
		return this.health;
	}

	public void setHealth(float health) {
		this.set(DataWatcherObject.LIVING_HEAlTH_07, health);
		this.health = health;
	}

	// ##### POTION_COLOR #####
	public int getPotionColor() {
		return this.potionColor;
	}

	public void setPotionColor(int potionColor) {
		this.set(DataWatcherObject.LIVING_POTION_COLOR_08, potionColor);
		this.potionColor = potionColor;
	}

	// #####POTION_AMBIENT #####
	public boolean isPotionAmbient() {
		return this.potionAmbient;
	}

	public void setPotionAmbient(boolean potionAmbient) {
		this.set(DataWatcherObject.LIVING_POTION_AMBIENT_09, potionAmbient);
		this.potionAmbient = potionAmbient;
	}

	// ##### ARROWS #####
	public int getArrows() {
		return this.arrows;
	}

	public void setArrows(int arrows) {
		this.set(DataWatcherObject.LIVING_ARROWS_10, arrows);
		this.arrows = arrows;
	}

	// ##### ABSORPTION #####
	public float getAbsorption() {
		return this.absorption;
	}

	public void setAbsorption(float absorption) {
		this.set(DataWatcherObject.HUMAN_ABSORPTION_11, absorption);
		this.absorption = absorption > 0 ? absorption : 0;
	}

	// ##### SCORE #####
	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.set(DataWatcherObject.HUMAN_SCORE_12, score);
		this.score = score;
	}

	// ##### SKIN_FLAGS #####
	public boolean isCapeEnabled() {
		return MathHelper.getBit(this.skinFlags, SkinFlag.CAPE.getId());
	}

	public void enableCape(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.CAPE.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITMASK_13, this.skinFlags);
	}

	public boolean isJacketEnabled() {
		return MathHelper.getBit(this.skinFlags, SkinFlag.JACKET.getId());
	}

	public void enableJacket(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.JACKET.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITMASK_13, this.skinFlags);
	}

	public boolean isLeftArmEnabled() {
		return MathHelper.getBit(this.skinFlags, SkinFlag.LEFT_SLEEVE.getId());
	}

	public void enableLeftArm(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.LEFT_SLEEVE.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITMASK_13, this.skinFlags);
	}

	public boolean isRightArmEnabled() {
		return MathHelper.getBit(this.skinFlags, SkinFlag.RIGHT_SLEEVE.getId());
	}

	public void enableRightArm(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.RIGHT_SLEEVE.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITMASK_13, this.skinFlags);
	}

	public boolean isLeftLegEnabled() {
		return MathHelper.getBit(this.skinFlags, SkinFlag.LEFT_PANTS.getId());
	}

	public void enableLeftLeg(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.LEFT_PANTS.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITMASK_13, this.skinFlags);
	}

	public boolean isRightLegEnabled() {
		return MathHelper.getBit(this.skinFlags, SkinFlag.RIGHT_PANTS.getId());
	}

	public void enableRightLeg(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.RIGHT_PANTS.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITMASK_13, this.skinFlags);
	}

	public boolean isHatEnabled() {
		return MathHelper.getBit(this.skinFlags, SkinFlag.HAT.getId());
	}

	public void enableHat(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.HAT.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITMASK_13, this.skinFlags);
	}

	public void setSkinFlags(boolean cape, boolean jacket, boolean leftArm, boolean rightArm, boolean leftLeg, boolean rightLeg, boolean hat) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.CAPE.getId(), cape);
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.JACKET.getId(), jacket);
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.LEFT_SLEEVE.getId(), leftArm);
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.RIGHT_SLEEVE.getId(), rightArm);
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.LEFT_PANTS.getId(), leftLeg);
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.RIGHT_PANTS.getId(), rightLeg);
		this.skinFlags = MathHelper.setBit(this.skinFlags, SkinFlag.HAT.getId(), hat);
		this.set(DataWatcherObject.HUMAN_SKIN_BITMASK_13, this.skinFlags);
	}

	// ##### MAIN_HAND #####
	public MainHand getMainHand() {
		return this.mainHand;
	}

	public void setMainHand(MainHand mainHand) {
		switch(mainHand) {
			case LEFT:
				this.set(DataWatcherObject.HUMAN_MAINHAND_14, 0);
				break;
			case RIGHT:
				this.set(DataWatcherObject.HUMAN_MAINHAND_14, 1);
				break;
		}
		this.mainHand = mainHand;
	}

	// ##### SHOULDERS #####
	public NBTTagCompound getLeftShoulder() {
		return leftShoulder;
	}

	public void setLeftShoulder(NBTTagCompound leftShoulder) {
		this.set(DataWatcherObject.HUMAN_LEFT_SHOULDER_15, leftShoulder);
		this.leftShoulder = leftShoulder;
	}

	public NBTTagCompound getRightShoulder() {
		return rightShoulder;
	}

	public void setRightShoulder(NBTTagCompound rightShoulder) {
		this.set(DataWatcherObject.HUMAN_RIGHT_SHOULDER_16, rightShoulder);
		this.rightShoulder = rightShoulder;
	}

	/**
	 * <a href="http://wiki.vg/Entities">Reference</a>
	 */
	//TODO should be updated on each minor version
	public enum DataWatcherObject {
		ENTITY_STATUS_BITMASK_00("Entity", "Z", 0),
		ENTITY_AIR_01("Entity", "aA", 1),
		ENTITY_NAME_02("Entity", "aB", 2),
		ENTITY_NAME_VISIBLE_03("Entity", "aC", 3),
		ENTITY_SILENT_04("Entity", "aD", 4),
		ENTITY_NO_GRAVITY_05("Entity", "aE", 5),

		LIVING_ACTIVE_HAND_06("EntityLiving", "at", 0),
		LIVING_HEAlTH_07("EntityLiving", "HEALTH", 1),
		LIVING_POTION_COLOR_08("EntityLiving", "g", 2),
		LIVING_POTION_AMBIENT_09("EntityLiving", "h", 3),
		LIVING_ARROWS_10("EntityLiving", "br", 4),

		HUMAN_ABSORPTION_11("EntityHuman", "a", 0),
		HUMAN_SCORE_12("EntityHuman", "b", 1),
		HUMAN_SKIN_BITMASK_13("EntityHuman", "br", 2),
		HUMAN_MAINHAND_14("EntityHuman", "bs", 3),
		HUMAN_LEFT_SHOULDER_15("EntityHuman", "bt", 4),
		HUMAN_RIGHT_SHOULDER_16("EntityHuman", "bu", 5);

		private Object object;

		DataWatcherObject(String parent, String fieldName, int index) {
			Class<?> parentClazz = Reflection.getMinecraftClass(parent);
			FieldAccessor field;
			if(NPCPlugin.STABLE)
				field = Reflection.getField(parentClazz, fieldName);
			else
				field = Reflection.getField(parentClazz, CLASS_DataWatcherObject, index);

			this.object = field.get(null);
		}

		public Object getObject() {
			return object;
		}
	}

	public enum Status {
		FIRE(0),
		SNEAK(1),
		SPRINT(3),
		INVISIBLE(5),
		GLOW(6),
		ELYTRA(7);

		byte id;

		Status(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return id;
		}
	}

	public enum SkinFlag {
		CAPE(0),
		JACKET(1),
		LEFT_SLEEVE(2),
		RIGHT_SLEEVE(3),
		LEFT_PANTS(4),
		RIGHT_PANTS(5),
		HAT(6);

		byte id;

		SkinFlag(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return id;
		}
	}
}
