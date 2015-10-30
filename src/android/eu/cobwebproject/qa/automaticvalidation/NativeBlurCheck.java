package eu.cobwebproject.qa.automaticvalidation;

import android.graphics.Bitmap;
import android.graphics.Color;

public class NativeBlurCheck {
    
    public Bitmap getLaplaceImage(Bitmap pic1){
        
        Bitmap pic2 = Bitmap.createBitmap(pic1.getWidth(), pic1.getHeight(), Bitmap.Config.RGB_565);
        
        int height = pic1.getHeight();
        int width = pic1.getWidth();
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int c00 = pic1.getPixel(x-1, y-1);
                int c01 = pic1.getPixel(x-1, y  );
                int c02 = pic1.getPixel(x-1, y+1);
                int c10 = pic1.getPixel(x  , y-1);
                int c11 = pic1.getPixel(x  , y  );
                int c12 = pic1.getPixel(x  , y+1);
                int c20 = pic1.getPixel(x+1, y-1);
                int c21 = pic1.getPixel(x+1, y  );
                int c22 = pic1.getPixel(x+1, y+1);
                int r = -Color.red(c00) -   Color.red(c01) - Color.red(c02) +
                        -Color.red(c10) + 8*Color.red(c11) - Color.red(c12) +
                        -Color.red(c20) -   Color.red(c21) - Color.red(c22);
                int g = -Color.green(c00) -   Color.green(c01) - Color.green(c02) +
                        -Color.green(c10) + 8*Color.green(c11) - Color.green(c12) +
                        -Color.green(c20) -   Color.green(c21) - Color.green(c22);
                int b = -Color.blue(c00) -   Color.blue(c01) - Color.blue(c02) +
                        -Color.blue(c10) + 8*Color.blue(c11) - Color.blue(c12) +
                        -Color.blue(c20) -   Color.blue(c21) - Color.blue(c22);
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));
                
                pic2.setPixel(x, y, Color.rgb(r, g, b));
            }
        }

        return pic2;
    }

}
