package dev.foolen.playtime.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev.foolen.playtime.PlayTimePlugin;
import dev.foolen.playtime.objects.Player;

public class OnJoin implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String lastLogin = formatter.format(new Date());

		PlayTimePlugin.getPlayers().add(new Player(e.getPlayer().getUniqueId(), lastLogin));
	}
}
