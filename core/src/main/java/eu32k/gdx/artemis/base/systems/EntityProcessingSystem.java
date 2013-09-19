package eu32k.gdx.artemis.base.systems;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.EntitySystem;
import eu32k.gdx.artemis.base.utils.Bag;
import eu32k.gdx.artemis.base.utils.ImmutableBag;

/**
 * A typical entity system. Use this when you need to process entities possessing the
 * provided component types.
 * 
 * @author Arni Arent
 *
 */
public abstract class EntityProcessingSystem extends EntitySystem {
	
	public EntityProcessingSystem(Aspect aspect) {
		super(aspect);
	}

	/**
	 * Process a entity this system is interested in.
	 * @param e the entity to process.
	 */
	protected abstract void process(Entity e);

	@Override
	protected final void processEntities(ImmutableBag<Entity> entities) {
			Object[] array = ((Bag<Entity>)entities).getData();
			for (int i = 0, s = entities.size(); s > i; i++) {
				process((Entity)array[i]);
			}
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}
	
}
