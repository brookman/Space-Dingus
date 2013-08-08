package eu32k.spaceDingus.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import eu32k.spaceDingus.core.SpaceDingus;

public class SpaceDingusDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		new LwjglApplication(new SpaceDingus(), config);
	}
}
