package eu32k.spaceDingus.core.system.moving;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;

import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.component.weapon.TargetPositionComponent;

public class AimingSystem extends EntityProcessingSystem {

   private ComponentMapper<TargetPositionComponent> tpm;
   private ComponentMapper<TransformComponent> tm;

   @SuppressWarnings("unchecked")
   public AimingSystem() {
      super(Aspect.getAspectForAll(TargetPositionComponent.class));
   }

   @Override
   protected void initialize() {
      tpm = world.getMapper(TargetPositionComponent.class);
      tm = world.getMapper(TransformComponent.class);
   }

   @Override
   protected void process(Entity e) {
      TargetPositionComponent targetPositionComponent = tpm.get(e);

      ImmutableBag<Entity> enemies = world.getManager(GroupManager.class).getEntities("ENEMY");

      targetPositionComponent.enabled = false;

      for (Entity enemy : enemies) {
         if (tm.has(enemy)) {
            targetPositionComponent.x = tm.get(enemy).x;
            targetPositionComponent.y = tm.get(enemy).y;
            targetPositionComponent.enabled = true;
         }
         return;
      }
   }
}
