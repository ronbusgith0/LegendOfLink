package entity;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import display.Direction;
import display.World;
import display.MusicPlayer;

public class EntityMolblin extends EntityMinion {
	
	private static final long serialVersionUID = -8833066142798127669L;
	private static Image northImage1;
	private static Image southImage1;
	
	private static Image eastImage1;
	private static Image westImage1;
	
	private static Image northImage2;
	private static Image southImage2;
	
	private static Image eastImage2;
	private static Image westImage2;

	private static final double SPEED = 0.1;
	private static BufferedImage arrowImage;

	static {
		
		try {
			northImage1 = ImageIO.read(
					EntityMolblin.class
							.getResource("/assets/molblin/molblin_north1.png"))
					.getScaledInstance(28, 32, Image.SCALE_REPLICATE);
			northImage2 = ImageIO.read(
					EntityMolblin.class
							.getResource("/assets/molblin/molblin_north2.png"))
					.getScaledInstance(28, 32, Image.SCALE_REPLICATE);
			southImage1 = ImageIO.read(
					EntityMolblin.class
							.getResource("/assets/molblin/molblin_south1.png"))
					.getScaledInstance(32, 32, Image.SCALE_REPLICATE);
			southImage2 = ImageIO.read(
					EntityMolblin.class
							.getResource("/assets/molblin/molblin_south2.png"))
					.getScaledInstance(32, 32, Image.SCALE_REPLICATE);
			eastImage1 = ImageIO.read(
					EntityMolblin.class
							.getResource("/assets/molblin/molblin_east1.png"))
					.getScaledInstance(32, 32, Image.SCALE_REPLICATE);
			eastImage2 = ImageIO.read(
					EntityMolblin.class
							.getResource("/assets/molblin/molblin_east2.png"))
					.getScaledInstance(32, 30, Image.SCALE_REPLICATE);
			westImage1 = ImageIO.read(
					EntityMolblin.class
							.getResource("/assets/molblin/molblin_west1.png"))
					.getScaledInstance(32, 30, Image.SCALE_REPLICATE);
			westImage2 = ImageIO.read(
					EntityMolblin.class
							.getResource("/assets/molblin/molblin_west2.png"))
					.getScaledInstance(32, 32, Image.SCALE_REPLICATE);
			Image arrowImageTemp = ImageIO.read(
					EntityMolblin.class
							.getResource("/assets/molblin/arrow.png"))
					.getScaledInstance(10, 32, Image.SCALE_REPLICATE);
			arrowImage = new BufferedImage(arrowImageTemp.getWidth(null),
					arrowImageTemp.getHeight(null), BufferedImage.TYPE_INT_ARGB);

			Graphics2D bGr = arrowImage.createGraphics();
			bGr.drawImage(arrowImageTemp, 0, 0, null);
			bGr.dispose();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error reading Molblin image files");
			System.exit(1);
		}
	}

	public EntityMolblin(double x, double y) {
		this.x = x;
		this.y = y;
		this.health = 3; 
		this.facing = Direction.NORTH;
		this.setHitbox(northImage1);
	}

	@Override
	public void attack(Entity target) {}

	private void shoot() {
		BufferedImage rotated = arrowImage;
		switch (facing) {
		case WEST:
			rotated = rotate(arrowImage, 270);
			break;
		case SOUTH:
			rotated = rotate(rotated, 180);
			break;
		case EAST:
			rotated = rotate(rotated, 90);
			break;
		case NORTH:
			rotated = arrowImage;
			break;
		default:
			rotated = arrowImage;
			break;
		}
		new EntityProjectile(x, y, 1, facing, rotated);
		MusicPlayer.playSoundEffect("/assets/music/Arrow.wav");
	}

	public static BufferedImage rotate(BufferedImage image, double angleDegrees) {
		double angleRadians = Math.toRadians(angleDegrees);
		
		double sin = Math.abs(Math.sin(angleRadians)), cos = Math.abs(Math
				.cos(angleRadians));
		int w = image.getWidth(), h = image.getHeight();
		int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math
				.floor(h * cos + w * sin);
		GraphicsConfiguration gc = getDefaultConfiguration();
		BufferedImage result = gc.createCompatibleImage(neww, newh,
				Transparency.TRANSLUCENT);
		Graphics2D g = result.createGraphics();
		g.translate((neww - w) / 2, (newh - h) / 2);
		g.rotate(angleRadians, w / 2, h / 2);
		g.drawRenderedImage(image, null);
		g.dispose();
		
		return result;
	}

	private static GraphicsConfiguration getDefaultConfiguration() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		return gd.getDefaultConfiguration();
	}

	@Override
	public void tick() {
		
		if (Math.random() < 0.02) {
			shoot();
		}
	
		if (Math.random() < 0.01)
			randomTurn();
	
		if (World.willCollideTile(this, SPEED)) {
			randomTurn();
		} else { 
			double[] newCoords = facing.moveInDirection(x, y, SPEED);
			x = newCoords[0];
			y = newCoords[1];
		}
	}

	private void randomTurn() {
		if (Math.random() < 0.5)
			facing = facing.getLeft();
		else
			facing = facing.getRight();
	}

	@Override
	public void collide(Entity e) {
		if (e instanceof Player)
			e.damage(1, this);
	}

	@Override
	public void damage(int amount, Entity damageSource) {
		health -= amount;
		if (health <= 0) {
			World.deregisterEntity(this);
			MusicPlayer.playSoundEffect("/assets/music/Kill.wav");
			if (Math.random() < 0.9) {
				new EntityHeart(x, y, 2);
			}
		} else {
			MusicPlayer.playSoundEffect("/assets/music/Hit.wav");
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void draw(Graphics2D g) {
		int[] sCoords = World.getScreenCoordinates(x, y);
		sCoords[0] -= spriteWidth / 2;
		sCoords[1] -= spriteHeight / 2;
		boolean whichImage = (((int) (x * 2) % 2) == 1)
				^ (((int) (y * 2) % 2) == 1);
		Image imageToDraw = null;
		switch (facing) {
		case EAST:
			if (whichImage)
				imageToDraw = eastImage1;
			else
				imageToDraw = eastImage2;
			break;
		case NORTH:
			if (whichImage)
				imageToDraw = northImage1;
			else
				imageToDraw = northImage2;
			break;
		case SOUTH:
			if (whichImage)
				imageToDraw = southImage1;
			else
				imageToDraw = southImage2;
			break;
		case WEST:
			if (whichImage)
				imageToDraw = westImage1;
			else
				imageToDraw = westImage2;
			break;
		}
		g.drawImage(imageToDraw, sCoords[0], sCoords[1], null);
		setHitbox(imageToDraw);
	}
}
