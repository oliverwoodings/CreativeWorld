package uk.co.oliwali.CreativeWorld;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionUser;
import uk.co.oliwali.CreativeWorld.listeners.CWBlockListener;
import uk.co.oliwali.CreativeWorld.listeners.CWWorldListener;

public class CreativeWorld extends JavaPlugin {
	
	public String name;
	public String version;
	public Config config;
	public static Server server;
	public static MapGenerator generator;
	private CWBlockListener blockListener;
	private CWWorldListener worldListener;

	public void onDisable() {
		Util.info("Version " + version + " disabled!");
	}

	public void onEnable() {
		
		//Set up config and permissions
        PluginManager pm = getServer().getPluginManager();
		server = getServer();
		name = this.getDescription().getName();
        version = this.getDescription().getVersion();
        blockListener = new CWBlockListener();
        worldListener = new CWWorldListener();
        Util.info("Preparing Permissions for CreativeRights...");
        try {
			new Permission();
		} catch (Exception e) {
			pm.disablePlugin(this);
			return;
		}
        config = new Config(this);
        
        //Start Map Generator
    	generator = new MapGenerator(this);
    	if (generator.enabled) {
    		Timer timer = new Timer();
			timer.scheduleAtFixedRate(generator, 5000, Config.MapGenPeriod * 1000);
    	}
    	
    	pm.registerEvent(Type.BLOCK_BREAK, blockListener, Event.Priority.Lowest, this);
    	pm.registerEvent(Type.CHUNK_LOAD, worldListener, Event.Priority.Highest, this);

		Util.info("Version " + version + " enabled!");
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String arg[]) {
		
		List<String> args = Arrays.asList(arg);
		
		if (cmd.getName().equalsIgnoreCase("creativerights") && Permission.promote(sender)) {
			
			if (args.size() != 1) {
				Util.sendMessage(sender, "&cInvalid arguments supplied!");
				return true;
			}
			
			//Check permissions
			PermissionUser user = Permission.manager.getUser(args.get(0));
			if (user.inGroup(Config.CreativeGroup))
				Util.sendMessage(sender, "&7" + args.get(0) + " already has creative rights!");
			else {
				user.addGroup(Config.CreativeGroup);
				Util.sendMessage(sender, "&7" + args.get(0) + " granted creative rights");
			}
			
			//Check config list
			Config.savePlayers();
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("delcreativerights") && Permission.promote(sender)) {
			
			if (args.size() != 1) {
				Util.sendMessage(sender, "&cInvalid arguments supplied!");
				return true;
			}
			
			//Check permissions
			PermissionUser user = Permission.manager.getUser(args.get(0));
			if (!user.inGroup(Config.CreativeGroup))
				Util.sendMessage(sender, "&7" + args.get(0) + " does not have creative rights!");
			else {
				user.removeGroup(Config.CreativeGroup);
				Util.sendMessage(sender, "&7Removed creative rights from " + args.get(0));
			}
			
			//Check config list
			Config.savePlayers();
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("creativerender") && Permission.render(sender)) {
			if (generator.enabled) {
				Util.info("Running map generator...");
				Thread thread = new Thread(generator);
				thread.start();
			}
			else {
				Util.info("Map generator not enabled!");
			}
			return true;
		}
		return false;
	}

}
