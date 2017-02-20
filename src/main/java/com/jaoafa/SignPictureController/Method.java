package com.jaoafa.SignPictureController;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * 主要メゾット
 * @author mine_book000
 */
public class Method {
	JavaPlugin plugin;
	public Method(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/**
	 * 「[COMMAND] Text」に変換した上でsenderに送信する
	 * @param sender 送信先
	 * @param cmd コマンド情報
	 * @param text 送信テキスト
	 * @author mine_book000
	 */
	public static void SendMessage(CommandSender sender, Command cmd, String text) {
		sender.sendMessage("[" + cmd.getName().toUpperCase() +"] " + ChatColor.LIGHT_PURPLE + text);
	}
	/**
	 * 指定したプレイヤーのPermissionsExにおけるグループに入っているかどうか確認する
	 * @param player 取得するプレイヤー
	 * @param group PermissionsExのグループ名
	 * @return 入っていたらtrue、入っていなかったらfalse
	 * @author mine_book000
	 */
	public static boolean CheckQroup(Player player, String group) {
		if(PermissionsEx.getUser(player).inGroup(group)){
			return true;
		}
		return false;
	}
}
