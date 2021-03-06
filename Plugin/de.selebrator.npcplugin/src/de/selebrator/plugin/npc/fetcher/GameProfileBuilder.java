package de.selebrator.plugin.npc.fetcher;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.selebrator.plugin.npc.NPCPlugin;
import org.bukkit.ChatColor;

import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.*;
import java.util.UUID;

public class GameProfileBuilder {

	private UUID uuid;
	private String name;
	private String skinOwner;
	private String skinValue;
	private String skinSignature;

	public GameProfileBuilder(String name) {
		this(name, name);
	}

	public GameProfileBuilder(String name, String skinOwner) {
		name = ChatColor.translateAlternateColorCodes('&', name).substring(0, name.length() <= 16 ? name.length() : 16);
		this.name = name;
		name = ChatColor.stripColor(name);
		this.skinOwner = skinOwner;

		this.uuid = createUUID(name);
		createSkin(createUUID(skinOwner));
	}

	public GameProfile build() {
		GameProfile profile = new GameProfile(getUUID(), getName());
		if(getValue() != null && getSignature() != null)
			profile.getProperties().put("textures", new Property("textures", getValue(), getSignature()));
		return profile;
	}

	private UUID createUUID(String name) {
		String mojangAPI = read("https://api.mojang.com/users/profiles/minecraft/" + name);
		try {
			JsonElement uuidElement = new JsonParser().parse(mojangAPI);
			JsonObject uuidObject = uuidElement.getAsJsonObject();

			String uuid = uuidObject.get("id").toString();
			uuid = uuid.substring(1, uuid.length() - 1).replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
			return UUID.fromString(uuid);
		} catch(Exception e) {
			return UUID.fromString("00000000-0000-2000-0000-000000000000");
		}
	}

	private void createSkin(UUID uuid) {
		try {
			String uuidString = uuid.toString().replace("-", "");
			String sessionserver = read("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidString + "?unsigned=false");
			JsonElement mainElement = new JsonParser().parse(sessionserver);
			JsonObject mainObject = mainElement.getAsJsonObject();

			this.skinOwner = mainObject.get("name").getAsString();

			JsonArray properties = mainObject.getAsJsonArray("properties");

			JsonObject propertiesObject = properties.get(0).getAsJsonObject();
			this.skinValue = propertiesObject.get("value").getAsString();
			String signature = propertiesObject.get("signature").toString();
			this.skinSignature = signature.substring(1, signature.length() - 1);
			NPCPlugin.logger.info("Successfully downloaded " + this.skinOwner + "'s Skin.");
		} catch(IllegalStateException e) {
			NPCPlugin.logger.warning("There is no Skin for '" + this.skinOwner + "' available. Using default Skin instead.");
		} catch(NullPointerException e) {
			NPCPlugin.logger.warning("Failed to receive Skin for " + uuid.toString() + ".");
		}
	}

	public String getName() {
		return this.name;
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public String getValue() {
		return this.skinValue;
	}

	public String getSignature() {
		return this.skinSignature;
	}

	private String read(String domain) {
		String result = null;
		try {
			URL url = new URL(domain);
			InputStream stream = url.openStream();
			InputStreamReader inputStreamReader = new InputStreamReader(stream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String content;
			StringBuilder contentBuilder = new StringBuilder();
			while((content = reader.readLine()) != null) {
				contentBuilder.append(content);
			}
			result = contentBuilder.toString();
		} catch(MalformedURLException e) {
			NPCPlugin.logger.warning("Unable to connect to Mojang. :(");
		} catch(UnknownHostException e) {
			NPCPlugin.logger.warning("Unable to connect to Mojang. Check your internet connection.");
		} catch(SSLHandshakeException e) {
			NPCPlugin.logger.warning("Unable to connect to Mojang. Check your internet access.");
		} catch(IOException e) {
			String errorCode = e.getMessage().replace("Server returned HTTP response code: ", "").replace(" for URL: " + domain, "");
			if(errorCode.contains("429")) {
				NPCPlugin.logger.warning("Unable to read Information from Mojang. Try again in 30sec.");
			} else {
				e.printStackTrace();
			}
		}
		return result;
	}
}
