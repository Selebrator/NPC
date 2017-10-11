package de.selebrator.npc.metadata;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.inventory.MainHand;

//TODO remove version dependent import

public class FakeHumanMetadata extends FakeLivingMetadata {
	private MetadataObject<Float> absorption = new MetadataObject<>(this.getDataWatcher(), 0.0f, "EntityHuman", "a", 0); //11
	private MetadataObject<Integer> score = new MetadataObject<>(this.getDataWatcher(), 0, "EntityHuman", "b", 1); //12
	private MetadataObject<Byte> skinFlags = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityHuman", "br", 2); //13
	private MetadataObject<Byte> mainHand = new MetadataObject<>(this.getDataWatcher(), (byte) 1, "EntityHuman", "bs", 3); //14
	private MetadataObject<NBTTagCompound> leftShoulder = new MetadataObject<>(this.getDataWatcher(), new NBTTagCompound(), "EntityHuman", "bt", 4); //15
	private MetadataObject<NBTTagCompound> rightShoulder = new MetadataObject<>(this.getDataWatcher(), new NBTTagCompound(), "EntityHuman", "bu", 5); //16

	public FakeHumanMetadata() {
		super();
	}

	public float getAbsorption() {
		return this.absorption.get();
	}

	public void setAbsorption(float absorption) {
		this.absorption.set(absorption > 0 ? absorption : 0);
	}

	public int getScore() {
		return this.score.get();
	}

	public void setScore(int score) {
		this.score.set(score);
	}

	public boolean getSkinFlag(SkinFlag target) {
		return FakeMetadata.getBitmaskValue(this.skinFlags, target.getId());
	}

	public void setSkinFlag(SkinFlag target, boolean state) {
		FakeMetadata.setBitmaskValue(this.skinFlags, target.getId(), state);
	}

	public boolean isCapeEnabled() {
		return this.getSkinFlag(SkinFlag.CAPE);
	}

	public void enableCape(boolean state) {
		this.setSkinFlag(SkinFlag.CAPE, state);
	}

	public boolean isJacketEnabled() {
		return this.getSkinFlag(SkinFlag.JACKET);
	}

	public void enableJacket(boolean state) {
		this.setSkinFlag(SkinFlag.JACKET, state);
	}

	public boolean isLeftArmEnabled() {
		return this.getSkinFlag(SkinFlag.LEFT_SLEEVE);
	}

	public void enableLeftArm(boolean state) {
		this.setSkinFlag(SkinFlag.LEFT_SLEEVE, state);
	}

	public boolean isRightArmEnabled() {
		return this.getSkinFlag(SkinFlag.RIGHT_SLEEVE);
	}

	public void enableRightArm(boolean state) {
		this.setSkinFlag(SkinFlag.RIGHT_SLEEVE, state);
	}

	public boolean isLeftLegEnabled() {
		return this.getSkinFlag(SkinFlag.LEFT_PANTS);
	}

	public void enableLeftLeg(boolean state) {
		this.setSkinFlag(SkinFlag.LEFT_PANTS, state);
	}

	public boolean isRightLegEnabled() {
		return this.getSkinFlag(SkinFlag.RIGHT_PANTS);
	}

	public void enableRightLeg(boolean state) {
		this.setSkinFlag(SkinFlag.RIGHT_PANTS, state);
	}

	public boolean isHatEnabled() {
		return this.getSkinFlag(SkinFlag.HAT);
	}

	public void enableHat(boolean state) {
		this.setSkinFlag(SkinFlag.HAT, state);
	}

	public void setSkinFlags(boolean cape, boolean jacket, boolean leftArm, boolean rightArm, boolean leftLeg, boolean rightLeg, boolean hat) {
		this.skinFlags.set((byte) ((cape ? 1 : 0) << SkinFlag.CAPE.getId() | (jacket ? 1 : 0) << SkinFlag.JACKET.getId() | (leftArm ? 1 : 0) << SkinFlag.LEFT_SLEEVE.getId() | (rightArm ? 1 : 0) << SkinFlag.RIGHT_SLEEVE.getId() | (leftLeg ? 1 : 0) << SkinFlag.LEFT_PANTS.getId() | (rightLeg ? 1 : 0) << SkinFlag.RIGHT_PANTS.getId() | (hat ? 1 : 0) << SkinFlag.HAT.getId()));
	}

	public MainHand getMainHand() {
		return this.mainHand.get() == 1 ? MainHand.RIGHT : MainHand.LEFT;
	}

	public void setMainHand(MainHand mainHand) {
		switch(mainHand) {
			case LEFT:
				this.mainHand.set((byte) 0);
				break;
			case RIGHT:
				this.mainHand.set((byte) 1);
				break;
		}
	}

	public NBTTagCompound getLeftShoulder() {
		return leftShoulder.get();
	}

	public void setLeftShoulder(NBTTagCompound leftShoulder) {
		this.leftShoulder.set(leftShoulder);
	}

	public NBTTagCompound getRightShoulder() {
		return rightShoulder.get();
	}

	public void setRightShoulder(NBTTagCompound rightShoulder) {
		this.rightShoulder.set(rightShoulder);
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
