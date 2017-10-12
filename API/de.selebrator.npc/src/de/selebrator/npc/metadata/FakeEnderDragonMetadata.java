package de.selebrator.npc.metadata;

import org.bukkit.entity.EnderDragon;

public class FakeEnderDragonMetadata extends FakeInsentientMetadata {
	private MetadataObject<Integer> phase = new MetadataObject<>(this.getDataWatcher(), 10, "EntityEnderDragon", "PHASE", 0); //12

	public FakeEnderDragonMetadata() {
		super();
	}

	public EnderDragon.Phase getPhase() {
		switch(this.phase.get()) {
			case 0:
				return EnderDragon.Phase.CIRCLING;
			case 1:
				return EnderDragon.Phase.STRAFING;
			case 2:
				return EnderDragon.Phase.FLY_TO_PORTAL;
			case 3:
				return EnderDragon.Phase.LAND_ON_PORTAL;
			case 4:
				return EnderDragon.Phase.LEAVE_PORTAL;
			case 5:
				return EnderDragon.Phase.BREATH_ATTACK;
			case 6:
				return EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET;
			case 7:
				return EnderDragon.Phase.ROAR_BEFORE_ATTACK;
			case 8:
				return EnderDragon.Phase.CHARGE_PLAYER;
			case 9:
				return EnderDragon.Phase.DYING;
			case 10:
			default:
				return EnderDragon.Phase.HOVER;
		}
	}

	public void setPhase(EnderDragon.Phase phase) {
		switch(phase) {
			case CIRCLING:
				this.phase.set(0);
				break;
			case STRAFING:
				this.phase.set(1);
				break;
			case FLY_TO_PORTAL:
				this.phase.set(2);
				break;
			case LAND_ON_PORTAL:
				this.phase.set(3);
				break;
			case LEAVE_PORTAL:
				this.phase.set(4);
				break;
			case BREATH_ATTACK:
				this.phase.set(5);
				break;
			case SEARCH_FOR_BREATH_ATTACK_TARGET:
				this.phase.set(6);
				break;
			case ROAR_BEFORE_ATTACK:
				this.phase.set(7);
				break;
			case CHARGE_PLAYER:
				this.phase.set(8);
				break;
			case DYING:
				this.phase.set(9);
				break;
			case HOVER:
				this.phase.set(10);
				break;
		}
	}
}
