package io.armandukx.rpccraft;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RPCCfraft implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rpccfraft");
	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello Fabric world!");
	}
}