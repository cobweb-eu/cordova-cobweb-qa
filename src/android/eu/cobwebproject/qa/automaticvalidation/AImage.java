package eu.cobwebproject.qa.automaticvalidation;

import android.graphics.Bitmap;

public class AImage implements IImage{

    private Bitmap bitmap;
    
    public AImage(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    
    @Override
    public int getHeight() {
        return this.bitmap.getHeight();
    }

    @Override
    public int getRGB(int x, int y) {
        return this.bitmap.getPixel(x, y);
    }

    @Override
    public int getType() {
        return this.bitmap.getConfig().ordinal();
    }

    @Override
    public int getWidth() {
        return this.bitmap.getWidth();
    }

    @Override
    public void setRGB(int x, int y, int rgb) {
        this.bitmap.setPixel(x, y, rgb);
    }
}
