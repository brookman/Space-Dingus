package eu32k.spaceDingus.html;

import eu32k.spaceDingus.core.SpaceDingus;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class SpaceDingusHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new SpaceDingus();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
