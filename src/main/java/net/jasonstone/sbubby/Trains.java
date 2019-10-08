package net.jasonstone.sbubby;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.yaml.snakeyaml.Yaml;

public final class Trains extends JavaPlugin {
	public ScoreboardManager boardManager;
	public Scoreboard board;
	public Logger logger;

	@Override
	public void onEnable() {
		logger = getLogger();
		PluginCommand ticketCommand = getCommand("ticket");
		TicketCommand ticketExecutor = new TicketCommand(this);
		ticketCommand.setExecutor(ticketExecutor);
		ticketCommand.setTabCompleter(ticketExecutor);

		PluginCommand railCommand = getCommand("rail");
		RailCommand railExecutor = new RailCommand(this);
		railCommand.setExecutor(railExecutor);
		railCommand.setTabCompleter(railExecutor);

		boardManager = Bukkit.getScoreboardManager();
		board = boardManager.getMainScoreboard();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Yaml yaml = new Yaml();
				try {
					@SuppressWarnings("unchecked")
					Map<String, Map<String, Integer>> switchMap = (Map<String, Map<String, Integer>>) yaml
							.load(new FileReader(getDataFolder() + File.separator + "switches.yml"));

					Set<String> switches = switchMap.keySet();

					for (String switchName : switches) {
						try {
							if (board.getObjective(switchName) == null) {
								board.registerNewObjective(switchName, "dummy", switchName);
							}
							Switch sw = new Switch(switchName, switchMap.get(switchName).get("x"),
									switchMap.get(switchName).get("y"), switchMap.get(switchName).get("z"));

							Player player = sw.getClosestPlayer();

							if (player == null) {
								return;
							}

							int score = player.getScoreboard().getObjective(switchName)
									.getScore(player.getPlayerListName()).getScore();

//							logger.log(Level.INFO, player.toString());
//							logger.log(Level.INFO, sw.toString());

							sw.reset();
							sw.setDirection(score);

						} catch (IllegalArgumentException e) {
							logger.log(Level.WARNING, e.getMessage());
						}
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}, 0, 10);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Yaml yaml = new Yaml();
				try {
					@SuppressWarnings("unchecked")
					Map<String, Map<String, Map<String, Integer>>> stationMap = (Map<String, Map<String, Map<String, Integer>>>) yaml
							.load(new FileReader(getDataFolder() + File.separator + "sbubby.yml"));

					Set<String> stations = stationMap.keySet();

					for (String stationName : stations) {

						Station station = new Station(stationName, stationMap.get(stationName).get("pos").get("x"),
								stationMap.get(stationName).get("pos").get("y"),
								stationMap.get(stationName).get("pos").get("z"));

						Player player = station.getClosestPlayer();

						if (player == null) {
							return;
						}

						boolean power = !player.getScoreboardTags().contains(stationName);

//						logger.log(Level.INFO, player.toString());
//						logger.log(Level.INFO, station.toString());

						station.SetPowered(power);

					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}, 0, 10);
	}
}
