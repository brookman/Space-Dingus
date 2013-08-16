package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.RemoveMeComponent;

public class DeathSystem extends EntityProcessingSystem {

   private ComponentMapper<HealthComponent> hm;

   @SuppressWarnings("unchecked")
   public DeathSystem() {
      super(Aspect.getAspectForAll(HealthComponent.class));
   }

   @Override
   protected void initialize() {
      hm = world.getMapper(HealthComponent.class);
   }

   @Override
   protected void process(Entity e) {
      if (hm.get(e).health <= 0) {
         e.addComponent(Pools.get(RemoveMeComponent.class).obtain());
         world.changedEntity(e);
      }
   }
}
