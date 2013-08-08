package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import eu32k.spaceDingus.core.InputHandler;
import eu32k.spaceDingus.core.component.weapon.PlayerControlledWeaponComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;

public class WeaponInputSystem extends EntityProcessingSystem {

   private ComponentMapper<WeaponComponent> wm;

   private InputHandler handler;

   @SuppressWarnings("unchecked")
   public WeaponInputSystem(InputHandler handler) {
      super(Aspect.getAspectForAll(WeaponComponent.class, PlayerControlledWeaponComponent.class));
      this.handler = handler;
   }

   @Override
   protected void initialize() {
      wm = world.getMapper(WeaponComponent.class);
   }

   @Override
   protected void process(Entity e) {
      WeaponComponent weapon = wm.get(e);
      weapon.targetX = handler.mouseX;
      weapon.targetY = handler.mouseY;
      weapon.shootRequested = handler.leftMouse;

   }
}
