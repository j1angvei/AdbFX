package cn.j1angvei.adbfx.adb;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ScreenShotService extends Service<File> {
    private static final String IMG_NAME_PREFIX = "ScreenShot_AdbFX_";

    private IDevice mDevice;
    private String mSaveDir;
    private boolean mLandScape;

    public final void restart(@NonNull IDevice device, @NonNull String saveDir, @NonNull Boolean landscape) {
        mDevice = device;
        mSaveDir = saveDir;
        mLandScape = landscape;
        this.restart();
    }

    @Override
    protected Task<File> createTask() {
        return new Task<File>() {
            @Override
            protected File call() {

                String imgPath = mSaveDir + File.separator + IMG_NAME_PREFIX + System.currentTimeMillis() + ".png";
                File imgFile = new File(imgPath);

                try {
                    RawImage rawImage = mDevice.getScreenshot();

                    if (rawImage == null) {
                        throw new NullPointerException("raw image from device is NULL");
                    }

                    if (mLandScape) {
                        rawImage = rawImage.getRotated();
                    }

                    // convert raw data to an Image
                    BufferedImage image = new BufferedImage(rawImage.width, rawImage.height,
                            BufferedImage.TYPE_INT_ARGB);

                    int index = 0;
                    int IndexInc = rawImage.bpp >> 3;
                    for (int y = 0; y < rawImage.height; y++) {
                        for (int x = 0; x < rawImage.width; x++) {
                            int value = rawImage.getARGB(index);
                            index += IndexInc;
                            image.setRGB(x, y, value);
                        }
                    }

                    if (!ImageIO.write(image, "png", imgFile)) {
                        throw new IOException("Failed to find png writer");
                    }

                } catch (TimeoutException | AdbCommandRejectedException | IOException e) {
                    log.error("Error when take screen shot", e);
                }

                return imgFile;
            }
        };
    }
}
