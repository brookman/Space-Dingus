package eu32k.spaceDingus.core.component.weapon;

import eu32k.gdx.artemis.base.Component;
import eu32k.gdx.common.Time;

public class WeaponComponent extends Component {

   public static int BULLET_TYPE_NORMAL = 0;
   public static int BULLET_TYPE_ROCKET = 1;

   private int shootDelay; // In ms

   public boolean shootRequested;
   public long lastShoot;
   public float targetX;
   public float targetY;
   public float precision;
   public int bulletType;
   public boolean canRotate;

   public WeaponComponent init(int shootDelay, int bulletType, boolean canRotate) {
      this.shootDelay = shootDelay;
      this.bulletType = bulletType;
      this.canRotate = canRotate;
      shootRequested = false;
      lastShoot = 0;
      targetX = 0;
      targetY = 0;
      precision = 0.01f;
      return this;
   }

   public boolean shouldShoot() {
      return shootRequested && Time.getTime() - lastShoot >= shootDelay;
   }

   public void shoot() {
      lastShoot = Time.getTime();
      shootRequested = false;
   }
}