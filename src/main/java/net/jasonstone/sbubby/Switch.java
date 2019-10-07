package net.jasonstone.sbubby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rail.Shape;
import org.bukkit.entity.Player;

public class Switch {
	public Location center;
	public String name;

	public Location north;
	public Location south;
	public Location east;
	public Location west;

	public Block northBlock;
	public Block southBlock;
	public Block eastBlock;
	public Block westBlock;

	public Rail northRail;
	public Rail southRail;
	public Rail eastRail;
	public Rail westRail;

	public World world;

	public Switch(String name, int x, int y, int z) {
		world = Bukkit.getWorld("world_nether");
		center = new Location(world, x, y, z);
		this.name = name;

		north = new Location(world, x + 1, y, z - 1);
		south = new Location(world, x - 1, y, z + 1);
		east = new Location(world, x + 1, y, z + 1);
		west = new Location(world, x - 1, y, z - 1);

		northBlock = north.getBlock();
		southBlock = south.getBlock();
		eastBlock = east.getBlock();
		westBlock = west.getBlock();
	}

	private void _getData() throws ClassCastException {
		northRail = (Rail) northBlock.getBlockData();
		southRail = (Rail) southBlock.getBlockData();
		eastRail = (Rail) eastBlock.getBlockData();
		westRail = (Rail) westBlock.getBlockData();
	}

	private void _update() {
		northBlock.setBlockData(northRail);
		southBlock.setBlockData(southRail);
		eastBlock.setBlockData(eastRail);
		westBlock.setBlockData(westRail);
	}

	public String toString() {
		return String.format("Switch \"%s\" at %f,%f,%f", name, center.getX(), center.getY(), center.getZ());
	}

	public void reset() {
		try {
			_getData();
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		northRail.setShape(Shape.SOUTH_WEST);
		southRail.setShape(Shape.NORTH_EAST);
		eastRail.setShape(Shape.NORTH_WEST);
		westRail.setShape(Shape.SOUTH_EAST);

		_update();
	}

	public void setDirection(int score) {
		try {
			_getData();
		} catch (ClassCastException e) {
			e.printStackTrace();
			return;
		}

		switch (score) {
		case 1:
			// North +1, -1, SW
			northRail.setShape(Shape.NORTH_EAST);
			break;
		case 2:
			// East +1, +1, NW
			eastRail.setShape(Shape.SOUTH_EAST);
			break;
		case 3:
			// South -1, +1, NE
			southRail.setShape(Shape.SOUTH_WEST);
			break;
		case 4:
			// West -1, -1, SE
			westRail.setShape(Shape.NORTH_WEST);
			break;
		default:
			break;
		}

		_update();
	}

	public Player getClosestPlayer() {
		double closest = 100000.0;
		Player closestPlayer = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			Location playerLocation = p.getLocation();

			if (playerLocation.distance(center) < closest) {
				closestPlayer = p;
				closest = playerLocation.distance(center);
			}
		}

		return closestPlayer;

	}
}
