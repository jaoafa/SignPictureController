package com.jaoafa.SignPictureController.Event;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.SignPictureController.MySQL;
import com.jaoafa.SignPictureController.SignPictureController;

public class OnSignChangeEvent implements Listener {
	JavaPlugin plugin;
	public OnSignChangeEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onSignChangeEvent(SignChangeEvent event){
		Player player = event.getPlayer();
		String[] lines = event.getLines();
		Block block = event.getBlock();
		Location loc = block.getLocation();
		String allline = "";
		for(String line: lines){
			allline = allline + line;
		}
		// https://twitter.com/Kmesuta/status/830812359635591168
		Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");
        Matcher m = p.matcher(allline);
        if(!m.find()){
        	return;
        }
        Statement statement;
		try {
			statement = SignPictureController.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", SignPictureController.sqluser, SignPictureController.sqlpassword);
			try {
				SignPictureController.c = MySQL.openConnection();
				statement = SignPictureController.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				player.sendMessage("[SPC] " + ChatColor.LIGHT_PURPLE + "操作に失敗しました。(ClassNotFoundException/SQLException)");
				player.sendMessage("[SPC] " + ChatColor.LIGHT_PURPLE + "詳しくはサーバコンソールをご確認ください");
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[SPC] " + ChatColor.LIGHT_PURPLE + "操作に失敗しました。(SQLException)");
			player.sendMessage("[SPC] " + ChatColor.LIGHT_PURPLE + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}

		statement = MySQL.check(statement);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			statement.executeUpdate("INSERT INTO signpicture (player, uuid, x, y, z, date) VALUES ('" + player.getName() + "', '" + player.getUniqueId().toString() + "', " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", '" + sdf.format(new Date()) + "');");
			player.sendMessage("[SPC] " + ChatColor.LIGHT_PURPLE + "操作が完了しました。");
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[SPC] " + ChatColor.LIGHT_PURPLE + "操作に失敗しました。(SQLException)");
			player.sendMessage("[SPC] " + ChatColor.LIGHT_PURPLE + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}
	}
}
