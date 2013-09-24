package eu32k.spaceDingus.core.system.moving;

import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;

public class StearingSystem extends EntityProcessingSystem {

   private ComponentMapper<MovableComponent> mm;
   private ComponentMapper<EngineComponent> em;
   private ComponentMapper<ActorComponent> am;

   @SuppressWarnings("unchecked")
   public StearingSystem() {
      super(Aspect.getAspectForAll(MovableComponent.class, ActorComponent.class));
   }

   @Override
   protected void initialize() {
      mm = world.getMapper(MovableComponent.class);
      em = world.getMapper(EngineComponent.class);
      am = world.getMapper(ActorComponent.class);
   }

   @Override
   protected void process(Entity e) {
      MovableComponent movableComponent = mm.get(e);
      ActorComponent actorComponent = am.get(e);

      for (Actor child : actorComponent.actor.getChildren()) {
         EntityActor entityChild = (EntityActor) child;
         if (em.has(entityChild.getEntity())) {
            EngineComponent engine = em.get(entityChild.getEntity());
            engine.isRunning = Directions.compareOr(engine.directions, movableComponent.directions);
         }
      }
   }
}
