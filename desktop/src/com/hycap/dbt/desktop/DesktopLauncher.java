package com.hycap.dbt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hycap.dbt.DBTGame;

final class DesktopLauncher {
	private DesktopLauncher() {
	}

	public static void main (final String[] arg) {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
		config.foregroundFPS = 144;
		new LwjglApplication(new DBTGame(), config);
	}
}
