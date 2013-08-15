package eu32k.spaceDingus.core.system.moving;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import eu32k.spaceDingus.core.component.TargetPositionComponent;

public class AimingSystem extends EntityProcessingSystem {

   private ComponentMapper<TargetPositionComponent> tpm;

   @SuppressWarnings("unchecked")
   public AimingSystem() {
      super(Aspect.getAspectForAll(TargetPositionComponent.class));
   }

   @Override
   protected void initialize() {
      tpm = world.getMapper(TargetPositionComponent.class);
   }

   @Override
   protected void process(Entity e) {
      // TODO Auto-generated method stub

   }
}
