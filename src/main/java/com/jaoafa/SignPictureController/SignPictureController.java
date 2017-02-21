package com.jaoafa.SignPictureController;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.SignPictureController.Command.SPC;
import com.jaoafa.SignPictureController.Event.OnSignChangeEvent;


public class SignPictureController extends JavaPlugin {

	private static JavaPlugin instance;
	public static FileConfiguration conf;
	public static Connection c = null;
	public static String sqluser;
	public static String sqlpassword;
	/**
	 * プラグインが起動したときに呼び出し
	 * @author mine_book000
	 * @since 2017/02/20
	 */
	@Override
	public void onEnable() {
		getLogger().info("--------------------------------------------------");
		// クレジット
		getLogger().info("(c) jao Minecraft Server");
		getLogger().info("Product by tomachi.");

		//連携プラグインの確認
		Load_Plugin("PermissionsEx");
		Load_Plugin("WorldEdit");

		//初期設定
		FirstSetting();

		//コンフィグ読み込み
		Load_Config();

		//CommandExecutor
		getCommand("spc").setExecutor(new SPC(this));

		//EventHandler
		getServer().getPluginManager().registerEvents(new OnSignChangeEvent(this), this);
	}
	/**
	 * 初期設定
	 * @author mine_book000
	 */
	private void FirstSetting(){
		instance = this;
	}
	/**
	 * 連携プラグイン確認
	 * @author mine_book000
	 */
	private void Load_Plugin(String PluginName){
		if(getServer().getPluginManager().isPluginEnabled(PluginName)){
			getLogger().info("SPC Success(LOADED: " + PluginName + ")");
			getLogger().info("Using " + PluginName);
		}else{
			getLogger().warning("SPC ERR(NOTLOADED: " + PluginName + ")");
			getLogger().info("Disable SPC...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
	/**
	 * JavaPluginを取得する
	 * @return JavaPlugin
	 * @author mine_book000
	 */
	public static JavaPlugin getJavaPlugin(){
		return instance;
	}
	/**
	 * コンフィグ読み込み
	 * @author mine_book000
	 */
	private void Load_Config(){
		conf = getConfig();

		if(conf.contains("sqluser") && conf.contains("sqlpassword")){
			sqluser = conf.getString("sqluser");
			sqlpassword = conf.getString("sqlpassword");
			MySQL_Enable(conf.getString("sqluser"), conf.getString("sqlpassword"));
		}else{
			getLogger().info("MySQL Connect err. [conf NotFound]");
			getLogger().info("Disable SPC...");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	/**
	 * MySQLの初期設定
	 * @author mine_book000
	 */
	private void MySQL_Enable(String user, String password){
		MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", user, password);

		try {
			c = MySQL.openConnection();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [ClassNotFoundException]");
			getLogger().info("Disable SPC...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [SQLException: " + e.getSQLState() + "]");
			getLogger().info("Disable SPC...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getLogger().info("MySQL Connect successful.");
	}
}
