package be.kdg.integration.gameapplication.model.settings;

public class AdjustableColor{
    public static final double MIN_COLOR = 1;
    public static final double MAX_COLOR = 255;
    private double red;
    private double green;
    private double blue;

    public AdjustableColor(double red, double green, double blue){
        this.red = MIN_COLOR;
        this.green = MIN_COLOR;
        this.blue = MIN_COLOR;
        if(red >= MIN_COLOR && red <= MAX_COLOR) this.red = red;
        if(green >= MIN_COLOR && green <= MAX_COLOR) this.green = green;
        if(blue >= MIN_COLOR && blue <= MAX_COLOR) this.blue = blue;
    }

    public AdjustableColor(String hex){
        setColorUsingHexValue(hex);
    }

    public String getHexFromRGB(){
        return String.format("#%02X%02X%02X", (int) red, (int) green, (int) blue);
    }

    public static String getHexFromHSL(float[] HSL){
        float HUE = HSL[0];
        float saturation = HSL[1];
        float lightness = HSL[2];
        saturation = saturation / 100F; //normalising values because
        lightness = lightness / 100F; //algorithm needs value between 0.0 and 1.0

        //amplitude
        float a = saturation * Math.min(lightness, 1 - lightness);

        final int redShift = 0;
        final int greenShift = 8;
        final int blueShift = 4;

        int r = hslChannel(HUE, redShift, lightness , a);
        int g = hslChannel(HUE, greenShift, lightness, a);
        int b = hslChannel(HUE, blueShift, lightness, a);
        return String.format("%02x%02x%02x", r, g, b);
    }

    private static int hslChannel(float HUE, float nNumberOfSpectrumShifts, float lightness, float amplitude) {
        float k = (nNumberOfSpectrumShifts + HUE / 30f) % 12;
        //building trapezoid
        float clamp = lightness - amplitude * Math.max(Math.min(Math.min(k - 3, 9 - k), 1), -1);
        return Math.round(clamp * 255);
    }

    public void setColorUsingHexValue(String hex){
        if(hex.isEmpty()) return;
        if(!hex.substring(0,1).contains("#")){
            hex = "#" + hex;
        }
        red = Integer.parseInt(hex.substring(1, 3), 16);
        green = Integer.parseInt(hex.substring(3, 5), 16);
        blue = Integer.parseInt(hex.substring(5, 7), 16);
    }

    public double getBlue(){
        return blue;
    }

    public void setBlue(double blue){
        this.blue = blue;
    }

    public double getGreen(){
        return green;
    }

    public void setGreen(double green){
        this.green = green;
    }

    public double getRed(){
        return red;
    }

    public void setRed(double red){
        this.red = red;
    }

    public float[] getHSL(){
        float redRatio = (float) getRed() / 255f;
        float greenRatio = (float) getGreen() / 255f;
        float blueRatio = (float) getBlue() / 255f;

        float max = Math.max(redRatio, Math.max(greenRatio, blueRatio));
        float min = Math.min(redRatio, Math.min(greenRatio, blueRatio));
        float delta = max - min;

        float lightness = (max + min) / 2f;
        float saturation = 0;
        if(delta != 0){
            saturation = delta / (1 - Math.abs(2 * lightness - 1));
        }

        // HUE
        float HUE = 0;
        if(delta != 0){
            if(max == redRatio){
                HUE = 60 * (((greenRatio - blueRatio) / delta) % 6);
            }else if(max == greenRatio){
                HUE = 60 * (((blueRatio - redRatio) / delta) + 2);
            }else{
                HUE = 60 * (((redRatio - greenRatio) / delta) + 4);
            }
        }

        if(HUE < 0){
            HUE += 360;
        }
        // returning H(0-360), S(0-100), L(0-100)
        return new float[]{HUE, saturation * 100, lightness * 100};
    }
}
