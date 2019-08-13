package dev.foolen.playtime;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dev.foolen.playtime.commands.PlayTime;
import dev.foolen.playtime.databases.mysql.MySQL;
import dev.foolen.playtime.listeners.OnJoin;
import dev.foolen.playtime.listeners.OnQuit;
import dev.foolen.playtime.objects.Player;

public class PlayTimePlugin extends JavaPlugin {

	public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "PlayTime" + ChatColor.DARK_GRAY
			+ "]" + ChatColor.AQUA + " ";
	
	private static PlayTimePlugin instance;
	private static ArrayList<Player> players;
	
	private MySQL mysql;
	
	@Override
	public void onEnable() {
		instance = this;
		players = new ArrayList<Player>();

		// Load config
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		// Open DB connection
		mysql = new MySQL();
		
		// Register events
		registerEvents();
		
		// Register commands
		registerCommands();
	}

	@Override
	public void onDisable() {
		// Close DB connection
		mysql.close();
	}
	
	public static PlayTimePlugin getInstance() {
		return instance;
	}
	
	public static ArrayList<Player> getPlayers() {
		return players;
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new OnJoin(), this);
		pm.registerEvents(new OnQuit(), this);
	}
	
	private void registerCommands() {
		getCommand("playtime").setExecutor(new PlayTime());
	}

}
