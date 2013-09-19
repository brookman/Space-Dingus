package eu32k.gdx.artemis.base;

/**
 * Manager.
 * 
 * @author Arni Arent
 * 
 */
public abstract class Manager implements EntityObserver {
   protected World world;

   protected abstract void initialize();

   protected void setWorld(World world) {
      this.world = world;
   }

   protected World getWorld() {
      return world;
   }

   @Override
   public void added(Entity e) {
      // NOP
   }

   @Override
   public void changed(Entity e) {
      // NOP
   }

   @Override
   public void deleted(Entity e) {
      // NOP
   }

   @Override
   public void disabled(Entity e) {
      // NOP
   }

   @Override
   public void enabled(Entity e) {
      // NOP
   }
}
