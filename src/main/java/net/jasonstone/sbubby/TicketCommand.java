package net.jasonstone.sbubby;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.ImmutableList;

public class TicketCommand implements CommandExecutor, TabCompleter {
	private final Trains plugin;

	public TicketCommand(Trains t) {
		plugin = t;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}

		if (args.length != 1) {
			return false;
		}

		Player player = (Player) sender;
		Yaml yaml = new Yaml();
		try {
			@SuppressWarnings("unchecked")

			Map<String, Map<String, Map<String, Integer>>> map = (Map<String, Map<String, Map<String, Integer>>>) yaml
					.load(new FileReader(plugin.getDataFolder() + File.separator + "sbubby.yml"));

			if (!map.containsKey(args[0])) {
				return false;
			}

			Map<String, Integer> switches = map.get(args[0]).get("switches");

			ArrayList<String> currentTags = new ArrayList<String>(player.getScoreboardTags());
			for (String tag : currentTags) {
				player.removeScoreboardTag(tag);
			}

			player.addScoreboardTag(args[0]);

			for (String sw : switches.keySet()) {
				player.getScoreboard().getObjective(sw).getScore(player.getPlayerListName()).setScore(switches.get(sw));
			}

			player.sendMessage("Set destination to " + args[0] + "!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length != 1) {
			return ImmutableList.of();
		}
		Yaml yaml = new Yaml();

		try {
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Map<String, Integer>>> map = (Map<String, Map<String, Map<String, Integer>>>) yaml
					.load(new FileReader(plugin.getDataFolder() + File.separator + "sbubby.yml"));
			ArrayList<String> commands = new ArrayList<String>();

			for (String key : map.keySet()) {
				commands.add(key);
			}

			return StringUtil.copyPartialMatches(args[0], commands, new ArrayList<String>(commands.size()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ImmutableList.of();
		}
	}
}
