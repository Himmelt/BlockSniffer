package org.soraworld.sniffer.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
public class I19n {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Pattern FORMAT = Pattern.compile("((?<!&)&[0-9a-fk-or])+");

    private static Style parseStyle(@Nonnull String text) {
        Style style = new Style();
        int length = text.length();
        for (int i = 1; i < length; i += 2) {
            switch (text.charAt(i)) {
                case '0':
                    style.setColor(TextFormatting.BLACK);
                    break;
                case '1':
                    style.setColor(TextFormatting.DARK_BLUE);
                    break;
                case '2':
                    style.setColor(TextFormatting.DARK_GREEN);
                    break;
                case '3':
                    style.setColor(TextFormatting.DARK_AQUA);
                    break;
                case '4':
                    style.setColor(TextFormatting.DARK_RED);
                    break;
                case '5':
                    style.setColor(TextFormatting.DARK_PURPLE);
                    break;
                case '6':
                    style.setColor(TextFormatting.GOLD);
                    break;
                case '7':
                    style.setColor(TextFormatting.GRAY);
                    break;
                case '8':
                    style.setColor(TextFormatting.DARK_GRAY);
                    break;
                case '9':
                    style.setColor(TextFormatting.BLUE);
                    break;
                case 'a':
                    style.setColor(TextFormatting.GREEN);
                    break;
                case 'b':
                    style.setColor(TextFormatting.AQUA);
                    break;
                case 'c':
                    style.setColor(TextFormatting.RED);
                    break;
                case 'd':
                    style.setColor(TextFormatting.LIGHT_PURPLE);
                    break;
                case 'e':
                    style.setColor(TextFormatting.YELLOW);
                    break;
                case 'f':
                    style.setColor(TextFormatting.WHITE);
                    break;
                case 'k':
                    style.setObfuscated(true);
                    break;
                case 'l':
                    style.setBold(true);
                    break;
                case 'm':
                    style.setStrikethrough(true);
                    break;
                case 'n':
                    style.setUnderlined(true);
                    break;
                case 'o':
                    style.setItalic(true);
                    break;
                default:
                    style = new Style();
            }
        }
        return style;
    }

    private static ITextComponent format(String text) {
        Matcher matcher = FORMAT.matcher(text);
        ITextComponent component = new TextComponentString("");
        int head = 0;
        Style style = new Style();
        while (matcher.find()) {
            component.appendSibling(new TextComponentString(text.substring(head, matcher.start()).replaceAll("&&", "&")).setStyle(style));
            style = parseStyle(matcher.group());
            head = matcher.end();
        }
        component.appendSibling(new TextComponentString(text.substring(head).replaceAll("&&", "&")).setStyle(style));
        return component;
    }

    public static ITextComponent formatKey(String key, Object... args) {
        return format(I18n.format(key, args));
    }

    public static void sendChat(String key, Object... args) {
        mc.player.sendMessage(I19n.format(I18n.format("sf.chat.head") + I18n.format(key, args)));
    }

}
