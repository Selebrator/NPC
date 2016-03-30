package de.selebrator.npc;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class MathHelper {
	/**
	 *
	 * @param x relative x
	 * @param y relative y
	 * @param z relative z
	 * @return distance aka (rho)
	 */
	public static double calcDistance(double x, double y, double z) {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 *
	 * @param x relative x
	 * @param z relative z
	 * @return yaw aka (theta) in degree
	 */
	public static float calcYaw(double x, double z) {
		return (float) Math.toDegrees(Math.atan2(z, x)) - 90F;
	}

	/**
	 *
	 * @param x relative x
	 * @param y relative y
	 * @param z relative z
	 * @return pitch aka (phi) in degree
	 */
	public static float calcPitch(double x, double y, double z) {
		return (float) -Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z)));
	}

	/**
	 *
	 * @param distance aka (rho)
	 * @param yaw aka (theta) in degrees
	 * @param pitch aka (phi) in degrees
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
	 *
	 * @param startX current location x
	 * @param startY current location y
	 * @param startZ current location z
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
	 *
	 * @param start current location
	 * @param destination target location
	 * @return distance between start location and destination location
	 */
	public static Vector calcDistanceVector(Location start, Location destination) {
		return calcDistanceVector(start.getX(), start.getY(), start.getZ(), destination.getX(), destination.getY(), destination.getZ());
	}

	public static boolean getBit(byte bitMask, int bit) {
		return (bitMask & (1 << bit)) != 0;
	}

	public static byte setBit(byte bitMask, int bit, boolean state) {
		return state ? (byte) (bitMask | (1 << bit)) : (byte) (bitMask & ~(1 << bit));
	}
}
