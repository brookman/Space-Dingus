package eu32k.spaceDingus.core.component;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

import eu32k.gdx.artemis.base.Component;
import eu32k.gdx.common.Bits;
import eu32k.spaceDingus.core.common.GameBits;

public class PhysicsShieldComponent extends Component {

   private Bits bits;
   private Fixture fixture;
   private boolean enabled;

   public PhysicsShieldComponent init(Bits bits, Fixture fixture) {
      this.bits = bits;
      this.fixture = fixture;
      enabled = false;
      enableCollision();
      return this;
   }

   public void enableCollision() {
      if (enabled) {
         return;
      }
      Filter filter = fixture.getFilterData();
      filter.categoryBits = bits.category;
      filter.maskBits = bits.mask;
      fixture.setFilterData(filter);
      enabled = true;
   }

   public void disableCollision() {
      if (!enabled) {
         return;
      }
      Filter filter = fixture.getFilterData();
      filter.categoryBits = GameBits.VOID.category;
      filter.maskBits = GameBits.VOID.mask;
      fixture.setFilterData(filter);
      enabled = false;
   }
}
