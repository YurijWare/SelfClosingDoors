package com.yurijware.bukkit.selfclosingdoors;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

/**
 * WorldWarp for Bukkit
 * 
 * @author YurijWare
 */
public class SelfClosingDoors extends JavaPlugin {
	private final Logger log = Logger.getLogger("Minecraft");
	protected static PluginDescriptionFile pdfFile = null;
	protected static SelfClosingDoors plugin = null;
	
	private SCDBlockListener blocklistener = new SCDBlockListener();
	
	protected static File maindir = new File("plugins" + File.separatorChar + "SelfClosingDoors");
	protected static File configFile = new File(maindir, "SelfClosingDoors.yml");
	private Configuration conf = new Configuration(configFile);
	
	private int closingTime = 3; 
	
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	
	public SelfClosingDoors(PluginLoader pluginLoader, Server instance,
			PluginDescriptionFile desc, File folder, File plugin,
			ClassLoader cLoader) {
		super(pluginLoader, instance, desc, folder, plugin, cLoader);
	}
	
	public void onEnable() {
		plugin = this;
		pdfFile = this.getDescription();
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.BLOCK_INTERACT, blocklistener,
				Priority.Normal, this);
		
		loadConfig();
		log.info("[" + pdfFile.getName() + "] Version " + pdfFile.getVersion()
				+ " is enabled!");
	}
	
	public void onDisable() {
		log.info("[" + pdfFile.getName() + "] " +
				"Plugin disabled!");
	}
	
	public boolean isDebugging(final Player player) {
		if (debugees.containsKey(player)) {
			return debugees.get(player);
		} else {
			return false;
		}
	}
	
	public void setDebugging(final Player player, final boolean value) {
		debugees.put(player, value);
	}
	
	private void loadConfig(){
		if(!configFile.exists()){
			createConfig();
			return;
		}
		conf.load();
		closingTime = conf.getInt("closing-time", closingTime);
		log.info("[" + pdfFile.getName() + "] " +
				"Closing time set to: " + closingTime);
	}
	
	private void createConfig(){
		if(configFile.exists()) return;
		try {
			maindir.mkdirs();
			configFile.createNewFile();
			conf.setProperty("closing-time", closingTime);
			conf.save();
			log.info("[" + pdfFile.getName() + "] " +
				"Created the config file");
		} catch (IOException e) {
			log.warning("[" + pdfFile.getName() + "] " +
					"Could not create the config file");
		}
	}

	
	public void setClosingTime(int closingTime) {
		this.closingTime = closingTime;
	}
	

	public int getClosingTime() {
		return closingTime;
	}
	
}
