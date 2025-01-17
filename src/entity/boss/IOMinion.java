package entity.boss;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.imageio.ImageIO;

import display.World;
import entity.Entity;
import entity.Player;

public class IOMinion extends Entity {

	private static final long serialVersionUID = 717420573254936694L;
	private static final double SPEED = 0.2;
	private static Image projectileImage;

	static {
		try {
			projectileImage = ImageIO.read(IOMinion.class.getResource(
					"/assets/ioexception/minion.png"));
		} catch (Exception e) {
			System.err.println("Error reading IOMinion files!");
			System.exit(1);
		}
	}

	public IOMinion(double x, double y) {
		this.x = x;
		this.y = y;
		this.setHitbox(projectileImage);
	}

	@Override
	public void tick() {
		Player p = World.getThePlayer(); 
		if (p.getX() > this.x)
			this.x += SPEED;
		else if (p.getX() < this.x)
			this.x -= SPEED;
		if (p.getY() > this.x)
			this.y += SPEED;
		else if (p.getY() < this.y)
			this.y -= SPEED;
	}

	@Override
	public void collide(Entity e) {
		if (e instanceof Player) {
			e.damage(2, this);
			World.deregisterEntity(this);
		}
	}

	@Override
	public void damage(int amount, Entity damageSource) {
		World.deregisterEntity(this);
	}

	@Override
	public void draw(Graphics2D g) {
		int[] sCoords = World.getScreenCoordinates(x, y);
		g.drawImage(projectileImage, sCoords[0] - spriteWidth / 2, sCoords[1]
				- spriteHeight / 2, null);
	}

}
