package be.kdg.integration.gameapplication.model.settings;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ColorPaletteGenerator{
    private StringBuilder firstColorCode;
    private StringBuilder secondColorCode;
    private StringBuilder thirdColorCode;
    private String toRewriteFile;

    public ColorPaletteGenerator(){
        this.firstColorCode = new StringBuilder("4A3E2A3A32262E291F3A25102A2418231F1A1E140B1E14081A120819100A120D09");
        this.secondColorCode = new StringBuilder("E8C860D4B96AD4A853C5A55A69522A5F4B2655432242331B2F24144A2E10");
        this.thirdColorCode = new StringBuilder("FFFFFF4AA888E8D5B0A3A188B8A880C0A0708A7A607A6E5C5A5040");
    }
    public void generateColor(int priorityNumber, String startHex, String endHex) throws IllegalArgumentException{
        int steps = 12 - priorityNumber;
        StringBuilder currentCode = switch(priorityNumber){
            case 1 -> firstColorCode;
            case 2 -> secondColorCode;
            case 3 -> thirdColorCode;
            default -> throw new IllegalArgumentException("Wrong priority number provided");
        };
        currentCode.setLength(0);

        AdjustableColor startColor = new AdjustableColor(startHex);
        AdjustableColor endColor = new AdjustableColor(endHex);

        float[] startHSL = startColor.getHSL();
        float[] endHSL = endColor.getHSL();
        float startHUE = startHSL[0];
        float endHUE = endHSL[0];
        float saturationStart = startHSL[1];
        float saturationEnd = endHSL[1];
        float lightnessStart = startHSL[2];
        float lightnessEnd = endHSL[2];;
        final float yGamma = 2F;

        for(int currentStep = 0; currentStep < steps; currentStep++){
            float rawT = (currentStep / (float) (steps - 1));
            float functionValue = (float) Math.pow(rawT, yGamma);
            float resultedHue = startHUE + (endHUE - startHUE) * functionValue;
            float resultedSaturation = saturationStart + (saturationEnd - saturationStart) * functionValue;
            float resultedLightness = lightnessStart + (lightnessEnd - lightnessStart) * functionValue;
            String value = AdjustableColor.getHexFromHSL(new float[]{resultedHue, resultedSaturation, resultedLightness});
            currentCode.append(value.toUpperCase());
        }
    }


    public void rewrite(){
        ArrayList<String> firstColor = new ArrayList<>();
        final int codeLength = 6;
        int lines = 11;
        for(int beginValue = 0; beginValue < (lines * codeLength); beginValue += codeLength){
            int endValue = beginValue + 6;
            firstColor.add(firstColorCode.substring(beginValue, endValue));
        }
        lines = 10;
        ArrayList<String> secondColor = new ArrayList<>();
        for(int beginValue = 0; beginValue < (lines * codeLength); beginValue += codeLength){
            int endValue = beginValue + codeLength;
            secondColor.add(secondColorCode.substring(beginValue, endValue));
        }
        lines = 9;
        ArrayList<String> thirdColor = new ArrayList<>();
        for(int beginValue = 0; beginValue < (lines * codeLength); beginValue += codeLength){
            int endValue = beginValue + codeLength;
            thirdColor.add(thirdColorCode.substring(beginValue, endValue));
        }

        this.toRewriteFile = String.format(
                """
                        /*Updated*/
                        .root {
                            /* --- first (brown/dark) --- */
                            color-first-light-level-1: #%s; /* color-first-light-level-1 */ /* final-screen (.btn-secondary border) */
                            color-first-light-level-2: #%s; /* color-first-light-level-2 */  /* final-screen (.divider) */
                            color-first-light-level-3: #%s; /* color-first-light-level-3 */ /* final-screen (.stat-box border) */
                            color-first-light-level-4: #%s;  /* final-screen (.btn-primary bg), login-page (.btn-primary bg) */ /* login-page (.btn-primary bg) */ /* login-page (.btn-primary:hover bg), mode-selection (.button:hover, .btn-ghost:hover), tutorial-page (.button:hover) */
                            /* mode-selection (.separator, .games-*-card, .competitive-info, .toggle-button, .spinner), tutorial-page (.text-area border, .scroll-bar thumb) */
                        
                            color-first-medium-level-1: #%s; /* color-btn-secondary-bg-hover */ /* final-screen */
                            color-first-medium-level-2: #%s; /* color-stat-box-bg */ /* final-screen (.stat-box background) */
                            color-first-medium-level-3: #%s; /* color-bg-overlay-medium */
                        
                        
                            color-first-dark-level-1: #%s; /* color-first-dark-level-1 */ /* mode-selection (.games-*-card), tutorial-page (.text-area .content) */
                            color-first-dark-level-2: #%s; /* color-first-dark-level-2 */ /* mode-selection (.spinner text-field, arrow buttons) */
                            color-first-dark-level-3: #%s;  /* leader-board (.leaderboard-table) */
                            /*THIS ONE IS BASE BG COLOR*/ color-first-dark-level-4: #%s; /*it is main bg color, color-bg-popup, color-bg-scrollbar */
                        
                            /* --- second (gold/amber) --- */
                            color-second-light-level-1: #%s; /* color-gold-bright */
                            color-second-light-level-2: #%s; /* color-gold-medium */ /* final-screen (.btn-primary text) */
                            color-second-light-level-3: #%s; /* color-gold-main */ /* login-page (.btn-primary border, .btn-ghost:hover border), mode-selection (.button, .difficulty-hard, .toggle-button:selected border, spinner arrows), tutorial-page (.button, .scroll-bar thumb:hover), leader-board (column headers, scroll arrows), start-view (.menu-button:hover) */
                        
                            color-second-medium-level-1: #%s; /* color-gold-dim */ /* final-screen (.stat-val-default, .stat-val-time) */
                            color-second-medium-level-2: #%s; /* color-second-medium-level-2 */ /* leader-board (scrollbar thumb:hover) */
                            color-second-medium-level-3: #%s; /* color-second-medium-level-3 */ /* login-page (.login-box border, .popup-box border, .btn-ghost border) */ /* final-screen (.btn-primary border) */
                        
                            color-second-dark-level-1: #%s; /* color-gold-scroll */ /* leader-board (.leaderboard-table border, scroll arrows) */
                            color-second-dark-level-2: #%s; /* color-second-dark-level-2 */ /* leader-board (scrollbar thumb) */
                            color-second-dark-level-3: #%s; /* color-second-dark-level-3 */ /* leader-board (selected row background) */
                            color-second-dark-level-4: #%s; /* final-screen (.btn-primary:hover bg), login-page (.btn-primary:hover bg) */
                        
                            /* --- third (text/neutral) --- */
                            color-third-light-level-1: #%s; /* color-table-bg-white */
                            color-third-light-level-2: #%s; /* color-scrollbar-thumb */ /* start-view, style (.table-row-cell:selected text) */
                            color-third-light-level-3: #%s; /* color-text-primary */
                        
                            color-third-medium-level-1: #%s; /* color-scrollbar-track */
                            color-third-medium-level-2: #%s; /* color-third-medium-level-2 */ /* final-screen (.btn-secondary text) */
                            color-third-medium-level-3: #%s; /* color-third-medium-level-3 */ /* login-page (.sub-label, .forget-password, .btn-ghost), mode-selection (.btn-ghost, .rules-label-column), start-view (.sub-label) */
                        
                            color-third-dark-level-1: #%s; /* color-text-muted */ /* final-screen (.game-over-label) */
                            color-third-dark-level-2: #%s; /*  color-third-dark-level-2 */ /* final-screen (.congrats-label) */
                            color-third-dark-level-3: #%s; /* color-third-dark-level-3 */ /* final-screen (.stat-label, .thanks-label) */
                            
                            color-bg-overlay-light: #18100A; /* leader-board (odd table rows) */
                            color-gold-row-hover: #21190E; /* leader-board (row:hover background) */
                            color-bg-overlay-header: #2A1B0D; /* leader-board (column headers, filler) */
                            color-gold-border-faint: #251C10; /* leader-board (row borders) */
                            color-bg-overlay-strong: #26190C; /* login-page (.btn-ghost:hover) */
                            }
                        """,
                firstColor.get(0), firstColor.get(1), firstColor.get(2),
                firstColor.get(3), firstColor.get(4), firstColor.get(5),
                firstColor.get(6), firstColor.get(7), firstColor.get(8),
                firstColor.get(9), firstColor.get(10),
                secondColor.get(0), secondColor.get(1), secondColor.get(2),
                secondColor.get(3), secondColor.get(4), secondColor.get(5),
                secondColor.get(6), secondColor.get(7), secondColor.get(8),
                secondColor.get(9),
                thirdColor.get(0), thirdColor.get(1), thirdColor.get(2),
                thirdColor.get(3), thirdColor.get(4), thirdColor.get(5),
                thirdColor.get(6), thirdColor.get(7), thirdColor.get(8)
        );

        try(BufferedWriter writer = new BufferedWriter(
                new FileWriter(Paths.get(
                        "resources/css/color-variables.css").toFile(), false))){
            writer.write(toRewriteFile);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
