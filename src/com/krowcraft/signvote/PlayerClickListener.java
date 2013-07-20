package com.krowcraft.signvote;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClickListener implements Listener{
	
	mainvote plugin;
	
	public PlayerClickListener(mainvote instance){
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		double x, y, z;
		String playerstr;
		Player player = e.getPlayer();
		Player voteplayer;
		Action eAction = e.getAction();
		if (eAction == Action.RIGHT_CLICK_BLOCK ) {
			mainvote.logger.info("Right click block");
			Block clickedblock = e.getClickedBlock();
			mainvote.logger.info("" + clickedblock.getType().getId());
			if(clickedblock.getTypeId() == 68 || clickedblock.getTypeId() ==  63){
				mainvote.logger.info("sign instance");
				Sign sign = (Sign)clickedblock.getState();
				x = sign.getLocation().getBlockX();
				y = sign.getLocation().getBlockY();
				z = sign.getLocation().getBlockZ();
				if(mainvote.selectplayers.containsKey(player.getDisplayName())){
					mainvote.logger.info("contains player");
					mainvote.logger.info(mainvote.selectplayers.get(player.getName()));

					playerstr = sign.getLine(0);
					voteplayer = Bukkit.getServer().getPlayer(playerstr);
					mainvote.votewrite((int) x,(int) y,(int) z,voteplayer.getName(),mainvote.selectplayers.get(player.getName()));
					
				} else {
					
					
				}
			}
		}
	}

}
