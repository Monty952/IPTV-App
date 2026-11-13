package com.iptv.online.player.live.tv.smart.utils;

//import io.appmetrica.analytics.coreutils.internal.StringUtils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes5.dex */
public class CountryUtils {
    private static final Pattern TVG_ID_COUNTRY_PATTERN = Pattern.compile("\\.([a-zA-Z]{2})(?:@[^\\s\"]*)?$");
    private static final Map<String, String> COUNTRY_NAME_TO_CODE = new HashMap();

    static {
        String[] iSOCountries;
        for (String str : Locale.getISOCountries()) {
            String displayCountry = new Locale("", str).getDisplayCountry(Locale.ENGLISH);
            if (displayCountry != null && !displayCountry.isEmpty()) {
                COUNTRY_NAME_TO_CODE.put(displayCountry.toLowerCase(Locale.ENGLISH), str.toUpperCase(Locale.ENGLISH));
            }
        }
        Map<String, String> map = COUNTRY_NAME_TO_CODE;
        map.put("usa", "US");
        map.put("united states of america", "US");
        map.put("uk", "GB");
        map.put("england", "GB");
        map.put("great britain", "GB");
        map.put("russia", "RU");
        map.put("south korea", "KR");
        map.put("uae", "AE");
    }

    public static String extractCountryCode(String str, String str2, String str3) {
        String str4;
        if (str != null && !str.trim().isEmpty()) {
            String trim = str.trim();
            if (trim.contains(";") || trim.contains(",")) {
                String[] split = trim.split("[;,]");
                if (split.length > 0) {
                    trim = split[0].trim();
                }
            }
            if (trim.length() == 2) {
                String upperCase = trim.toUpperCase(Locale.ENGLISH);
                return "UK".equals(upperCase) ? "GB" : upperCase;
            }
            String str5 = COUNTRY_NAME_TO_CODE.get(trim.toLowerCase(Locale.ENGLISH));
            if (str5 != null) {
                return str5;
            }
        }
        if (str2 != null && !str2.trim().isEmpty()) {
            Matcher matcher = TVG_ID_COUNTRY_PATTERN.matcher(str2.trim());
            if (matcher.find()) {
                String upperCase2 = matcher.group(1).toUpperCase(Locale.ENGLISH);
                return "UK".equals(upperCase2) ? "GB" : upperCase2;
            }
        }
        return (str3 == null || str3.trim().isEmpty() || (str4 = COUNTRY_NAME_TO_CODE.get(str3.trim().toLowerCase(Locale.ENGLISH))) == null) ? "" : str4;
    }

    public static String getCountryNameFromCode(String str) {
        if (str == null || str.trim().isEmpty()) {
            return "Other";
        }
        String upperCase = str.trim().toUpperCase(Locale.ENGLISH);
        if ("UK".equals(upperCase)) {
            upperCase = "GB";
        }
        if ("OTHER".equals(upperCase)) {
            return "Other";
        }
        try {
            String displayCountry = new Locale("", upperCase).getDisplayCountry(Locale.ENGLISH);
            if (displayCountry != null) {
                if (!displayCountry.trim().isEmpty()) {
                    return displayCountry;
                }
            }
        } catch (Exception unused) {
        }
        return upperCase;
    }

    public static String getCountryFlag(String countryCode) {
        // Return the globe symbol when the country code is missing.
        if (countryCode == null || countryCode.trim().isEmpty()
                || "OTHER".equalsIgnoreCase(countryCode.trim())) {
            return "🌐";
        }
        String upperCase = countryCode.trim().toUpperCase(Locale.ENGLISH);
        // Convert UK to the official ISO country code for the United Kingdom.
        if ("UK".equals(upperCase)) {
            upperCase = "GB";
        }
        // A valid country code must contain exactly two letters.
        if (upperCase.length() != 2) {
            return "🌐";
        }
        char first = upperCase.charAt(0);
        char second = upperCase.charAt(1);

        // Make sure both characters are English letters.
        if (first < 'A' || first > 'Z'
                || second < 'A' || second > 'Z') {
            return "🌐";
        }
        // Unicode starting code point for regional indicator A.
        int flagBase = 0x1F1E6;
        // Convert each country letter into its regional indicator symbol.
        int firstCodePoint = flagBase + (first - 'A');
        int secondCodePoint = flagBase + (second - 'A');

        return new String(Character.toChars(firstCodePoint))
                + new String(Character.toChars(secondCodePoint));
    }

  /*  public static String getCountryFlag(String str) {
        if (str != null && !str.trim().isEmpty() && !"OTHER".equalsIgnoreCase(str)) {
            String upperCase = str.trim().toUpperCase(Locale.ENGLISH);
            if ("UK".equals(upperCase)) {
                upperCase = "GB";
            }
            if (upperCase.length() == 2) {
                char charAt = upperCase.charAt(0);
                char charAt2 = upperCase.charAt(1);
                if (charAt >= 'A' && charAt <= 'Z' && charAt2 >= 'A' && charAt2 <= 'Z') {
                    return new String(Character.toChars(charAt - 3675)).concat(new String(Character.toChars(charAt2 - 3675)));
                }
            }
        }
        return "🌐";
    }*/
}
