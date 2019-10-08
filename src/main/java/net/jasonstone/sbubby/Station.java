package net.jasonstone.sbubby;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

public class Station {
	public Location center;
	public String name;

	public List<Location> levers;
	public World world;

	public Station(String name, int x, int y, int z) {
		world = Bukkit.getWorld("world_nether");
		center = new Location(world, x, y, z);
		this.name = name;

		levers = new ArrayList<Location>();
		levers.add(new Location(world, x, y, z - 1));
		levers.add(new Location(world, x, y, z + 1));
		levers.add(new Location(world, x + 1, y, z));
		levers.add(new Location(world, x - 1, y, z));
	}

	public void SetPowered(boolean power) {
		for (Location l : levers) {
			Block block = l.getBlock();
			if (block.getType() != Material.LEVER) {
				continue;
			}

			BlockState state = block.getState();
			Lever lever = (Lever) state.getData();

			lever.setPowered(power);
			state.setData(lever);
			state.update(true);
		}
	}

	public Player getClosestPlayer() {
		double closest = 100000.0;
		Player closestPlayer = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			Location playerLocation = p.getLocation();

			if (playerLocation.getWorld() != world) {
				continue;
			}

			if (playerLocation.distance(center) < closest) {
				closestPlayer = p;
				closest = playerLocation.distance(center);
			}
		}

		return closestPlayer;

	}
}
