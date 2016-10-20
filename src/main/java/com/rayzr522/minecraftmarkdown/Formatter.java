/**
 * 
 */
package com.rayzr522.minecraftmarkdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

/**
 * @author Rayzr
 *
 */
public class Formatter {

    private Pattern pattern;
    private String  colorCode;

    /**
     * @param character
     * @param colorCode
     */
    public Formatter(String character, String colorCode) {
        this.colorCode = color(colorCode);
        pattern = Pattern.compile(String.format("(\\%s([^\\%s]+)\\%s)+", character, character, character));
    }

    public String apply(String input) {

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String end = findLastFormatting(input, matcher.start());
            input = input.replace(matcher.group(1), colorCode + matcher.group(2) + end);
        }

        return input;

    }

    /**
     * @param str the input string
     * @param end the maximum position to check up to
     * @return The color and formatting combo that accurately represents the
     *         last formatting codes
     */
    public static String findLastFormatting(String str, int end) {

        str = str.substring(0, end);

        Matcher matcher = Pattern.compile(ChatColor.COLOR_CHAR + "([0-9a-f])").matcher(str);
        String color = ChatColor.RESET.toString();
        int pos = -1;
        while (matcher.find()) {
            color = matcher.group();
            pos = matcher.start();
        }

        String format = "";
        matcher = Pattern.compile(ChatColor.COLOR_CHAR + "([klmnor])").matcher(str);
        while (matcher.find()) {
            if (matcher.start() < pos) {
                continue;
            }
            format = matcher.group();
        }

        return color(color + format);
    }

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
