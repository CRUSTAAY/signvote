package com.krowcraft.signvote;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;



public class mainvote extends JavaPlugin{
	String player = "Crusty"; 										//debug
	static mainvote instance; 										//in order to make method votewrite static
	
	public static final Logger logger = Logger.getLogger("Minecraft"); //logger
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) 					//make eclipse STFU
	public static HashMap<String, String> selectplayers = new HashMap(); //store for players setting up signvote, Data stored as: player, compname
	
	
	@Override
	public void onEnable(){
	instance = this; 												//in order to make method votewrite static
	getServer().getPluginManager().registerEvents(new PlayerClickListener(this), this);// register playerclicklistener
	}

	@Override
	public void onDisable(){ 										//ondisable
		
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){ 								//sender is player, not console
			Player sentplayer = (Player)sender; 					//make sentplayer var from sender
			if (label.equalsIgnoreCase("signvote")){ 				//if command was /signvote
				if (args.length != 0){ 								//if has args after /signvote
					if(args[0].equalsIgnoreCase("create")){ 		// if first arg was create
						if (sentplayer.hasPermission("signvote.create") || sentplayer.isOp()){ //can player create signvote comps
							if(args.length == 2){ 					//2 args min req to create
								sentplayer.sendMessage("Create"); 	//debug
								createcomp(args[1], sentplayer);
								selectplayers.put(sentplayer.getName(), args[1]); //put in working store with comp name
							} else {
																	//invalid not enough args
								helpplayer(sentplayer); 			//send player help messages
							}
						}
					}else if(args[0].equalsIgnoreCase("delete")){ 	//not implemented atm
						
					}else if(args[0].equalsIgnoreCase("modify")){ 	//not implemented atm
						
					} else { 										//invalid not enough args, send help messages
						helpplayer(sentplayer);
					}
				} else { 											// no args, send help messages
					helpplayer(sentplayer);
					
				}
			}
		} else {
																	//not a player
			
		}
		
		
		return false;
	}
	
	public void createcomp(String string, Player sentplayer) {
		instance.getConfig().set(string + ".owner", sentplayer.getName());
	}

	//TODO: create on comp create and write methods
	public void signinfo(int x, int y, int z, String compplayer, String player){//
		String comp = selectplayers.get(player);
		votewrite(x, y, z, compplayer, comp);
		
	}
	
	public static void votewrite(String comp, String player , int votecount){
		String usekey =comp + "." + player + "." + "votes";
		instance.saveConfig();
		instance.getConfig().set(usekey, votecount);
		instance.saveConfig();
	}
	
	public static void votewrite(int x, int y, int z, String compplayer, String comp){//signs
		String getsignnostr = comp + ".teams." + compplayer + "." + "signno";
		if(instance.getConfig().getInt(getsignnostr) == 0){
			instance.getConfig().set(getsignnostr, 1);
		} else{
			int f = instance.getConfig().getInt(getsignnostr);
			f++;
			instance.getConfig().set(getsignnostr, f);
		}
		int getsignno = instance.getConfig().getInt(getsignnostr);
		String usekey = comp + "." + compplayer + "." + getsignno + "." + "Votesigns";
		String cord = "" + x + ":" + y + ":" + z;
		instance.getConfig();
		instance.getConfig().set(cord, usekey);
		instance.saveConfig();
	}
	
	public int voteread(String key){
		int votecount;
			votecount = (int) this.getConfig().get(key);
		return votecount;
	}
	
	private void helpplayer(Player sentplayer) {
		//display help
		
	}
	
}
