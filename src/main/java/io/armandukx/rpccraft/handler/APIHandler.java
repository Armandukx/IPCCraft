package io.armandukx.rpccraft.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.armandukx.rpccraft.discordipc.IPCClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class APIHandler {
    private static final Logger LOGGER = LogManager.getLogger(IPCClient.class);
    public static JsonObject getResponse(String urlString, boolean hasError) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                connection.disconnect();

                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(response.toString());
                if (jsonElement.isJsonObject()) {
                    return jsonElement.getAsJsonObject();
                } else {
                    LOGGER.debug("Invalid JSON response");
                }
            } else {
                LOGGER.debug("HTTP request failed with response code: " + responseCode);
            }

            connection.disconnect();

        }
        catch (IOException e) {
            if (hasError) {
                e.printStackTrace();
            } else {
                LOGGER.debug("Error occurred while performing HTTP request: " + e.getMessage());
            }
        }
        return null;
    }
}