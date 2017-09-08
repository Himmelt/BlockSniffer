package org.soraworld.sniffer.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
public class I18n {

    private static final Pattern FORMAT = Pattern.compile("(&[0-9a-fk-or]|&&){1,}");

    public ITextComponent format(String key, Object... args) {
        String text = net.minecraft.client.resources.I18n.format(key, args);
        TextComponentString components = new TextComponentString("");
        // & 表示转义 && 表示 & 如果&单独使用,直接忽略 0123456789abcdefklmnor
        // (&[0-9a-fk-or]|\&\&){1,}
        Matcher matcher = FORMAT.matcher(text);
        //String text = "&a&b&c&l你好&r&&&j&5&ruby&&&ko&o&r&&ghijawdi&&dalndk&dalkn&h&Hd";
        //Matcher matcher = FORMAT.matcher(text);
        int last = 0;
        while (matcher.find()) {
            System.out.println(text.substring(last, matcher.start()));
            System.out.println(matcher.group());
            last = matcher.end();
        }
        System.out.println(text.substring(last, text.length()));
        return components;
    }
}
