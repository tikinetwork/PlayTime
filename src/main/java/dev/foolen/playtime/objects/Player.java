package dev.foolen.playtime.objects;

import java.util.UUID;

public class Player {

	private UUID uuid;
	private String lastLogin;

	public Player(UUID uuid, String lastLogin) {
		this.uuid = uuid;
		this.lastLogin = lastLogin;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getLastLogin() {
		return lastLogin;
	}
}
