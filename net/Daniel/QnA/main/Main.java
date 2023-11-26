
package net.Daniel.QnA.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.command.Command;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;


public class Main extends JavaPlugin {
	public final Logger logger;
	public static Command command;
	public static Essentials essentials;

	protected int QCooltime = 60;
	protected int ACooltime = 40;
	protected int AnswerCooltimeSpecial = 5;
	public static Main plugin;


	public Main() {
		this.logger = Logger.getLogger("Minecraft");
		plugin = this;
	}

	public void onDisable() {
		final PluginDescriptionFile pdFile = this.getDescription();
		System.out.println(String.valueOf(pdFile.getName()) + " " + pdFile.getVersion() + "이(가) 비활성화 되었습니다.");
	}

	public void onEnable() {
		final PluginDescriptionFile pdFile = this.getDescription();
		if (!new File("plugins/" + pdFile.getName() + "/config.yml").exists()) {
			saveDefaultConfig();
		}
		this.QCooltime = getConfig().getInt("QuestionCooltime");
		this.ACooltime = getConfig().getInt("AnswerCooltime");
		this.AnswerCooltimeSpecial = getConfig().getInt("AnswerCooltimeSpecial");

		Plugin essentialsPlugin = Bukkit.getPluginManager().getPlugin("Essentials");

		Main.essentials = (Essentials) essentialsPlugin;

		System.out.println(String.valueOf(pdFile.getName()) + " " + pdFile.getVersion() + "이(가) 활성화 되었습니다.");
	}

	public void broadcast(String msg, Player sender) {

		Set<Player> outList = new HashSet<>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			final User onlineUser = essentials.getUser(player);
			final User send = essentials.getUser(sender);

			if (!(onlineUser.isIgnoredPlayer(send))) {
				outList.add(player);
			}
		}

