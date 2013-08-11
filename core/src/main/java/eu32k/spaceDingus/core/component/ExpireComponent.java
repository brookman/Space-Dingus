package eu32k.spaceDingus.core.component;

import com.artemis.Component;

import eu32k.spaceDingus.core.common.Time;

public class ExpireComponent extends Component {

   public long expireAt = 0;

   public ExpireComponent init(long expTime) {
      expireAt = Time.getTime() + expTime;
      return this;
   }
}
