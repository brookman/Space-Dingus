package eu32k.spaceDingus.core.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleEffectComponent extends Component {

   public ParticleEffect effect;

   public ParticleEffectComponent init(ParticleEffect effect) {
      this.effect = effect;
      return this;
   }
}
