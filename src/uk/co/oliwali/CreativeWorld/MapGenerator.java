package uk.co.oliwali.CreativeWorld;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TimerTask;

import org.jibble.simpleftp.SimpleFTP;

public class MapGenerator extends TimerTask {
	
	private CreativeWorld plugin;
	public boolean enabled = false;
	private boolean useMagick = false;
	
	public MapGenerator(CreativeWorld instance) {
		this.plugin = instance;
		
		//Attempt to locate generator
        Util.info("Attempting to locate map renderer at " + plugin.getDataFolder() + Config.MapRendererLoc);
        File renderer = new File(plugin.getDataFolder() + Config.MapRendererLoc);
        if (renderer.exists()) {
        	enabled = true;
        	Util.info("Map renderer located, map generation period of " + Config.MapGenPeriod + " seconds");
        	Util.info("Map renderer args: " + Config.MapRendererArgs);
        }
        else {
        	Util.info("Map renderer not found, unable to start generator utility");
        	return;
        }
        
        //Attempt to locate ImageMagick
        Util.info("Attempting to locate ImageMagick converter at " + plugin.getDataFolder() + Config.ImageMagickLoc + "convert.exe");
        File magick = new File(plugin.getDataFolder() + Config.ImageMagickLoc + "convert.exe");
        if (magick.exists()) {
        	Util.info("ImageMagick converter located");
        	Util.info("Crop Command: " + Config.CropCommand);
        	Util.info("Composite Command: " + Config.CompositeCommand);
        	Util.info("Thumnail Command: " + Config.ThumbnailCommand);
        	useMagick = true;
        }
        else Util.info("ImageMagick not found, cropping and overlay disabled");
        
	}

	public void run() {
        try {
        	
        	//Render map
    		Util.info("Running map renderer...");
        	Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(plugin.getDataFolder() + Config.MapRendererLoc + " " + Config.MapRendererArgs);
			
			//Read output
			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = null;
            while((line=input.readLine()) != null) {
            	if (line.length() > 5 && line.substring(0,4).equalsIgnoreCase("c10t:")) {
            		Util.info("Error while running renderer: " + line.substring(5));
            		throw new Exception();
            	}
            	if (line.length() > 7 && line.substring(0,6).equalsIgnoreCase("WARNING")) {
            		Util.info("Warning while running renderer: " + line);
            	}
            }
			pr.waitFor();
			Util.info("Map render complete");
			
			//ImageMagick - crop and composite
	        if (useMagick) {
	        	
	        	//Crop command
	        	Util.info("Starting image processing, running crop command");
	        	pr = rt.exec(plugin.getDataFolder() + Config.ImageMagickLoc + Config.CropCommand);
				//Read output
				input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	            line = null;
	            while((line=input.readLine()) != null) {
	            	if (line.length() > 8 && line.substring(0,7).equalsIgnoreCase("convert:")) {
	            		Util.info("Error while running crop command: " + line.substring(8));
	            		throw new Exception();
	            	}
	            }
				
				//Composite command
	        	Util.info("Crop complete, running composite command");
	        	pr = rt.exec(plugin.getDataFolder() + Config.ImageMagickLoc + Config.CompositeCommand);
				//Read output
				input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	            line = null;
	            while((line=input.readLine()) != null) {
	            	if (line.length() > 8 && line.substring(0,7).equalsIgnoreCase("convert:")) {
	            		Util.info("Error while running composite command: " + line.substring(8));
	            		throw new Exception();
	            	}
	            }
	        	
	        	//Thumbnail command
	        	Util.info("Composite complete, running thumbnail command");
	        	pr = rt.exec(plugin.getDataFolder() + Config.ImageMagickLoc + Config.ThumbnailCommand);
				//Read output
				input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	            line = null;
	            while((line=input.readLine()) != null) {
	            	if (line.length() > 8 && line.substring(0,7).equalsIgnoreCase("convert:")) {
	            		Util.info("Error while running thumbnail command: " + line.substring(8));
	            		throw new Exception();
	            	}
	            }
	        	Util.info("Thumbnail complete, finished image processing");
	        	
	        }
	        
	        //FTP upload
	        try {
	        	
	        	Util.info("Attempting to upload map via FTP to " + Config.FTPHost + "...");
	            SimpleFTP ftp = new SimpleFTP();
	            ftp.connect(Config.FTPHost, Config.FTPPort, Config.FTPUser, Config.FTPPass);
	            Util.info("Connected to " + Config.FTPHost);
	            ftp.bin();
	            ftp.cwd("");
	            Util.info("Uploading main map file...");
	            ftp.stor(new File(Config.MapFile));
	            Util.info("Uploading thumbnail file...");
	            ftp.stor(new File(Config.ThumbFile));
	            ftp.disconnect();
	            Util.info("Map uploaded successfully");
	        }
	        catch (IOException e) {
	            Util.severe("Error uploading map via FTP: " + e.getMessage());
	        }

        } catch (Exception e) {
			Util.severe("Unable to run map renderer, aborting generator utility");
			e.printStackTrace();
			this.cancel();
		}
	}

}
