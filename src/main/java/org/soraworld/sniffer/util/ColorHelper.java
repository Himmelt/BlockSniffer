package org.soraworld.sniffer.util;

import java.awt.*;
import java.util.HashMap;
import java.util.regex.Pattern;

public final class ColorHelper {

    private static final Pattern PATTERN_COLOR = Pattern.compile("#[0-9a-fA-F]{1,6}");
    private static final HashMap<String, Color> colorMap = new HashMap<>();

    private static final Color MCCOLOR1 = new Color(0, 0, 170);
    private static final Color MCCOLOR2 = new Color(0, 170, 0);
    private static final Color MCCOLOR3 = new Color(0, 170, 170);
    private static final Color MCCOLOR4 = new Color(170, 0, 0);
    private static final Color MCCOLOR5 = new Color(170, 0, 170);
    private static final Color MCCOLOR6 = new Color(255, 170, 0);
    private static final Color MCCOLOR7 = new Color(170, 170, 170);
    private static final Color MCCOLOR8 = new Color(85, 85, 85);
    private static final Color MCCOLOR9 = new Color(85, 85, 255);
    private static final Color MCCOLORA = new Color(85, 255, 85);
    private static final Color MCCOLORB = new Color(85, 255, 255);
    private static final Color MCCOLORC = new Color(255, 85, 85);
    private static final Color MCCOLORD = new Color(255, 85, 255);
    private static final Color MCCOLORE = new Color(255, 255, 85);

