package dev.foolen.playtime.listeners;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.foolen.playtime.PlayTimePlugin;
import dev.foolen.playtime.databases.PlayerDB;
import dev.foolen.playtime.objects.Player;
import dev.foolen.playtime.utils.Logger;

public class OnQuit implements Listener {

	private Player player;
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		// Get current player from saved data array
	    player = null;
		PlayTimePlugin.getPlayers().forEach(p -> {
			if (p.getUuid() == e.getPlayer().getUniqueId()) {
				player = p;
				return;
			}
		});
		
		// Remove player from array
		PlayTimePlugin.getPlayers().remove(player);
		
		// Get time of quit
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	    String loggedOutAt = formatter.format(new Date());
	    
	    // Compare times and calculate seconds between
	    try {
			Date d1 = formatter.parse(player.getLastLogin());
			Date d2 = formatter.parse(loggedOutAt);
			
			long diff = d2.getTime() - d1.getTime(); //in milliseconds
			long diffSeconds = diff / 1000 % 60; //in seconds
			
			Logger.info(e.getPlayer().getName() + " has been online for a total of " + diffSeconds + " seconds.");
			
			// Add new onlinetime to total playtime
			PlayerDB.savePlayer(e.getPlayer().getUniqueId(), diffSeconds);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
}
