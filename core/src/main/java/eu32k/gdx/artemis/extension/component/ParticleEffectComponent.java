package eu32k.gdx.artemis.extension.component;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import eu32k.gdx.artemis.base.Component;

public class ParticleEffectComponent extends Component {

   public ParticleEffect effect;

   public ParticleEffectComponent init(ParticleEffect effect) {
      this.effect = effect;
      return this;
   }
}
