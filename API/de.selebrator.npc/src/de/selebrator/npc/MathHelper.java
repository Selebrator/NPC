package de.selebrator.npc;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class MathHelper {
	/**
	 * @param dx relative x
	 * @param dy relative y
	 * @param dz relative z
	 * @return distance aka (rho)
	 */
	public static double calcDistance(double dx, double dy, double dz) {
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * @param dx relative x
	 * @param dz relative z
	 * @return yaw aka (theta) in degree
	 */
	public static float calcYaw(double dx, double dz) {
		return (float) Math.toDegrees(Math.atan2(dz, dx)) - 90F;
	}

	/**
	 * @param dx relative x
	 * @param dy relative y
	 * @param dz relative z
	 * @return pitch aka (phi) in degree
	 */
	public static float calcPitch(double dx, double dy, double dz) {
		return (float) -Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
	}

	/**
	 * @param distance aka (rho)
	 * @param yaw      aka (theta) in degrees
	 * @param pitch    aka (phi) in degrees
	 * @return rectangular coordinates
	 */
	public static Vector calcDirectionVector(double distance, float yaw, float pitch) {
		Vector direction = new Vector();
		direction.setX(-Math.sin(Math.toRadians(yaw)));
		direction.setY(-Math.sin(Math.toRadians(pitch)));
		direction.setZ(Math.cos(Math.toRadians(yaw)));
		return direction.multiply(distance);
	}

	/**
	 * @param startX       current location x
	 * @param startY       current location y
	 * @param startZ       current location z
	 * @param destinationX target location x
	 * @param destinationY target location y
	 * @param destinationZ target location z
	 * @return distance between start location and destination location
	 */
	public static Vector calcDistanceVector(double startX, double startY, double startZ, double destinationX, double destinationY, double destinationZ) {
		Vector distance = new Vector();
		distance.setX(destinationX - startX);
		distance.setY(destinationY - startY);
		distance.setZ(destinationZ - startZ);
		return distance;
	}

	/**
	 * @param start       current location
	 * @param destination target location
	 * @return distance between start location and destination location
	 */
	public static Vector calcDistanceVector(Location start, Location destination) {
		return calcDistanceVector(start.getX(), start.getY(), start.getZ(), destination.getX(), destination.getY(), destination.getZ());
	}
}
