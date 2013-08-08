package eu32k.spaceDingus.core.component;

import com.artemis.Component;

public abstract class DeathAnimationComponent extends Component {

   public abstract void createAnimation(float x, float y, float rotation);
}
