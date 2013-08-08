package eu32k.spaceDingus.core.component;

import com.artemis.Component;

public class StabilizerComponent extends Component {

   public boolean translation;
   public boolean rotation;

   public StabilizerComponent init(boolean translation, boolean rotation) {
      this.translation = translation;
      this.rotation = rotation;
      return this;
   }
}
