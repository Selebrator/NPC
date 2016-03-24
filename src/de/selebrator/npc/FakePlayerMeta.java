package de.selebrator.npc;

import de.selebrator.reflection.IMethodAccessor;
import de.selebrator.reflection.Reflection;
import de.selebrator.reflection.ServerPackage;
import net.minecraft.server.v1_9_R1.DataWatcher;
import org.bukkit.inventory.MainHand;

public class FakePlayerMeta {

	private DataWatcher dataWatcher;

	private byte status;
	private int air;
	private String name;
	private boolean nameVisible;
	private boolean silent;

	private byte unknown;
	private float health;
	private int potionColor;
	private boolean potionAmbient;
	private int arrows;

	private float absorption;
	private int score;
	private byte skinFlags;
	private MainHand mainHand;


	private static final IMethodAccessor METHOD_DataWatcher_registerObject = Reflection.getMethod(DataWatcher.class, "registerObject", net.minecraft.server.v1_9_R1.DataWatcherObject.class, Object.class);

	public FakePlayerMeta() {
		this.dataWatcher = new DataWatcher(null);
	}

	public void set(DataWatcherObject dataWatcherObject, Object value) {
		METHOD_DataWatcher_registerObject.invoke(this.dataWatcher, dataWatcherObject.getObject(), value);
	}

	public DataWatcher getDataWatcher() {
		return this.dataWatcher;
	}


	// ##### STATUS #####
	// ### GETTER ###
	public boolean isOnFire() {
		return MathHelper.getBit(this.status, EnumStatus.FIRE.getId());
	}

	public boolean isSneaking() {
		return MathHelper.getBit(this.status, EnumStatus.SNEAK.getId());
	}

	public boolean isSprinting() {
		return MathHelper.getBit(this.status, EnumStatus.SPRINT.getId());
	}

	public boolean isInvisible() {
		return MathHelper.getBit(this.status, EnumStatus.INVISIBLE.getId());
	}

	public boolean isGlowing() {
		return MathHelper.getBit(this.status, EnumStatus.GLOW.getId());
	}

	public boolean isElytraUsed() {
		return MathHelper.getBit(this.status, EnumStatus.ELYTRA.getId());
	}

