package net.sradonia.bukkit.antibuild;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiBuild extends JavaPlugin {
	
	public static final Logger log = Logger.getLogger("Minecraft");

	private PermissionHandler permissions;
	private FileConfiguration config;
	private boolean multiworldSupport;

	public void onEnable() {
		PluginDescriptionFile pdf = getDescription();

		if (!setupPermissions()) {
			log.severe("[" + pdf.getName() + "] version " + pdf.getVersion() + " not enabled! Permission plugin not detected!");
			return;
		}
		
		//@tyzoid's favorite part
		loadConfig();

		// Register listeners
		PluginManager pluginManager = getServer().getPluginManager();

		String message = getConfigString("build.message");
		MessageSender messageSender = (message == null) ? null : new MessageSender(message, Integer.parseInt(getConfigString("build.messageCooldown")));

		final BListener bl = new BListener(this, messageSender);
		pluginManager.registerEvents(bl, this);
		
		final EListener el = new EListener(this, messageSender);
		pluginManager.registerEvents(el, this);

		if (getConfigString("interaction.check").equalsIgnoreCase("false")) {
			message = getConfigString("interaction.message");
			messageSender = (message == null) ? null : new MessageSender(message, Integer.parseInt(getConfigString("build.messageCooldown")));

			final PListener pl = new PListener(this, messageSender);
			pluginManager.registerEvents(pl, this);

			log.info("[" + pdf.getName() + "] registered interaction listener");
		}

		log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled " + (multiworldSupport ? "with" : "without") + " multiworld support");
	}

	private void loadConfig(){
		if(exists() == false){
		    config = getConfig();
		    config.options().copyDefaults(false);
		    String p1 = "build.message";
		    String p2 = "build.messageCooldown";
		    String p3 = "interaction.check";
		    String p4 = "interaction.message";
		    String p5 = "interaction.messageCooldown";
		    
		    config.addDefault(p1, "You don't have permission to build!");
		    config.addDefault(p2, "3");
		    config.addDefault(p3, "false");
		    config.addDefault(p4, "You don't have permission to interact with the world!");
		    config.addDefault(p5, "3");
		    
		    config.options().copyDefaults(true);
		    saveConfig();
	    	}			
	}

	private boolean exists() {	
		try{
			File file = new File("plugins/AntiBuild/config.yml"); 
	        	if (file.exists()) { 
	        		return true;
	        	}else{
	        		return false;
	        	}

		}catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
			  return true;
		}
	}

	private String getConfigString(String path) {
		config = getConfig();
	    	String message = config.getString(path);
	    	if(message != null){
			message.trim();
			return message;
	    	}else{
	    		return null;
	    	}
	}

	public void onDisable() {
		permissions = null;

		PluginDescriptionFile pdf = getDescription();
		log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " disabled");
	}

	private boolean setupPermissions() {
		Plugin plugin = getServer().getPluginManager().getPlugin("Permissions");
		if (plugin != null) {
			// if we got it, it should already be enabled due to Bukkits dependency resolver.
			permissions = ((Permissions) plugin).getHandler();

			String pluginVersion = plugin.getDescription().getVersion();
			multiworldSupport = pluginVersion.compareTo("2.1") >= 0;
		} else {
			setEnabled(false);
			return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean canBuild(Player player) {
		if (multiworldSupport) {
			String worldName = player.getWorld().getName();
			String group = permissions.getGroup(worldName, player.getName());
			if (group != null)
				return permissions.canGroupBuild(worldName, group);
		} else {
			String group = permissions.getGroup(player.getName());
			if (group != null)
				return permissions.canGroupBuild(group);
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean canInteract(Player player) {
		if (multiworldSupport) {
			String worldName = player.getWorld().getName();
			String group = permissions.getGroup(worldName, player.getName());
			if (group != null)
				return permissions.getGroupPermissionBoolean(worldName, group, "interact"); 
		} else {
			String group = permissions.getGroup(player.getName());
			if (group != null)
				return permissions.getGroupPermissionBoolean(group, "interact"); 
		}
		return true;
	}

}
