package eu.cobwebproject.qa.automaticvalidation;

import android.graphics.Color;

public class AColour implements Colour{
    private int colour;

    public AColour(int rgb){
        this.colour = rgb;
    }
    
    public AColour(int r, int g, int b){
        this.colour = Color.rgb(r, g, b);
    }
    
    @Override
    public int getAlpha() {
        return Color.alpha(this.colour);
    }

    @Override
    public int getBlue() {
        return Color.blue(this.colour);
    }

    @Override
    public int getGreen() {
        return Color.green(this.colour);
    }

    @Override
    public int getRed() {
        return Color.green(this.colour);
    }
    
    @Override
    public int getRGB() {
        return this.colour;
    }
}
