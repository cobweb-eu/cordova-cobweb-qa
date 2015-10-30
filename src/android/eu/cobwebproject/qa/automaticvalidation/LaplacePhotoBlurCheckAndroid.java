package eu.cobwebproject.qa.automaticvalidation;

import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LaplacePhotoBlurCheckAndroid extends LaplacePhotoBlurCheck{

    public LaplacePhotoBlurCheckAndroid(){
        super();
    }
            
    public LaplacePhotoBlurCheckAndroid(File imageFile, int threshold){
        super(imageFile, threshold);
    }

    @Override
    Colour createColour(int rgb) {
        return new AColour(rgb);
    }

    @Override
    Colour createColour(int r, int g, int b) {
        return new AColour(r, g, b);
    }

    @Override
    IImage createImage(int width, int height, int type) {
        return new AImage(Bitmap.createBitmap(width, height, Bitmap.Config.values()[type]));
    }
    
    @Override
    IImage createImage(int width, int height) {
        return new AImage(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888));
    }

    @Override
    IImage createImageGreyscale(int width, int height) {
        return new AImage(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));

    }

    @Override
    public IImage read(File file) throws IOException {
        return new AImage(BitmapFactory.decodeFile(file.getPath()));
    }
}
