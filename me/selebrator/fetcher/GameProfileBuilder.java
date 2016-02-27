package me.selebrator.fetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class GameProfileBuilder {
	
	private String name;
	private String uuid;
	private String skinOwner;
	private String skinValue;
	private String skinSignature;
	
	public GameProfileBuilder(String name) {
		this.name = name;
		this.skinOwner = name;

		createUUID(this.name);
		createSkin(this.name);
	}
	
	public GameProfileBuilder(String name, String skinOwner) {
		this.name = name;
		this.skinOwner = skinOwner;
		
		createUUID(this.skinOwner);
		createSkin(this.skinOwner);
	}
	
	public GameProfile build() {
		GameProfile profile = new GameProfile(UUID.fromString(getUUID()), getName());
		if(getValue() != null && getSignature() != null)
			profile.getProperties().put("textures", new Property("textures", getValue(), getSignature()));
		return profile;
	}
	
	private void createUUID(String name) {
		String mojangAPI = read("https://api.mojang.com/users/profiles/minecraft/" + name);
		try {
			JsonElement uuidElement = new JsonParser().parse(mojangAPI);
			JsonObject uuiOobject = uuidElement.getAsJsonObject();
			
			String uuid = uuiOobject.get("id").toString();
			this.uuid = uuid.substring(1, uuid.length() - 1);
		} catch (Exception e) {
			this.uuid = "00000000000020000000000000000000";
		}
	}
	
	private void createSkin(String skinOwner) {
		try {
			String sessionserver = read("https://sessionserver.mojang.com/session/minecraft/profile/" + this.uuid + "?unsigned=false");
			JsonElement propertiesElement = new JsonParser().parse(sessionserver);
			JsonObject propertiesObject = propertiesElement.getAsJsonObject();
			
			JsonArray properties = propertiesObject.getAsJsonArray("properties");
			
			JsonObject object = properties.get(0).getAsJsonObject();
			this.skinValue = object.get("value").getAsString();
			String signature = object.get("signature").toString();
			this.skinSignature = signature.substring(1, signature.length() - 1);
			System.out.println("[NPC] Successfully downloaded " + this.skinOwner + "'s Skin");
		} catch (IllegalStateException e) {
			System.err.println("[NPC] There is no Skin for '" +  this.skinOwner + "' available. Using default Skin instead");
		} catch (NullPointerException e) {
			
		}
	}
	
	public String getName() {
		return this.name;
	}

	public String getUUID() {
		return this.uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
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
			String content = null;
			StringBuilder contentBuilder = new StringBuilder();
			while((content = reader.readLine()) != null) {
				contentBuilder.append(content);
			}
			result = contentBuilder.toString();
		} catch (MalformedURLException e) {
			System.err.println("[NPC] Unable to connect to Mojang. :(");
			e.printStackTrace();
			System.err.println("[NPC] Unable to connect to Mojang. :(");
		} catch (IOException e) {
			String errorCode = e.getMessage().replace("Server returned HTTP response code: ", "").replace(" for URL: " + domain, "");
			if(errorCode.contains("429")) {
				System.err.println("[NPC] Unable to read Information from Mojang. Try again in 20sec.");
			} else {
				e.printStackTrace();
			}
				
		}
		return result;
	}
}
