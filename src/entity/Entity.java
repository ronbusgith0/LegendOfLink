package entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.Serializable;

import display.Direction;
import display.World;

public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 9080843028633986388L;
	protected double x, y;
	protected int spriteWidth, spriteHeight;
	protected int health;
	protected Direction facing;

	public Entity() {
		World.registerEntity(this);
	}

	public abstract void tick();
	public abstract void collide(Entity e);
	public abstract void damage(int amount, Entity damageSource);
	
	protected void setHitbox(Image imageIn) {
		this.spriteHeight = imageIn.getHeight(null);
		this.spriteWidth = imageIn.getWidth(null);
	}

	public abstract void draw(Graphics2D g);

	
	public double getX() {return x;}
	public double getY() {return y;}
	public double getWidth() {return spriteWidth / 32D;}
	public double getHeight() {return spriteHeight / 32D;}
	public int getHealth() {return health;}
	public Direction getFacing() {return facing;}
}
