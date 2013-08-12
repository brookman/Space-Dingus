package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.TransformComponent;

public class PhysicsSystem extends EntitySystem {

   private ComponentMapper<PhysicsComponent> pm;
   private ComponentMapper<TransformComponent> tm;

   private World box2dWorld;

   @SuppressWarnings("unchecked")
   public PhysicsSystem(World box2dWorld) {
      super(Aspect.getAspectForAll(PhysicsComponent.class, TransformComponent.class));
      this.box2dWorld = box2dWorld;
   }

   @Override
   protected void initialize() {
      pm = world.getMapper(PhysicsComponent.class);
      tm = world.getMapper(TransformComponent.class);
   }

   @Override
   protected boolean checkProcessing() {
      return true;
   }

   @Override
   protected void processEntities(ImmutableBag<Entity> entities) {
      box2dWorld.step(world.delta, 8, 3);
      for (int i = 0; i < entities.size(); i++) {
         process(entities.get(i));
      }
   }

   private void process(Entity e) {
      TransformComponent trans = tm.get(e);
      PhysicsComponent physics = pm.get(e);

      trans.x = physics.body.getPosition().x;
      trans.y = physics.body.getPosition().y;
      trans.setRotation(physics.body.getAngle() * MathUtils.radiansToDegrees);
   }
}
