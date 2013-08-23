package eu32k.spaceDingus.core.system.moving;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.weapon.TargetPositionComponent;

public class AimingSystem extends EntityProcessingSystem {

   private ComponentMapper<TargetPositionComponent> tpm;
   private ComponentMapper<ActorComponent> am;

   @SuppressWarnings("unchecked")
   public AimingSystem() {
      super(Aspect.getAspectForAll(TargetPositionComponent.class));
   }

   @Override
   protected void initialize() {
      tpm = world.getMapper(TargetPositionComponent.class);
      am = world.getMapper(ActorComponent.class);
   }

   @Override
   protected void process(Entity e) {
      TargetPositionComponent targetPositionComponent = tpm.get(e);

      ImmutableBag<Entity> enemies = world.getManager(GroupManager.class).getEntities("ENEMY");

      targetPositionComponent.enabled = false;

      for (Entity enemy : enemies) {
         if (am.has(enemy)) {
            Actor actor = am.get(enemy).actor;
            targetPositionComponent.x = actor.getX();
            targetPositionComponent.y = actor.getY();
            targetPositionComponent.enabled = true;
         }
         return;
      }
   }
}