    private static final Color ALICEBLUE = new Color(240, 248, 255);
    private static final Color ANTIQUEWHITE = new Color(250, 235, 215);
    private static final Color AQUA = new Color(0, 255, 255);
    private static final Color AQUAMARINE = new Color(127, 255, 212);
    private static final Color AZURE = new Color(240, 255, 255);
    private static final Color BEIGE = new Color(245, 245, 220);
    private static final Color BISQUE = new Color(255, 228, 196);
    private static final Color BLACK = new Color(0, 0, 0);
    private static final Color BLANCHEDALMOND = new Color(255, 235, 205);
    private static final Color BLUE = new Color(0, 0, 255);
    private static final Color BLUEVIOLET = new Color(138, 43, 226);
    private static final Color BROWN = new Color(165, 42, 42);
    private static final Color BURLYWOOD = new Color(222, 184, 135);
    private static final Color CADETBLUE = new Color(95, 158, 160);
    private static final Color CHARTREUSE = new Color(127, 255, 0);
    private static final Color CHOCOLATE = new Color(210, 105, 30);
    private static final Color CORAL = new Color(255, 127, 80);
    private static final Color CORNFLOWERBLUE = new Color(100, 149, 237);
    private static final Color CORNSILK = new Color(255, 248, 220);
    private static final Color CRIMSON = new Color(220, 20, 60);
    private static final Color CYAN = new Color(0, 255, 255);
    private static final Color DARKBLUE = new Color(0, 0, 139);
    private static final Color DARKCYAN = new Color(0, 139, 139);
    private static final Color DARKGOLDENROD = new Color(184, 134, 11);
    private static final Color DARKGRAY = new Color(169, 169, 169);
    private static final Color DARKGREEN = new Color(0, 100, 0);
    private static final Color DARKKHAKI = new Color(189, 183, 107);
    private static final Color DARKMAGENTA = new Color(139, 0, 139);
    private static final Color DARKOLIVEGREEN = new Color(85, 107, 47);
    private static final Color DARKORANGE = new Color(255, 140, 0);
    private static final Color DARKORCHID = new Color(153, 50, 204);
    private static final Color DARKRED = new Color(139, 0, 0);
    private static final Color DARKSALMON = new Color(233, 150, 122);
    private static final Color DARKSEAGREEN = new Color(143, 188, 143);
    private static final Color DARKSLATEBLUE = new Color(72, 61, 139);
    private static final Color DARKSLATEGRAY = new Color(47, 79, 79);
    private static final Color DARKTURQUOISE = new Color(0, 206, 209);
    private static final Color DARKVIOLET = new Color(148, 0, 211);
    private static final Color DEEPPINK = new Color(255, 20, 147);
    private static final Color DEEPSKYBLUE = new Color(0, 191, 255);
    private static final Color DIMGRAY = new Color(105, 105, 105);
    private static final Color DIMGREY = new Color(105, 105, 105);
    private static final Color DODGERBLUE = new Color(30, 144, 255);
    private static final Color FIREBRICK = new Color(178, 34, 34);
    private static final Color FLORALWHITE = new Color(255, 250, 240);
    private static final Color FORESTGREEN = new Color(34, 139, 34);
    private static final Color FUCHSIA = new Color(255, 0, 255);
    private static final Color GAINSBORO = new Color(220, 220, 220);
    private static final Color GHOSTWHITE = new Color(248, 248, 255);
    private static final Color GOLD = new Color(255, 215, 0);
    private static final Color GOLDENROD = new Color(218, 165, 32);
    private static final Color GRAY = new Color(128, 128, 128);
    private static final Color GREEN = new Color(0, 128, 0);
    private static final Color GREENYELLOW = new Color(173, 255, 47);
    private static final Color HONEYDEW = new Color(240, 255, 240);
    private static final Color HOTPINK = new Color(255, 105, 180);
    private static final Color INDIANRED = new Color(205, 92, 92);
    private static final Color INDIGO = new Color(75, 0, 130);
    private static final Color IVORY = new Color(255, 255, 240);
    private static final Color KHAKI = new Color(240, 230, 140);
    private static final Color LAVENDER = new Color(230, 230, 250);
    private static final Color LAVENDERBLUSH = new Color(255, 240, 245);
    private static final Color LAWNGREEN = new Color(124, 252, 0);
    private static final Color LEMONCHIFFON = new Color(255, 250, 205);
    private static final Color LIGHTBLUE = new Color(173, 216, 230);
    private static final Color LIGHTCORAL = new Color(240, 128, 128);
    private static final Color LIGHTCYAN = new Color(224, 255, 255);
    private static final Color LIGHTGOLDENRODYELLOW = new Color(250, 250, 210);
    private static final Color LIGHTGRAY = new Color(211, 211, 211);
    private static final Color LIGHTGREEN = new Color(144, 238, 144);
    private static final Color LIGHTPINK = new Color(255, 182, 193);
    private static final Color LIGHTSALMON = new Color(255, 160, 122);
    private static final Color LIGHTSEAGREEN = new Color(32, 178, 170);
    private static final Color LIGHTSKYBLUE = new Color(135, 206, 250);
    private static final Color LIGHTSLATEGRAY = new Color(119, 136, 153);
    private static final Color LIGHTSTEELBLUE = new Color(176, 196, 222);
    private static final Color LIGHTYELLOW = new Color(255, 255, 224);
    private static final Color LIME = new Color(0, 255, 0);
    private static final Color LIMEGREEN = new Color(50, 205, 50);
    private static final Color LINEN = new Color(250, 240, 230);
    private static final Color MAGENTA = new Color(255, 0, 255);
    private static final Color MAROON = new Color(128, 0, 0);
    private static final Color MEDIUMAQUAMARINE = new Color(102, 205, 170);
    private static final Color MEDIUMBLUE = new Color(0, 0, 205);
    private static final Color MEDIUMORCHID = new Color(186, 85, 211);
    private static final Color MEDIUMPURPLE = new Color(147, 112, 219);
    private static final Color MEDIUMSEAGREEN = new Color(60, 179, 113);
    private static final Color MEDIUMSLATEBLUE = new Color(123, 104, 238);
    private static final Color MEDIUMSPRINGGREEN = new Color(0, 250, 154);
    private static final Color MEDIUMTURQUOISE = new Color(72, 209, 204);
    private static final Color MEDIUMVIOLETRED = new Color(199, 21, 133);
    private static final Color MIDNIGHTBLUE = new Color(25, 25, 112);
    private static final Color MINTCREAM = new Color(245, 255, 250);
    private static final Color MISTYROSE = new Color(255, 228, 225);
    private static final Color MOCCASIN = new Color(255, 228, 181);
    private static final Color NAVAJOWHITE = new Color(255, 222, 173);
    private static final Color NAVY = new Color(0, 0, 128);
    private static final Color OLDLACE = new Color(253, 245, 230);
    private static final Color OLIVE = new Color(128, 128, 0);
    private static final Color OLIVEDRAB = new Color(107, 142, 35);
    private static final Color ORANGE = new Color(255, 165, 0);
    private static final Color ORANGERED = new Color(255, 69, 0);
    private static final Color ORCHID = new Color(218, 112, 214);
    private static final Color PALEGOLDENROD = new Color(238, 232, 170);
    private static final Color PALEGREEN = new Color(152, 251, 152);
    private static final Color PALETURQUOISE = new Color(175, 238, 238);
    private static final Color PALEVIOLETRED = new Color(219, 112, 147);
    private static final Color PAPAYAWHIP = new Color(255, 239, 213);
    private static final Color PEACHPUFF = new Color(255, 218, 185);
    private static final Color PERU = new Color(205, 133, 63);
    private static final Color PINK = new Color(255, 192, 203);
    private static final Color PLUM = new Color(221, 160, 221);
    private static final Color POWDERBLUE = new Color(176, 224, 230);
    private static final Color PURPLE = new Color(128, 0, 128);
    private static final Color RED = new Color(255, 0, 0);
    private static final Color ROSYBROWN = new Color(188, 143, 143);
    private static final Color ROYALBLUE = new Color(65, 105, 225);
    private static final Color SADDLEBROWN = new Color(139, 69, 19);
    private static final Color SALMON = new Color(250, 128, 114);
    private static final Color SANDYBROWN = new Color(244, 164, 96);
    private static final Color SEAGREEN = new Color(46, 139, 87);
    private static final Color SEASHELL = new Color(255, 245, 238);
    private static final Color SIENNA = new Color(160, 82, 45);
    private static final Color SILVER = new Color(192, 192, 192);
    private static final Color SKYBLUE = new Color(135, 206, 235);
    private static final Color SLATEBLUE = new Color(106, 90, 205);
    private static final Color SLATEGRAY = new Color(112, 128, 144);
    private static final Color SNOW = new Color(255, 250, 250);
    private static final Color SPRINGGREEN = new Color(0, 255, 127);
    private static final Color STEELBLUE = new Color(70, 130, 180);
    private static final Color TAN = new Color(210, 180, 140);
    private static final Color TEAL = new Color(0, 128, 128);
    private static final Color THISTLE = new Color(216, 191, 216);
    private static final Color TOMATO = new Color(255, 99, 71);
    private static final Color TURQUOISE = new Color(64, 224, 208);
    private static final Color VIOLET = new Color(238, 130, 238);
    private static final Color WHEAT = new Color(245, 222, 179);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color WHITESMOKE = new Color(245, 245, 245);
    private static final Color YELLOW = new Color(255, 255, 0);
    private static final Color YELLOWGREEN = new Color(154, 205, 50);

