package main;

import com.moonmana.app.App;

import moontrackClientGateway.MoontrackClientGatewayApp;

public class AppGenerator {

	public static App generateApp() {
		return new MoontrackClientGatewayApp();
	}

}
