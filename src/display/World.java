package display;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import entity.Entity;
import entity.EntityMolblin;
import entity.EntityOctorokRed;
import entity.EntitySpider;
import entity.Player;

public final class World {

	public static final int WORLD_HEIGHT = 10;
	public static final int WORLD_WIDTH = 16;

	private static LinkPanel panel;
	private static int gameState = 0;
	
	private static Set<Entity> entities = new HashSet<Entity>();
	private static WorldTile[][] tiles = new WorldTile[WORLD_WIDTH][WORLD_HEIGHT];
	
	private static Player thePlayer;
	private static Timer ticker;
	
	private static String worldName;
	
	private static String northWorld;
	private static String southWorld;
	private static String eastWorld;
	private static String westWorld;

	static {
		thePlayer = new Player();
	}

	public static LinkPanel getPanel() {
		return panel;
	}

	public static void setPanel(LinkPanel panel) {
		World.panel = panel;
	}

	public static void registerEntity(Entity e) {
		entities.add(e);
	}

	public static int[] getScreenCoordinates(double x, double y) {
		return new int[] { (int) (Math.round(x * 32)),
				(int) (Math.round(y * 32 + 112)) };
	}

	public static void setTile(int x, int y, WorldTile tile) {
		tiles[x][y] = tile;
	}

	public static WorldTile getTileAt(int x, int y) {
		if (x >= 0 && y >= 0 && x < tiles.length && y < tiles[0].length)
			if (tiles[x][y] != null)
				return tiles[x][y];
		return WorldTile.ground;
	}

	public static WorldTile getTileAt(double x, double y) {
		int nx = (int) Math.floor(x);
		int ny = (int) Math.floor(y);
		return getTileAt(nx, ny);
	}

	public static void deregisterEntity(Entity e) {
		entities.remove(e);
	}
	
	public static Set<Entity> getAllEntities() {
		return new HashSet<Entity>(entities);
	}

	public static int getGameState() {
		return gameState;
	}

	public static void setGameState(int gameState) {
		if (gameState == 2) { // Death
			MusicPlayer.changePlayingMusic(null);
			MusicPlayer.playSoundEffect("/assets/music/Die.wav");
			World.stopTicker();
			JOptionPane.showMessageDialog(null, "You Lose!", "LINK",
					JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		} else if (gameState == 1) {
			World.loadWorld("8H.wld");
			World.startTicker(panel, panel.kb);
			panel.hideStartButton();
		} else if (gameState == 0) {
			MusicPlayer.changePlayingMusic("/assets/music/TitleScreen.wav");
			panel.showStartButton();
		} else if (gameState == 3) {
			JOptionPane.showMessageDialog(null, "You WIN!!!", "LINK",
					JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		World.gameState = gameState;
	}

	public static Player getThePlayer() {
		return thePlayer;
	}
	
	public static String getWorldName() {
		return worldName;
	}

	public static void loadWorld(Direction direction) {
		switch (direction) {
		case NORTH:
			loadWorld(northWorld);
			break;
		case SOUTH:
			loadWorld(southWorld);
			break;
		case EAST:
			loadWorld(eastWorld);
			break;
		case WEST:
			loadWorld(westWorld);
			break;
		default:
			System.err.println("Invalid world direction " + direction);
			System.exit(1);
		}
	}

	public static void loadWorld(String filename) {
		System.out.println("Loading world " + filename);
		SaveLoader.onWorldLeave();
		entities = new HashSet<Entity>();
		entities.add(thePlayer);
		worldName = filename;
		Scanner theScanner = null;
		try {
			theScanner = new Scanner(new BufferedReader(new InputStreamReader(
					World.class.getResource("/assets/worlds/" + filename)
							.openStream())));
		} catch (IOException e) {
			System.err.println("Error reading file assets/worlds/" + filename);
			System.exit(1);
		}
		String tileFileName = theScanner.nextLine().replace("tiles ", "");
		loadTiles(tileFileName);
		String musicName = theScanner.nextLine().replace("music ", "");
		MusicPlayer.changePlayingMusic("/assets/music/" + musicName);
		northWorld = theScanner.nextLine().replace("north ", "");
		southWorld = theScanner.nextLine().replace("south ", "");
		eastWorld = theScanner.nextLine().replace("east ", "");
		westWorld = theScanner.nextLine().replace("west ", "");
		if (!SaveLoader.hasVisited(worldName)) {
			try {
				String line = "";
				do {
					line = theScanner.nextLine();
				} while (!line.equals("entities:"));
				while (theScanner.hasNext()) {
					createEntity(theScanner.nextLine());
				}
			} catch (NoSuchElementException e) {
			}
		} else {
			SaveLoader.loadEntities(worldName);
		}
	}

	private static void createEntity(String entityLine) {
		String[] elements = entityLine.split(" ");
		elements[0] = elements[0].toLowerCase();
		double[] coords = randomEmptyCoordinates();
		switch (elements[0]) {
		case "octorokred":
			new EntityOctorokRed(coords[0], coords[1]);
			break;
		case "spider":
			new EntitySpider(coords[0], coords[1]);
			break;
		case "molblin":
			new EntityMolblin(coords[0], coords[1]);
			break;
		case "nullpointerexception":
			new entity.boss.NullPointerException();
			break;
		case "classcastexception":
			new entity.boss.ClassCastException();
			break;
		case "ioexception":
			new entity.boss.IOException();
			break;
		default:
			System.err.println("WARNING: Unknown entity type \"" + elements[0]
					+ "\"!");
		}
	}

	private static double[] randomEmptyCoordinates() {
		double x, y;
		do {
			x = Math.random() * WORLD_WIDTH;
			y = 1 + (Math.random() * (WORLD_HEIGHT - 2));
		} while (World.getTileAt(x, y).isSolid());
		return new double[] { x, y };
	}

	private static void loadTiles(String filename) {
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new InputStreamReader(
					World.class.getResource("/assets/worlds/tiles/" + filename)
							.openStream())));
		} catch (IOException e) {
		}
		for (int y = 0; y < WORLD_HEIGHT; y++) {
			String[] nums = s.nextLine().split("\\s+");
			for (int x = 0; x < WORLD_WIDTH; x++) {
				tiles[x][y] = WorldTile.getTileFromCode(Integer.parseInt(
						nums[x], 16));
			}
		}
	}

	public static void renderTiles(Graphics g) {
		for (int x = 0; x < WORLD_WIDTH; x++) {
			for (int y = 0; y < WORLD_HEIGHT; y++) {
				World.getTileAt(x, y).draw(x, y, g);
			}
		}
	}

	private static void startTicker(JPanel parent, KeyboardHandler kb) {
		ticker = new Timer(33, new Tick(parent, kb));
		ticker.start();
	}

	public static void stopTicker() {
		ticker.stop();
	}

	@SuppressWarnings("incomplete-switch")
	public static boolean willCollideTile(Entity e, double movement) {
		double[] newCoords = e.getFacing().moveInDirection(e.getX(), e.getY(),
				movement);
		double newX = newCoords[0];
		double newY = newCoords[1];
		switch (e.getFacing()) {
		case SOUTH:
			newY += e.getHeight() / 2;
			break;
		case EAST:
			newX += e.getWidth() / 2;
			break;
		case WEST:
			newX -= e.getWidth() / 2;
			break;
		}
		if (!(e instanceof Player))
			if (newX < 0 || newX >= WORLD_WIDTH || newY < 0
					|| newY >= WORLD_HEIGHT)
				return true;
		return World.getTileAt(newX, newY).isSolid();
	}

	private World() {}
}