    static {
        colorMap.put("&0", BLACK);
        colorMap.put("&1", MCCOLOR1);
        colorMap.put("&2", MCCOLOR2);
        colorMap.put("&3", MCCOLOR3);
        colorMap.put("&4", MCCOLOR4);
        colorMap.put("&5", MCCOLOR5);
        colorMap.put("&6", MCCOLOR6);
        colorMap.put("&7", MCCOLOR7);
        colorMap.put("&8", MCCOLOR8);
        colorMap.put("&9", MCCOLOR9);
        colorMap.put("&a", MCCOLORA);
        colorMap.put("&b", MCCOLORB);
        colorMap.put("&c", MCCOLORC);
        colorMap.put("&d", MCCOLORD);
        colorMap.put("&e", MCCOLORE);
        colorMap.put("&f", WHITE);

        colorMap.put("aliceblue", ALICEBLUE);
        colorMap.put("antiquewhite", ANTIQUEWHITE);
        colorMap.put("aqua", AQUA);
        colorMap.put("aquamarine", AQUAMARINE);
        colorMap.put("azure", AZURE);
        colorMap.put("beige", BEIGE);
        colorMap.put("bisque", BISQUE);
        colorMap.put("black", BLACK);
        colorMap.put("blanchedalmond", BLANCHEDALMOND);
        colorMap.put("blue", BLUE);
        colorMap.put("blueviolet", BLUEVIOLET);
        colorMap.put("brown", BROWN);
        colorMap.put("burlywood", BURLYWOOD);
        colorMap.put("cadetblue", CADETBLUE);
        colorMap.put("chartreuse", CHARTREUSE);
        colorMap.put("chocolate", CHOCOLATE);
        colorMap.put("coral", CORAL);
        colorMap.put("cornflowerblue", CORNFLOWERBLUE);
        colorMap.put("cornsilk", CORNSILK);
        colorMap.put("crimson", CRIMSON);
        colorMap.put("cyan", CYAN);
        colorMap.put("darkblue", DARKBLUE);
        colorMap.put("darkcyan", DARKCYAN);
        colorMap.put("darkgoldenrod", DARKGOLDENROD);
        colorMap.put("darkgray", DARKGRAY);
        colorMap.put("darkgreen", DARKGREEN);
        colorMap.put("darkgrey", DARKGRAY);
        colorMap.put("darkkhaki", DARKKHAKI);
        colorMap.put("darkmagenta", DARKMAGENTA);
        colorMap.put("darkolivegreen", DARKOLIVEGREEN);
        colorMap.put("darkorange", DARKORANGE);
        colorMap.put("darkorchid", DARKORCHID);
        colorMap.put("darkred", DARKRED);
        colorMap.put("darksalmon", DARKSALMON);
        colorMap.put("darkseagreen", DARKSEAGREEN);
        colorMap.put("darkslateblue", DARKSLATEBLUE);
        colorMap.put("darkslategray", DARKSLATEGRAY);
        colorMap.put("darkslategrey", DARKSLATEGRAY);
        colorMap.put("darkturquoise", DARKTURQUOISE);
        colorMap.put("darkviolet", DARKVIOLET);
        colorMap.put("deeppink", DEEPPINK);
        colorMap.put("deepskyblue", DEEPSKYBLUE);
        colorMap.put("dimgray", DIMGRAY);
        colorMap.put("dimgrey", DIMGREY);
        colorMap.put("dodgerblue", DODGERBLUE);
        colorMap.put("firebrick", FIREBRICK);
        colorMap.put("floralwhite", FLORALWHITE);
        colorMap.put("forestgreen", FORESTGREEN);
        colorMap.put("fuchsia", FUCHSIA);
        colorMap.put("gainsboro", GAINSBORO);
        colorMap.put("ghostwhite", GHOSTWHITE);
        colorMap.put("gold", GOLD);
        colorMap.put("goldenrod", GOLDENROD);
        colorMap.put("gray", GRAY);
        colorMap.put("green", GREEN);
        colorMap.put("greenyellow", GREENYELLOW);
        colorMap.put("grey", GRAY);
        colorMap.put("honeydew", HONEYDEW);
        colorMap.put("hotpink", HOTPINK);
        colorMap.put("indianred", INDIANRED);
        colorMap.put("indigo", INDIGO);
        colorMap.put("ivory", IVORY);
        colorMap.put("khaki", KHAKI);
        colorMap.put("lavender", LAVENDER);
        colorMap.put("lavenderblush", LAVENDERBLUSH);
        colorMap.put("lawngreen", LAWNGREEN);
        colorMap.put("lemonchiffon", LEMONCHIFFON);
        colorMap.put("lightblue", LIGHTBLUE);
        colorMap.put("lightcoral", LIGHTCORAL);
        colorMap.put("lightcyan", LIGHTCYAN);
        colorMap.put("lightgoldenrodyellow", LIGHTGOLDENRODYELLOW);
        colorMap.put("lightgray", LIGHTGRAY);
        colorMap.put("lightgreen", LIGHTGREEN);
        colorMap.put("lightgrey", LIGHTGRAY);
        colorMap.put("lightpink", LIGHTPINK);
        colorMap.put("lightsalmon", LIGHTSALMON);
        colorMap.put("lightseagreen", LIGHTSEAGREEN);
        colorMap.put("lightskyblue", LIGHTSKYBLUE);
        colorMap.put("lightslategray", LIGHTSLATEGRAY);
        colorMap.put("lightslategrey", LIGHTSLATEGRAY);
        colorMap.put("lightsteelblue", LIGHTSTEELBLUE);
        colorMap.put("lightyellow", LIGHTYELLOW);
        colorMap.put("lime", LIME);
        colorMap.put("limegreen", LIMEGREEN);
        colorMap.put("linen", LINEN);
        colorMap.put("magenta", MAGENTA);
        colorMap.put("maroon", MAROON);
        colorMap.put("mediumaquamarine", MEDIUMAQUAMARINE);
        colorMap.put("mediumblue", MEDIUMBLUE);
        colorMap.put("mediumorchid", MEDIUMORCHID);
        colorMap.put("mediumpurple", MEDIUMPURPLE);
        colorMap.put("mediumseagreen", MEDIUMSEAGREEN);
        colorMap.put("mediumslateblue", MEDIUMSLATEBLUE);
        colorMap.put("mediumspringgreen", MEDIUMSPRINGGREEN);
        colorMap.put("mediumturquoise", MEDIUMTURQUOISE);
        colorMap.put("mediumvioletred", MEDIUMVIOLETRED);
        colorMap.put("midnightblue", MIDNIGHTBLUE);
        colorMap.put("mintcream", MINTCREAM);
        colorMap.put("mistyrose", MISTYROSE);
        colorMap.put("moccasin", MOCCASIN);
        colorMap.put("navajowhite", NAVAJOWHITE);
        colorMap.put("navy", NAVY);
        colorMap.put("oldlace", OLDLACE);
        colorMap.put("olive", OLIVE);
        colorMap.put("olivedrab", OLIVEDRAB);
        colorMap.put("orange", ORANGE);
        colorMap.put("orangered", ORANGERED);
        colorMap.put("orchid", ORCHID);
        colorMap.put("palegoldenrod", PALEGOLDENROD);
        colorMap.put("palegreen", PALEGREEN);
        colorMap.put("paleturquoise", PALETURQUOISE);
        colorMap.put("palevioletred", PALEVIOLETRED);
        colorMap.put("papayawhip", PAPAYAWHIP);
        colorMap.put("peachpuff", PEACHPUFF);
        colorMap.put("peru", PERU);
        colorMap.put("pink", PINK);
        colorMap.put("plum", PLUM);
        colorMap.put("powderblue", POWDERBLUE);
        colorMap.put("purple", PURPLE);
        colorMap.put("red", RED);
        colorMap.put("rosybrown", ROSYBROWN);
        colorMap.put("royalblue", ROYALBLUE);
        colorMap.put("saddlebrown", SADDLEBROWN);
        colorMap.put("salmon", SALMON);
        colorMap.put("sandybrown", SANDYBROWN);
        colorMap.put("seagreen", SEAGREEN);
        colorMap.put("seashell", SEASHELL);
        colorMap.put("sienna", SIENNA);
        colorMap.put("silver", SILVER);
        colorMap.put("skyblue", SKYBLUE);
        colorMap.put("slateblue", SLATEBLUE);
        colorMap.put("slategray", SLATEGRAY);
        colorMap.put("slategrey", SLATEGRAY);
        colorMap.put("snow", SNOW);
        colorMap.put("springgreen", SPRINGGREEN);
        colorMap.put("steelblue", STEELBLUE);
        colorMap.put("tan", TAN);
        colorMap.put("teal", TEAL);
        colorMap.put("thistle", THISTLE);
        colorMap.put("tomato", TOMATO);
        colorMap.put("turquoise", TURQUOISE);
        colorMap.put("violet", VIOLET);
        colorMap.put("wheat", WHEAT);
        colorMap.put("white", WHITE);
        colorMap.put("whitesmoke", WHITESMOKE);
        colorMap.put("yellow", YELLOW);
        colorMap.put("yellowgreen", YELLOWGREEN);
    }

    public static Color getColor(String value) {
        try {
            if (value != null && !"map".equals(value) && !value.isEmpty()) {
                if (PATTERN_COLOR.matcher(value).matches()) {
                    return Color.decode(value);
                } else {
                    return colorMap.get(value);
                }
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }
}
