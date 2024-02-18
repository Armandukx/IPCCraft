package io.armandukx.ipccraft.utils;

import io.armandukx.ipccraft.config.ClothConfig;
import io.armandukx.ipccraft.discordipc.IPCClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Screenshot {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    static File _SCREENSHOTSFOLDER = new File(MinecraftClient.getInstance().runDirectory, "IPCCraft-Screenshots");
    private static final Logger LOGGER = LogManager.getLogger(IPCClient.class);
    static MinecraftClient client = MinecraftClient.getInstance();
    public static void initScreenshot(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (ClothConfig.Screenshots.enabled) {
                    if (_SCREENSHOTSFOLDER.exists()){
                        String[]entries = _SCREENSHOTSFOLDER.list();
                        for(String s: entries){
                            File currentFile = new File(_SCREENSHOTSFOLDER.getPath(),s);
                            currentFile.delete();
                        }
                    }
                    if (client.player != null){
                        saveScreenShot();
                    }
                }
            }
        }, 0, SecondToMillisecondConverter.convertTime(ClothConfig.Screenshots.timer));
    }
    private static void saveScreenShot() {
        MinecraftClient.getInstance().execute(() -> {
            NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(MinecraftClient.getInstance().getFramebuffer());
            File file = new File(MinecraftClient.getInstance().runDirectory, "IPCCraft-Screenshots");
            file.mkdir();
            File file2 = getScreenshotFilename(file);
            Util.getIoWorkerExecutor().execute(() -> {
                try {
                    nativeImage.writeTo(file2);
                } catch (Exception text) {
                    LOGGER.warn("Couldn't save screenshot", text);
                } finally {
                    nativeImage.close();
                }
            });
        });
    }

    private static File getScreenshotFilename(File directory) {
        String string = DATE_FORMAT.format(new Date());
        int i = 1;
        File file;
        while ((file = new File(directory, string + (i == 1 ? "" : "_" + i) + ".png")).exists()) {
            ++i;
        }
        return file;
    }
}
