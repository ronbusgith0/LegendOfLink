package entity;

public abstract class EntityItem extends Entity {
	private static final long serialVersionUID = 1142312141172520401L;

	public EntityItem() {super();}

	@Override
	public void damage(int amount, Entity e) {}
}