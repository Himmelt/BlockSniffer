package org.soraworld.sniffer.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
public class I19n {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Pattern FORMAT = Pattern.compile("((?<!&)&[0-9a-fk-or])+");

    private static ChatStyle parseStyle(String text) {
        ChatStyle ChatStyle = new ChatStyle();
        int length = text.length();
        for (int i = 1; i < length; i += 2) {
            switch (text.charAt(i)) {
                case '0':
                    ChatStyle.setColor(EnumChatFormatting.BLACK);
                    break;
                case '1':
                    ChatStyle.setColor(EnumChatFormatting.DARK_BLUE);
                    break;
                case '2':
                    ChatStyle.setColor(EnumChatFormatting.DARK_GREEN);
                    break;
                case '3':
                    ChatStyle.setColor(EnumChatFormatting.DARK_AQUA);
                    break;
                case '4':
                    ChatStyle.setColor(EnumChatFormatting.DARK_RED);
                    break;
                case '5':
                    ChatStyle.setColor(EnumChatFormatting.DARK_PURPLE);
                    break;
                case '6':
                    ChatStyle.setColor(EnumChatFormatting.GOLD);
                    break;
                case '7':
                    ChatStyle.setColor(EnumChatFormatting.GRAY);
                    break;
                case '8':
                    ChatStyle.setColor(EnumChatFormatting.DARK_GRAY);
                    break;
                case '9':
                    ChatStyle.setColor(EnumChatFormatting.BLUE);
                    break;
                case 'a':
                    ChatStyle.setColor(EnumChatFormatting.GREEN);
                    break;
                case 'b':
                    ChatStyle.setColor(EnumChatFormatting.AQUA);
                    break;
                case 'c':
                    ChatStyle.setColor(EnumChatFormatting.RED);
                    break;
                case 'd':
                    ChatStyle.setColor(EnumChatFormatting.LIGHT_PURPLE);
                    break;
                case 'e':
                    ChatStyle.setColor(EnumChatFormatting.YELLOW);
                    break;
                case 'f':
                    ChatStyle.setColor(EnumChatFormatting.WHITE);
                    break;
                case 'k':
                    ChatStyle.setObfuscated(true);
                    break;
                case 'l':
                    ChatStyle.setBold(true);
                    break;
                case 'm':
                    ChatStyle.setStrikethrough(true);
                    break;
                case 'n':
                    ChatStyle.setUnderlined(true);
                    break;
                case 'o':
                    ChatStyle.setItalic(true);
                    break;
                default:
                    ChatStyle = new ChatStyle();
            }
        }
        return ChatStyle;
    }

    private static IChatComponent format(String text) {
        Matcher matcher = FORMAT.matcher(text);
        IChatComponent component = new ChatComponentText("");
        int head = 0;
        ChatStyle ChatStyle = new ChatStyle();
        while (matcher.find()) {
            component.appendSibling(new ChatComponentText(text.substring(head, matcher.start()).replaceAll("&&", "&")).setChatStyle(ChatStyle));
            ChatStyle = parseStyle(matcher.group());
            head = matcher.end();
        }
        component.appendSibling(new ChatComponentText(text.substring(head).replaceAll("&&", "&")).setChatStyle(ChatStyle));
        return component;
    }

    public static IChatComponent formatKey(String key, Object... args) {
        return format(I18n.format(key, args));
    }

    public static void sendChat(String key, Object... args) {
        mc.thePlayer.addChatMessage(I19n.format(I18n.format("sf.chat.head") + I18n.format(key, args)));
    }

    public static void sendChat2(String message) {
        mc.thePlayer.addChatMessage(I19n.format(I18n.format("sf.chat.head") + message));
    }
}
