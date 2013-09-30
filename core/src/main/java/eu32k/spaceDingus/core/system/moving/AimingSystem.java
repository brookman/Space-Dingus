package eu32k.spaceDingus.core.system.moving;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.managers.GroupManager;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.base.utils.ImmutableBag;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.component.ActorComponent;
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

      EntityActor nearestEnemy = null;

      if (am.has(e)) {
         EntityActor thisActor = am.get(e).actor;

         float minDist = Float.MAX_VALUE;

         for (Entity enemy : enemies) {
            if (am.has(enemy)) {
               EntityActor enemyActor = am.get(enemy).actor;
               float dist = enemyActor.getPositionOnStage().dst(thisActor.getPositionOnStage());
               if (dist < minDist) {
                  nearestEnemy = enemyActor;
                  minDist = dist;
               }
            }
         }
      } else if (enemies.size() > 0) {
         nearestEnemy = am.get(enemies.get(0)).actor;
      }

      if (nearestEnemy != null) {
         targetPositionComponent.x = nearestEnemy.getPositionOnStage().x;
         targetPositionComponent.y = nearestEnemy.getPositionOnStage().y;
         targetPositionComponent.enabled = true;
      }
   }
}
