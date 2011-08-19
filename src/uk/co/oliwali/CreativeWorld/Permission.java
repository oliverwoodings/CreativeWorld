package uk.co.oliwali.CreativeWorld;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Permission {
	
	private static PermissionPlugin handler = PermissionPlugin.PERMISSIONSEX;
	public static PermissionManager manager;
	
	public Permission() throws Exception {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
        	handler = PermissionPlugin.PERMISSIONSEX;
        	manager = PermissionsEx.getPermissionManager();
        	Util.info("Using PermissionsEx for user permissions");
        }
        else {
        	Util.info("PermissionsEx not found, disabling CreativeWorld");
        	throw new Exception();
        }
	}
	
	private static boolean hasPermission(CommandSender sender, String node) {
		if (!(sender instanceof Player))
			return true;
		Player player = (Player)sender;
		switch (handler) {
			case PERMISSIONSEX:
				return manager.has(player, node);
		}
		return false;
	}
	
	public static boolean promote(CommandSender player) {
		return hasPermission(player, "creativeworld.promote");
	}
	public static boolean render(CommandSender player) {
		return hasPermission(player, "creativeworld.render");
	}
	public static boolean instaBreak(CommandSender player) {
		return hasPermission(player, "creativeworld.instabreak");
	}
	
	public static boolean inGroup(Player player, String group) {
		return inGroup(player.getName(), group);
	}
	public static boolean inGroup(String player, String group) {
		switch (handler) {
			case PERMISSIONSEX:
				return manager.getUser(player).inGroup(group);
		}
		return false;
	}
	
	private enum PermissionPlugin {
		PERMISSIONSEX
	}

}
