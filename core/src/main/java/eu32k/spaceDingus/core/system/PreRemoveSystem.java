package eu32k.spaceDingus.core.system;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.component.RemoveMeComponent;
import eu32k.spaceDingus.core.component.weapon.RocketComponent;
import eu32k.spaceDingus.core.factory.MiscFactory;

public class PreRemoveSystem extends EntityProcessingSystem {

   private ComponentMapper<RocketComponent> rm;

   private MiscFactory factory;

   @SuppressWarnings("unchecked")
   public PreRemoveSystem(MiscFactory factory) {
      super(Aspect.getAspectForAll(RemoveMeComponent.class));
      this.factory = factory;
   }

   @Override
   protected void initialize() {
      rm = world.getMapper(RocketComponent.class);
   }

   @Override
   protected void process(Entity e) {
      if (rm.has(e)) {
         factory.createExplosion(e);
      }
   }
}
