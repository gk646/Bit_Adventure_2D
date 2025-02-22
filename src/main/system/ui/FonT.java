/*
 * MIT License
 *
 * Copyright (c) 2023 gk646
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package main.system.ui;

import javafx.scene.text.Font;

public class FonT {
    //MINECRAFT STYLE

    public static Font minecraftItalic12;
    public static Font minecraftRegular20;
    public static Font minecraftRegular18;
    public static Font antParty16;
    public static Font varnished14;
    public static Font antParty15;
    public static Font minecraftBold15;
    private static Font minecraftRegular14;
    public static Font minecraftItalic15;
    private static Font minecraftItalic17;
    private static Font minecraftItalic30;
    public static Font minecraftBold14;
    public static Font minecraftBold20;
    public static Font minecraftBold30;
    public static Font minecraftBold50;

    public static Font minecraftBold13;
    public static Font minecraftBold16;
    public static Font minecraftBoldItalic15;

    //MARU MONICA
    private static Font maruMonica;

    private static Font maruMonica90;

    //PUBLIC PIXEL
    private static Font publicPixel20;
    public static Font editUndo18;
    public static Font editUndo16;
    public static Font editUndo19;
    public static Font editUndo22;
    public static Font editUndo24;
    public static Font minecraftItalic14;
    private static Font minecraftBold17;
    public static Font minecraftItalic11;
    public static Font minecraftBoldItalic14;
    private static Font minecraftBoldItalic18;
    public static Font antParty18;
    public static Font antParty20;
    public static Font antParty30;
    public static Font varnished16;

    public static void loadFonts() {
        minecraftRegular14 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftRegular-Bmg3.otf"), 14);
        minecraftRegular20 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftRegular-Bmg3.otf"), 20);
        minecraftRegular18 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftRegular-Bmg3.otf"), 18);
        minecraftItalic12 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftItalic-R8Mo.otf"), 12);
        minecraftItalic14 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftItalic-R8Mo.otf"), 14);
        minecraftItalic11 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftItalic-R8Mo.otf"), 11);
        minecraftItalic15 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftItalic-R8Mo.otf"), 15);
        minecraftItalic17 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftItalic-R8Mo.otf"), 17);
        minecraftItalic30 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftItalic-R8Mo.otf"), 30);
        minecraftBold13 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBold-nMK1.otf"), 13);
        minecraftBold16 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBold-nMK1.otf"), 16);
        minecraftBold15 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBold-nMK1.otf"), 15);
        minecraftBold17 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBold-nMK1.otf"), 17);
        minecraftBold20 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBold-nMK1.otf"), 25);
        minecraftBold14 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBold-nMK1.otf"), 14);
        minecraftBold50 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBold-nMK1.otf"), 50);
        minecraftBoldItalic15 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBoldItalic-1y1e.otf"), 15);
        minecraftBoldItalic18 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBoldItalic-1y1e.otf"), 18);
        minecraftBoldItalic14 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBoldItalic-1y1e.otf"), 14);
        maruMonica = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/x12y16pxMaruMonica.ttf"), 20);
        maruMonica90 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/x12y16pxMaruMonica.ttf"), 90);
        publicPixel20 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/PublicPixel-z84yD.ttf"), 15);
        editUndo18 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/EditUndoBrk-RwaV.ttf"), 18);
        editUndo16 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/EditUndoBrk-RwaV.ttf"), 16);
        editUndo19 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/EditUndoBrk-RwaV.ttf"), 19);
        editUndo22 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/EditUndoBrk-RwaV.ttf"), 22);
        editUndo24 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/EditUndoBrk-RwaV.ttf"), 24);
        antParty18 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/Ant Party.ttf"), 18);
        antParty20 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/Ant Party.ttf"), 20);
        antParty16 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/Ant Party.ttf"), 16);
        varnished14 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/Varnished.ttf"), 14);
        varnished16 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/Varnished.ttf"), 16);
        antParty30 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/Ant Party.ttf"), 30);
        antParty15 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/Ant Party.ttf"), 15);
    }
}
