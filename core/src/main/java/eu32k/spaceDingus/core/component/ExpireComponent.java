package eu32k.spaceDingus.core.component;

import eu32k.gdx.artemis.base.Component;
import eu32k.gdx.common.Time;

public class ExpireComponent extends Component {

   public long expireAt = 0;

   public ExpireComponent init(long expTime) {
      expireAt = Time.getTime() + expTime;
      return this;
   }
}
