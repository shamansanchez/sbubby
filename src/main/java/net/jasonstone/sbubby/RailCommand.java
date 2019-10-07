package net.jasonstone.sbubby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rail;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class RailCommand implements CommandExecutor, TabCompleter {
	private final Trains plugin;
	private final Map<String, Rail.Shape> shapeMap;

	public RailCommand(Trains t) {
		plugin = t;
		shapeMap = new HashMap<String, Rail.Shape>();
		shapeMap.put("ns", Rail.Shape.NORTH_SOUTH);
		shapeMap.put("ew", Rail.Shape.EAST_WEST);
		shapeMap.put("ne", Rail.Shape.NORTH_EAST);
		shapeMap.put("nw", Rail.Shape.NORTH_WEST);
		shapeMap.put("se", Rail.Shape.SOUTH_EAST);
		shapeMap.put("sw", Rail.Shape.SOUTH_WEST);

		shapeMap.put("an", Rail.Shape.ASCENDING_NORTH);
		shapeMap.put("as", Rail.Shape.ASCENDING_SOUTH);
		shapeMap.put("ae", Rail.Shape.ASCENDING_EAST);
		shapeMap.put("aw", Rail.Shape.ASCENDING_WEST);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}

		if (args.length != 1) {
			return false;
		}

		Player player = (Player) sender;

		Block block = player.getTargetBlockExact(10);
		if (block == null) {
			return false;
		}

		BlockData data = block.getBlockData();

		if (!(data instanceof Rail)) {
			return false;
		}

		if (!shapeMap.containsKey(args[0])) {
			return false;
		}

		((Rail) data).setShape(shapeMap.get(args[0]));
		block.setBlockData(data);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length != 1) {
			return ImmutableList.of();
		}

		return StringUtil.copyPartialMatches(args[0], shapeMap.keySet(), new ArrayList<String>(shapeMap.size()));
	}
}
