package de.selebrator.npc.entity.metadata;

import com.google.common.base.Optional;

import java.util.UUID;

public interface Ownable {

	default boolean hasOwner() {
		return this.getOwner().isPresent();
	}

	Optional<UUID> getOwner();

	default void setOwner(UUID owner) {
		this.setOwner(Optional.fromNullable(owner));
	}

	void setOwner(Optional<UUID> owner);
}