		for (Player onlinePlayer : outList) {
			onlinePlayer.sendMessage(" ");
			onlinePlayer.sendMessage(msg);
			onlinePlayer.sendMessage(" ");
	
		}
		System.out.println(ChatColor.stripColor(msg));
	}

	private HashSet<Player> coolList = new HashSet<>();
	private HashSet<Player> coolList2 = new HashSet<>();

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel,
			final String[] args) {
		if (commandLabel.equalsIgnoreCase("답변")) {

			if (sender instanceof Player) {
				// Player player = (Player) sender;

				if (sender.hasPermission("User.Answercmd")) {
					if (sender.hasPermission("op.answer")) {
						if (args.length > 0) {
							StringBuilder str = new StringBuilder();
							for (int i = 0; i < args.length; i++) {
								str.append(args[i] + " ");
							}
							String msg = str.toString();

							msg = "§f§l[§a§l답변§f§l] §a§l" + sender.getName() + "님의 답변: " + msg;

							broadcast(msg, (Player) sender);

							return true;

						} else {
							sender.sendMessage("§f§l[  §6§l서버 §f§l] §a§l사용법 : /답변 <내용>");
							sender.sendMessage("§f§l[§a§l 답변§f§l] §b§l답변으로 도배할 경우 기존경고의 2배를 부여합니다.");
							return true;
						}

					} else {
						if (sender.hasPermission("MineQnA.A.Cooltime")) {

							if (args.length > 0) {
								if (!coolList.contains((Player) sender)) {
									coolList.add((Player) sender);
									StringBuilder str = new StringBuilder();
									for (int i = 0; i < args.length; i++) {
										str.append(args[i] + " ");
									}
									String msg = str.toString();

									msg = "§f§l[ §a§l답변§f§l] §a§l" + sender.getName() + "님의 답변: " + msg;

									broadcast(msg, (Player) sender);
									sender.sendMessage("§f§l[§a§l 답변§f§l] §b§l답변으로 도배할 경우 기존경고의 2배를 부여합니다.");

									Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
										@Override
										// 유저 명령어
										public void run() {
											coolList.remove((Player) sender);

										}
									}, AnswerCooltimeSpecial * 20L); // 쿨타임 권한
								} else {
									sender.sendMessage("§f§l[ §e§l답변 §f§l] §f§l" + AnswerCooltimeSpecial
											+ "§a§l초에 한번씩 사용이 가능합니다.");
								}

							} else {
								sender.sendMessage("§f§l[  §6§l서버 §f§l] §a§l사용법 : /답변 <내용>");
								sender.sendMessage("§f§l[§a§l 답변§f§l] §b§l답변으로 도배할 경우 기존경고의 2배를 부여합니다.");
								return true;
							}

						}

						else if (args.length > 0) {
							if (!coolList.contains((Player) sender)) {
								coolList.add((Player) sender);
								StringBuilder str = new StringBuilder();
								for (int i = 0; i < args.length; i++) {
									str.append(args[i] + " ");
								}
								String msg = str.toString();

								msg = "§f§l[ §a§l답변§f§l] §a§l" + sender.getName() + "님의 답변: " + msg;

								broadcast(msg, (Player) sender);
								sender.sendMessage("§f§l[§a§l 답변§f§l] §b§l답변으로 도배할 경우 기존경고의 2배를 부여합니다.");

								Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									// 유저 명령어
									public void run() {
										coolList.remove((Player) sender);

									}
								}, ACooltime * 20L); 
							} else {
								sender.sendMessage("§f§l[ §e§l답변 §f§l] §f§l" + ACooltime + "§a§l초에 한번씩 사용이 가능합니다.");
							}

						} else {
							sender.sendMessage("§f§l[  §6§l서버 §f§l] §a§l사용법 : /답변 <내용>");
							sender.sendMessage("§f§l[§a§l 답변§f§l] §b§l답변으로 도배할 경우 기존경고의 2배를 부여합니다.");
							return true;
						}

					}
				} else {
					sender.sendMessage("§f§l[  §6§l서버 §f§l] §c§l권한이 없습니다. 튜토리얼을 완료하신 후 사용이 가능합니다.");
					return true;
				}

			} else {
				System.out.println("[Kite 서버] 답변 명령어는 게임 내 플레이어만 사용이 가능한 명령어 입니다.");
				return true;
			}
		} else {
			if (commandLabel.equalsIgnoreCase("질문")) {
				if (sender instanceof Player) {
					if (sender.hasPermission("op.question")) {

						if (args.length > 0) {
							StringBuilder str = new StringBuilder();
							for (int i = 0; i < args.length; i++) {
								str.append(args[i] + " ");
							}
							String msg = str.toString();

							msg = "§f§l[ §a§l질문§f§l] §e§l" + sender.getName() + "님의 질문: " + msg;
							broadcast(msg, (Player) sender);

						} else {
							sender.sendMessage("§f§l[  §6§l서버 §f§l] §a§l사용법 :  /질문 <내용>");
							sender.sendMessage("§f§l[§a§l 질문§f§l] §b§l질문으로 도배할 경우 기존경고의 2 배를 부여합니다. ");
							sender.sendMessage("§f§l[§a§l 질문§f§l] §b§l질문하기전에 튜토리얼에 나와있는지 확인바랍니다.");
							sender.sendMessage(
									"§f§l[§a§l 질문§f§l] §a§l질문에 대한 답변이 없다면, 서버 카페의 §eKITE 질문 게시판§a에 질문 내용을 올려주세요. §6카페 주소: §ehttp://cafe.naver.com/ju0625");
						}
					} else {
						if (args.length > 0) { // 쿨타임 적용
							if (!coolList2.contains((Player) sender)) {
								coolList2.add((Player) sender);
								StringBuilder str = new StringBuilder();
								for (int i = 0; i < args.length; i++) {
									str.append(args[i] + " ");
								}
								String msg = str.toString();

								msg = "§f§l[ §a§l질문§f§l] §e§l" + sender.getName() + "님의 질문: " + msg;

								broadcast(msg, (Player) sender);

								sender.sendMessage("§f§l[§a§l 질문§f§l] §b§l질문으로 도배할 경우 기존경고의 2 배를 부여합니다. ");
								sender.sendMessage("§f§l[§a§l 질문§f§l] §b§l질문하기전에 튜토리얼에 나와있는지 확인바랍니다.");
								sender.sendMessage(
										"§f§l[§a§l 질문§f§l] §a§l질문에 대한 답변이 없다면, 서버 카페의 §eKITE 질문 게시판§a에 질문 내용을 올려주세요. §6카페 주소: §ehttp://cafe.naver.com/ju0625");
								Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										coolList2.remove((Player) sender);
									}
								}, QCooltime * 20L); // 쿨타임
							} else {
								sender.sendMessage("§f§l[ §e§l질문 §f§l] §f§l" + QCooltime + "§a§l초에 한번씩 사용이 가능합니다.");
								return false;
							}

						} else {
							sender.sendMessage("§f§l[  §6§l서버 §f§l] §a§l사용법 :  /질문 <내용>");
							sender.sendMessage("§f§l[§a§l 질문§f§l] §b§l질문으로 도배할 경우 기존경고의 2 배를 부여합니다. ");
							sender.sendMessage("§f§l[§a§l 질문§f§l] §b§l질문하기전에 튜토리얼에 나와있는지 확인바랍니다.");
							sender.sendMessage(
									"§f§l[§a§l 질문§f§l] §a§l질문에 대한 답변이 없다면, 서버 카페의 §eKITE 질문 게시판§a에 질문 내용을 올려주세요. §6카페 주소: §ehttp://cafe.naver.com/ju0625");
							return true;
						}

					}

				} else {
					sender.sendMessage("[서버] 질문/답변 명령어는 게임 내 플레이어만 사용이 가능한 명령어 입니다.");
					return false;
				}
			} else {
				if (commandLabel.equalsIgnoreCase("MineQnA")
						&& (sender.hasPermission("MineQnA.Admin.Reload") || sender.isOp())) {

					this.reloadConfig();
					final PluginDescriptionFile pdFile = this.getDescription();
					if (!new File("plugins/" + pdFile.getName() + "/config.yml").exists()) {
						saveDefaultConfig();
					}
					this.QCooltime = getConfig().getInt("QuestionCooltime");
					this.ACooltime = getConfig().getInt("AnswerCooltime");
					this.AnswerCooltimeSpecial = getConfig().getInt("AnswerCooltimeSpecial");
					this.saveConfig();

					sender.sendMessage("[서버] 질문/답변 설정이 리로드 되었습니다.");

					return true;

				}
			}
		}

		return false;
	}

}
