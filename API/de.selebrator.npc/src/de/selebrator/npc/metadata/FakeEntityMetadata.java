package de.selebrator.npc.metadata;

public class FakeEntityMetadata extends FakeMetadata {
	private MetadataObject<Byte> status = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "Entity", "Z", 0); //0
	private MetadataObject<Integer> air = new MetadataObject<>(this.getDataWatcher(), 300, "Entity", "aA", 1); //1
	private MetadataObject<String> name = new MetadataObject<>(this.getDataWatcher(), "", "Entity", "aB", 2); //2
	private MetadataObject<Boolean> nameVisible = new MetadataObject<>(this.getDataWatcher(), false, "Entity", "aC", 3); //3
	private MetadataObject<Boolean> silent = new MetadataObject<>(this.getDataWatcher(), false, "Entity", "aD", 4); //4
	private MetadataObject<Boolean> noGravity = new MetadataObject<>(this.getDataWatcher(), false, "Entity", "aE", 5); //5

	private boolean defaultInvisible;
	private boolean defaultGlowing;

	public FakeEntityMetadata() {
		super();
	}

	public boolean getStatus(Status target) {
		return FakeMetadata.getBitmaskValue(this.status, target.getId());
	}

	public void setStatus(Status target, boolean state) {
		FakeMetadata.setBitmaskValue(this.status, target.getId(), state);
	}

	public boolean isOnFire() {
		return this.getStatus(Status.FIRE);
	}

	public void setOnFire(boolean state) {
		this.setStatus(Status.FIRE, state);
	}

	public boolean isSneaking() {
		return this.getStatus(Status.SNEAK);
	}

	public void setSneaking(boolean state) {
		this.setStatus(Status.SNEAK, state);
	}

	public boolean isSprinting() {
		return this.getStatus(Status.SPRINT);
	}

	public void setSprinting(boolean state) {
		this.setStatus(Status.SPRINT, state);
	}

	public boolean isInvisible() {
		return this.getStatus(Status.INVISIBLE);
	}

	public void setInvisible(boolean state) {
		setInvisibleTemp(state);
		this.defaultInvisible = state;
	}

	public boolean isDefaultInvisible() {
		return this.defaultInvisible;
	}

	public void setInvisibleTemp(boolean state) {
		this.setStatus(Status.INVISIBLE, state);
	}

	public boolean isGlowing() {
		return this.getStatus(Status.GLOW);
	}

	public void setGlowing(boolean state) {
		setGlowingTemp(state);
		this.defaultGlowing = state;
	}

	public boolean isDefaultGlowing() {
		return this.defaultGlowing;
	}

	public void setGlowingTemp(boolean state) {
		this.setStatus(Status.GLOW, state);
	}

	public boolean isElytraUsed() {
		return this.getStatus(Status.ELYTRA);
	}

	public void useElytra(boolean state) {
		this.setStatus(Status.ELYTRA, state);
	}

	public void setStatus(boolean fire, boolean sneak, boolean sprint, boolean invisible, boolean glow, boolean elytra) {
		this.status.set((byte) ((fire ? 1 : 0) << Status.FIRE.getId() | (sneak ? 1 : 0) << Status.SNEAK.getId() | (sprint ? 1 : 0) << Status.SPRINT.getId() | (invisible ? 1 : 0) << Status.INVISIBLE.getId() | (glow ? 1 : 0) << Status.GLOW.getId() | (elytra ? 1 : 0) << Status.ELYTRA.getId()));
	}

	public int getAir() {
		return this.air.get();
	}

	public void setAir(int air) {
		this.air.set(air);
	}

	public String getName() {
		return this.name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public boolean isNameVisible() {
		return this.nameVisible.get();
	}

	public void setNameVisible(boolean nameVisible) {
		this.nameVisible.set(nameVisible);
	}

	public boolean isSilent() {
		return this.silent.get();
	}

	public void setSilent(boolean silent) {
		this.silent.set(silent);
	}

	public boolean hasGravity() {
		return !this.noGravity.get();
	}

	public void setGravity(boolean gravity) {
		this.noGravity.set(!gravity);
	}

	public enum Status {
		FIRE(0),
		SNEAK(1),
		SPRINT(3),
		INVISIBLE(5),
		GLOW(6),
		ELYTRA(7);

		private byte id;

		Status(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return id;
		}
	}
}
