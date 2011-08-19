package uk.co.oliwali.CreativeWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.util.config.Configuration;

import ru.tehkode.permissions.PermissionUser;

/**
 * Configuration manager for CreativeWorld.
 * Any field with the first letter capitalised is a config option
 * @author oliverw92
 */
public class Config {
	
	public static String CreativeGroup;
	public static String CreativeWorld;
	public static int MapGenPeriod;
	public static String MapFile;
	public static String MapRendererLoc;
	public static String MapRendererArgs;
	public static String ImageMagickLoc;
	public static String CropCommand;
	public static String CompositeCommand;
	public static String ThumbnailCommand;
	public static String ThumbFile;
	public static String FTPHost;
	public static String FTPUser;
	public static String FTPPass;
	public static int FTPPort;
	public static int InstaTool;
	public static boolean DisableChunkGen;
	
	
	private static Configuration config;
	
	/**
	 * Loads the config from file and validates the data
	 * @param plugin
	 */
	public Config(CreativeWorld plugin) {
		
		config = plugin.getConfiguration();
		
		//Check general settings
		if (config.getProperty("creative-rights.creative-players") == null)
			config.setProperty("creative-rights.creative-players", Arrays.asList(new String[]{"oliverw92"}));
		if (config.getProperty("creative-rights.creative-group") == null)
			config.setProperty("creative-rights.creative-group", "builder");
		if (config.getProperty("creative-rights.creative-world") == null)
			config.setProperty("creative-rights.creative-world", "creative1");
		if (config.getProperty("map-generator.map-generation-period") == null)
			config.setProperty("map-generator.map-generation-period", 3600);
		if (config.getProperty("map-generator.map-renderer-location") == null)
			config.setProperty("map-generator.map-renderer-location", "\\c10t\\c10t.exe");
		if (config.getProperty("map-generator.map-renderer-args") == null)
			config.setProperty("map-generator.map-renderer-args", "-w M:\\\\0_TEST\\creative1 -o %MAP%");
		if (config.getProperty("map-generator.imagemagick-location") == null)
			config.setProperty("map-generator.imagemagick-location", "\\ImageMagick\\");
		if (config.getProperty("map-generator.crop-command") == null)
			config.setProperty("map-generator.crop-command", "convert -crop 5325x5340+736+708 %MAP% %MAP%");
		if (config.getProperty("map-generator.composite-command") == null)
			config.setProperty("map-generator.composite-command", "convert -composite -gravity Center %MAP% map-overlay.png %MAP%");
		if (config.getProperty("map-generator.thumbnail-command") == null)
			config.setProperty("map-generator.thumbnail-command", "convert -resize 800x800 %MAP% M:\\0_TEST\\plugins\\CreativeWorld\\creative-thumb.png");
		if (config.getProperty("map-generator.ftp.host") == null)
			config.setProperty("map-generator.ftp.host", "play.minecraftcc.com");
		if (config.getProperty("map-generator.ftp.user") == null)
			config.setProperty("map-generator.ftp.user", "admin");
		if (config.getProperty("map-generator.ftp.pass") == null)
			config.setProperty("map-generator.ftp.pass", "password");
		if (config.getProperty("map-generator.ftp.port") == null)
			config.setProperty("map-generator.ftp.port", 21);
		if (config.getProperty("map-generator.map-file") == null)
			config.setProperty("map-generator.map-file", "M:\\0_TEST\\plugins\\CreativeWorld\\creative-thumb.png");
		if (config.getProperty("map-generator.thumb-file") == null)
			config.setProperty("map-generator.thumb-file", "M:\\0_TEST\\plugins\\CreativeWorld\\creative.png");
		if (config.getProperty("insta-break.tool") == null)
			config.setProperty("insta-break.tool", 285);
		if (config.getProperty("chunk-gen.disable-chunk-gen") == null);
			config.setProperty("chunk-gen.disable-chunk-gen", true);
		
		//Attempt a save
		if (!config.save())
			Util.severe("Error while writing to config.yml");

		//Load values
		CreativeGroup = config.getString("creative-rights.creative-group", null);
		CreativeWorld = config.getString("creative-rights.creative-world", "creative1");
		MapFile = config.getString("map-generator.map-file", "M:\\0_TEST\\plugins\\CreativeWorld\\creative.png");
		ThumbFile = config.getString("map-generator.thumbnail-file", "M:\\0_TEST\\plugins\\CreativeWorld\\creative-thumb.png");
		MapGenPeriod = config.getInt("map-generator.map-generation-period", 3600);
		MapRendererLoc = config.getString("map-generator.map-renderer-location", "\\c10t\\c10t.exe");
		MapRendererArgs = config.getString("map-generator.map-renderer-args", "-w M:\\\\0_TEST\\creative1 -o .%MAP%").replace("%MAP%", MapFile);
		ImageMagickLoc = config.getString("map-generator.imagemagick-location", "\\ImageMagick\\");
		CropCommand = config.getString("map-generator.crop-command", "convert -crop 5325x5340+736+708 %MAP% %MAP%").replace("%MAP%", MapFile);
		CompositeCommand = config.getString("map-generator.composite-command", "convert -composite -gravity Center %MAP% ../map-overlay.png %MAP%").replace("%MAP%", MapFile);
		ThumbnailCommand = config.getString("map-generator.thumbnail-command", "convert -resize 800x800 %MAP% %THUMB%").replace("%MAP%", MapFile).replace("%THUMB%", ThumbFile);
		FTPHost = config.getString("map-generator.ftp.host", "play.minecraftcc.com");
		FTPUser = config.getString("map-generator.ftp.user", "admin");
		FTPPass = config.getString("map-generator.ftp.pass", "password");
		FTPPort = config.getInt("map-generator.ftp.port", 21);
		InstaTool = config.getInt("insta-break.tool", 285);
		DisableChunkGen = config.getBoolean("chunk-gen.disable-chunk-gen", true);
		
		for (String name : config.getStringList("creative-rights.creative-players", null))
			Permission.manager.getUser(name).addGroup(CreativeGroup);

	}
	
	public static void savePlayers() {
		List<String> players = new ArrayList<String>();
		for (PermissionUser user : Permission.manager.getUsers(CreativeGroup))
			players.add(user.getName());
		config.setProperty("creative-rights.creative-players", players);
	}

}