	// ### SETTER ###
	public void setOnFire(boolean state) {
		this.status = MathHelper.setBit(this.status, EnumStatus.FIRE.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public void setSneaking(boolean state) {
		this.status = MathHelper.setBit(this.status, EnumStatus.SNEAK.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public void setSprinting(boolean state) {
		this.status = MathHelper.setBit(this.status, EnumStatus.SPRINT.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public void setInvisible(boolean state) {
		this.status = MathHelper.setBit(this.status, EnumStatus.INVISIBLE.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public void setGlowing(boolean state) {
		this.status = MathHelper.setBit(this.status, EnumStatus.GLOW.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public void useElytra(boolean state) {
		this.status = MathHelper.setBit(this.status, EnumStatus.ELYTRA.getId(), state);
		this.set(DataWatcherObject.ENTITY_STATUS_BITMASK_00, this.status);
	}

	public void setStatus(boolean fire, boolean sneak, boolean sprint, boolean invisible, boolean glow, boolean elytra) {
		this.status = MathHelper.setBit(this.status, EnumStatus.FIRE.getId(), fire);
		this.status = MathHelper.setBit(this.status, EnumStatus.SNEAK.getId(), sneak);
		this.status = MathHelper.setBit(this.status, EnumStatus.SPRINT.getId(), sprint);
		this.status = MathHelper.setBit(this.status, EnumStatus.INVISIBLE.getId(), invisible);
		this.status = MathHelper.setBit(this.status, EnumStatus.GLOW.getId(), glow);
		this.status = MathHelper.setBit(this.status, EnumStatus.ELYTRA.getId(), elytra);
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

	// ##### HEALTH #####
	public float getHealth() {
		return this.health;
	}

	public void setHealth(float health) {
		this.set(DataWatcherObject.LIVING_HEAlTH_06, health);
		this.health = health;
	}

	// ##### POTION_COLOR #####
	public int getPotionColor() {
		return this.potionColor;
	}

	public void setPotionColor(int potionColor) {
		this.set(DataWatcherObject.LIVING_POTION_COLOR_07, potionColor);
		this.potionColor = potionColor;
	}

	// #####POTION_AMBIENT #####
	public boolean isPotionAmbient() {
		return this.potionAmbient;
	}

	public void setPotionAmbient(boolean potionAmbient) {
		this.set(DataWatcherObject.LIVING_POTION_AMBIENT_08, potionAmbient);
		this.potionAmbient = potionAmbient;
	}

	// ##### ARROWS #####
	public int getArrows() {
		return this.arrows;
	}

	public void setArrows(int arrows) {
		this.set(DataWatcherObject.LIVING_ARROWS_09, arrows);
		this.arrows = arrows;
	}

	// ##### ABSORPTION #####
	public float getAbsorption() {
		return this.absorption;
	}

	public void setAbsorption(float absorption) {
		this.set(DataWatcherObject.HUMAN_ABSORPTION_10, absorption);
		this.absorption = absorption;
	}

	// ##### SCORE #####
	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.set(DataWatcherObject.HUMAN_SCORE_11, score);
		this.score = score;
	}

	// ##### SKIN_FLAGS #####
	// ### GETTER ###
	public boolean isCapeEnabled() {
		return MathHelper.getBit(this.skinFlags, EnumSkinFlag.CAPE.getId());
	}

	public boolean isJacketEnabled() {
		return MathHelper.getBit(this.skinFlags, EnumSkinFlag.JACKET.getId());
	}

	public boolean isLeftArmEnabled() {
		return MathHelper.getBit(this.skinFlags, EnumSkinFlag.LEFT_SLEEVE.getId());
	}

	public boolean isRightArmEnabled() {
		return MathHelper.getBit(this.skinFlags, EnumSkinFlag.RIGHT_SLEEVE.getId());
	}

	public boolean isLeftLegEnabled() {
		return MathHelper.getBit(this.skinFlags, EnumSkinFlag.LEFT_PANTS.getId());
	}

	public boolean isRightLegEnabled() {
		return MathHelper.getBit(this.skinFlags, EnumSkinFlag.RIGHT_PANTS.getId());
	}

	public boolean isHatEnabled() {
		return MathHelper.getBit(this.skinFlags, EnumSkinFlag.HAT.getId());
	}

	// ### SETTER ###
	public void enableCape(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.CAPE.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITBASK_12, this.skinFlags);
	}

	public void enableJacket(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.JACKET.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITBASK_12, this.skinFlags);
	}

	public void enableLeftArm(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.LEFT_SLEEVE.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITBASK_12, this.skinFlags);
	}

	public void enableRightArm(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.RIGHT_SLEEVE.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITBASK_12, this.skinFlags);
	}

	public void enableLeftLeg(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.LEFT_PANTS.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITBASK_12, this.skinFlags);
	}

	public void enableRightLeg(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.RIGHT_PANTS.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITBASK_12, this.skinFlags);
	}

	public void enableHat(boolean state) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.HAT.getId(), state);
		this.set(DataWatcherObject.HUMAN_SKIN_BITBASK_12, this.skinFlags);
	}

	public void setSkinFlags(boolean cape, boolean jacket, boolean leftArm, boolean rightArm, boolean leftLeg, boolean rightLeg, boolean hat) {
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.CAPE.getId(), cape);
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.JACKET.getId(), jacket);
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.LEFT_SLEEVE.getId(), leftArm);
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.RIGHT_SLEEVE.getId(), rightArm);
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.LEFT_PANTS.getId(), leftLeg);
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.RIGHT_PANTS.getId(), rightLeg);
		this.skinFlags = MathHelper.setBit(this.skinFlags, EnumSkinFlag.HAT.getId(), hat);
		this.set(DataWatcherObject.HUMAN_SKIN_BITBASK_12, this.skinFlags);
	}

	// ##### MAIN_HAND #####
	public MainHand getMainHand() {
		return this.mainHand;
	}

	public void setMainHand(MainHand mainHand) {
		switch(mainHand) {
			case LEFT:
				this.set(DataWatcherObject.HUMAN_MAINHAND_13, 0);
				break;
			case RIGHT:
				this.set(DataWatcherObject.HUMAN_MAINHAND_13, 1);
				break;
		}
		this.mainHand = mainHand;
	}

	public enum DataWatcherObject {
		ENTITY_STATUS_BITMASK_00("Entity", "ax"),
		ENTITY_AIR_01("Entity", "ay"),
		ENTITY_NAME_02("Entity", "az"),
		ENTITY_NAME_VISIBLE_03("Entity", "aA"),
		ENTITY_SILENT_04("Entity", "aB"),

		LIVING_UNKNOWN_05("EntityLiving", "as"),
		LIVING_HEAlTH_06("EntityLiving", "HEALTH"),
		LIVING_POTION_COLOR_07("EntityLiving", "f"),
		LIVING_POTION_AMBIENT_08("EntityLiving", "g"),
		LIVING_ARROWS_09("EntityLiving", "h"),

		HUMAN_ABSORPTION_10("EntityHuman", "a"),
		HUMAN_SCORE_11("EntityHuman", "b"),
		HUMAN_SKIN_BITBASK_12("EntityHuman", "bp"),
		HUMAN_MAINHAND_13("EntityHuman", "bq");

		private Object object;

		DataWatcherObject(String owner, String field) {
			Class<?> ownerClazz = Reflection.getClass(ServerPackage.NMS, owner);
			this.object = Reflection.getField(ownerClazz, field).get(null);
		}

		public Object getObject() {
			return object;
		}
	}
}
