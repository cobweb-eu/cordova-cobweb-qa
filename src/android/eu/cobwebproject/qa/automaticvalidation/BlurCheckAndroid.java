package eu.cobwebproject.qa.automaticvalidation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Vector;

import android.os.Environment;
import android.util.Log;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.renderscript.Allocation;
import android.renderscript.Element;

/**
* Class to Check for Blurness of an image. The algorithm is as follows
* 1. Greyscale and resize image
* 2. Convolve around a Laplace kernel defined below
* 3. Calculate variance of result and compare with user defined threshold
*
* Example Usage:
*     test = new BlurCheckAndroid(myimage, 1000, true);
*      test.setContext(cordovaContext);
*      test.run();
*      if (test.pass){ //.. test has passed
*
* 
* @author EDINA
*/
public class BlurCheckAndroid implements Runnable {
        /*********/

        /* the actual Kernel. Any 3x3 kernel should work here for experimentation e.g. {-1,-1,-1,-1,8,-1,-1,-1,-1}  */
        public static final float LAPLACE_KERNEL[]={0,-1,0,-1,4,-1,0,-1,0};
        /* Check this after the test is run for the result. This is null if process has not yet been "run()" */
        public Boolean pass;

        /*********/

        private File file;
        private int threshold;
        private Bitmap original;
        private boolean debug;
        private Context context;

        /** use this for debug messages for now */
        private void dbg(String msg){
             Log.i("BlurCheckAndroid", msg);
        }

        /** Theshold is the desired variance i.e. the higher the sharper ( 1500 is a good start) */ 
        public BlurCheckAndroid(File imageFile, int threshold, boolean debug){
            this.context = null;
            this.pass = null;
            this.file = imageFile;
            this.threshold = threshold;
            this.debug = debug;
            this.original=BitmapFactory.decodeFile(imageFile.getPath());
            dbg("image size = " + original.getWidth() + " " + original.getHeight());
        }
    
        /** Set the context as required by RenderScript. CordovaPlugins provide their own. */
        public void setContext(Context ctx){
            this.context = ctx;
        }

        /** Executes the test. Can run as a thread but should be quite fast anyway */
        public void run(){
            if (this.context == null){
                dbg("called run() before setting the context variable. Aborting.");
                return;
            }
            Bitmap blackAndWhiteImage = convertImageToGrey(original);
            Bitmap laplaceImage = convolve(blackAndWhiteImage, LAPLACE_KERNEL);
            if (this.debug){
                dump(blackAndWhiteImage, file.getName() + "-grey.jpg");
                dump(laplaceImage, file.getName() + "-laplace.jpg");
            }
            this.pass = getPassDecision(laplaceImage);
        }

        /** 
        * Preprocess image by 1. resizing it to 500x500 and 2. greyscaling it
        *
        * @param img the input image
        * @return the output image
        */        

        public Bitmap convertImageToGrey(Bitmap img){
            int newWidth = 500; // or img.getWidth()
            int newHeight = 500; // or img.getHeight()
            //"resized" is img after resizing
            Bitmap resized = Bitmap.createScaledBitmap(img, newWidth, newHeight, true);
            //"output" is resized in greyscale
            Bitmap outImage =  Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.RGB_565);

            Canvas c = new Canvas(outImage);
            Paint p = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);            
            p.setColorFilter(new ColorMatrixColorFilter(cm));
            c.drawBitmap(resized, 0, 0, p);
            return outImage;
        }

        /** 
        * Apply a 3x3 kernel on image using H/W accelerated RenderScript
        *
        * @param src The image
        * @param k the 3x3 kernel
        */
        private Bitmap convolve(Bitmap src, final float[] k) {
            Bitmap out = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(),
                Bitmap.Config.RGB_565);

            RenderScript rs = RenderScript.create(context);

            Allocation allocIn = Allocation.createFromBitmap(rs, src);
            Allocation allocOut = Allocation.createFromBitmap(rs, out);

            ScriptIntrinsicConvolve3x3 convolution
                = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
            convolution.setInput(allocIn);
            convolution.setCoefficients(k);
            convolution.forEach(allocOut);

            allocOut.copyTo(out);
            rs.destroy();
            return out;
        }

        private boolean getPassDecision( Bitmap img ){
            long variance = getVariance(img);
            dbg("Variance is : " + variance);
            return variance > threshold;
        }

        /** 
        * Write the image to disc
        *
        * @param img The image
        * @param fname the filename eg. "foo.jpg"
        */
        private void dump( Bitmap img, String fname){
            File filepath = new File(Environment.getExternalStorageDirectory().toString(), "/Pictures/" + fname);
            dbg("Dumping: " + filepath.getPath());
            try{
                FileOutputStream fos = new FileOutputStream(filepath);
                img.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.flush();
                fos.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        /** 
        * Calculare variance of all pixels. Assumes greyscale i.e. R=G=B 
        *
        * @param img The greyscaled image
        * @return the variance of the image
        */
        private long getVariance ( Bitmap img ){
            Vector<Integer> data = new Vector<Integer>(20000,5000);
            long sum = 0;
            long mean = 0;
            long variance = 0;
            // Populate vector<int> from image data -- greyscale implies R=G=B;
            for (int i = 0; i < img.getWidth(); i++){
                for (int j = 0; j < img.getHeight(); j++){
                    int c = img.getPixel(i, j);
                    data.add( Color.red(c) ); //remember R=G=B
                }
            }

            //find mean
            for ( int pix : data ){
                sum += pix;
            }
            mean = sum  / data.size();

            //find variance
            int temp = 0;
            for(int pix :data)
                temp += (mean-pix)*(mean-pix);
            variance = temp/data.size();
            
            dbg("Sum: " + sum);
            dbg("Size: " + data.size());
            dbg("Mean: " + mean);
            dbg("Variance: " + variance);

            return variance;
        }
}
