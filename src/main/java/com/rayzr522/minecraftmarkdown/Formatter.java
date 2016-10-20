/**
 * 
 */
package com.rayzr522.minecraftmarkdown;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Rayzr
 *
 */
public class Formatter {

    public static final String   INVALID_STRING   = "ERR_INVALID";

    private static final Pattern FORMATTING_CODES = Pattern.compile(ChatColor.COLOR_CHAR + "([klmnor])");
    private static final Pattern COLOR_CODES      = Pattern.compile(ChatColor.COLOR_CHAR + "([0-9a-f])");

    private static final String  BASE_PATTERN     = "({0}([^{0}]+){0})+";

    private Pattern              pattern;
    private String               start;
    private String               end;

    /**
     * @param character
     * @param start
     * @param end
     */
    public Formatter(String pattern, String start, String end) {
        Objects.requireNonNull(pattern, "`pattern` for formatter cannot be null!");
        Objects.requireNonNull(start, "`start` for formatter cannot be null!");

        this.start = color(start);
        this.end = color(end);
        this.pattern = Pattern.compile(BASE_PATTERN.replace("{0}", Pattern.quote(pattern)));
    }

    /**
     * @param character
     * @param start
     */
    public Formatter(String character, String start) {
        this(character, start, "");
    }

    /**
     * @param configurationSection
     */
    public Formatter(ConfigurationSection cs) {
        this(cs.getString("pattern", null), cs.getString("start", null), cs.getString("end", ""));
    }

    public String apply(String input) {

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String reset = findLastFormatting(input, matcher.start());
            input = input.replace(matcher.group(1), start + matcher.group(2) + end + reset);
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

        Matcher matcher = COLOR_CODES.matcher(str);
        String color = ChatColor.RESET.toString();
        int pos = -1;
        while (matcher.find()) {
            color = matcher.group();
            pos = matcher.start();
        }

        String format = "";
        matcher = FORMATTING_CODES.matcher(str);
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
