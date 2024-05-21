package entity;

public abstract class EntityMinion extends Entity {
	private static final long serialVersionUID = -2048816544234580612L;

	public abstract void attack(Entity target);

	public EntityMinion() {
		super();
	}
}