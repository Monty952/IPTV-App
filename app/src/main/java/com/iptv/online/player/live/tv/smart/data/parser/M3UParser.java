package com.iptv.online.player.live.tv.smart.data.parser;

import com.iptv.online.player.live.tv.smart.data.model.Category;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.utils.CountryUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* loaded from: classes5.dex */

// New Code
public class M3UParser {

    private static final Pattern TVG_ID_PATTERN =
            Pattern.compile("tvg-id=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern TVG_NAME_PATTERN =
            Pattern.compile("tvg-name=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern TVG_LOGO_PATTERN =
            Pattern.compile("tvg-logo=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern TVG_COUNTRY_PATTERN =
            Pattern.compile("tvg-country=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern GROUP_TITLE_PATTERN =
            Pattern.compile("group-title=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern USER_AGENT_PATTERN =
            Pattern.compile("user-agent=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern REFERRER_PATTERN =
            Pattern.compile("(?:http-referrer|http-referer|referer|referrer)=\"([^\"]*)\"",
                    Pattern.CASE_INSENSITIVE);
    private static final Pattern JSON_USER_AGENT_PATTERN =
            Pattern.compile("\"user-agent\"\\s*:\\s*\"([^\"]*)\"",
                    Pattern.CASE_INSENSITIVE);
    private static final Pattern JSON_REFERRER_PATTERN =
            Pattern.compile("\"(?:referer|referrer)\"\\s*:\\s*\"([^\"]*)\"",
                    Pattern.CASE_INSENSITIVE);


    public static class ParseResult {
        public List<PlaylistItem> channels = new ArrayList<>();
        public List<Category> categories = new ArrayList<>();
    }


    public static ParseResult parse(InputStream inputStream) throws Exception {

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(inputStream);

        bufferedInputStream.mark(4);

        byte[] bytes = new byte[4];
        int read = bufferedInputStream.read(bytes);

        bufferedInputStream.reset();

        // Check whether the input is a ZIP file.
        boolean isZip = read >= 4
                && bytes[0] == 80
                && bytes[1] == 75
                && (bytes[2] == 3 || bytes[2] == 5 || bytes[2] == 7);

        if (isZip) {

            ZipInputStream zipInputStream =
                    new ZipInputStream(bufferedInputStream);

            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {

                if (entry.isDirectory()) {
                    continue;
                }

                String fileName = entry.getName().toLowerCase(Locale.ROOT);

                if (fileName.endsWith(".m3u")
                        || fileName.endsWith(".m3u8")
                        || fileName.endsWith(".txt")) {

                    // Parse the selected ZIP entry before moving to another entry.
                    return parseReader(
                            new BufferedReader(
                                    new InputStreamReader(
                                            zipInputStream,
                                            StandardCharsets.UTF_8
                                    )
                            )
                    );
                }
            }

            // No supported playlist file was found inside the ZIP.
            return new ParseResult();
        }

        return parseReader(
                new BufferedReader(
                        new InputStreamReader(
                                bufferedInputStream,
                                StandardCharsets.UTF_8
                        )
                )
        );
    }


    public static ParseResult parse(String str) throws Exception {
        return parseReader(new BufferedReader(new StringReader(str)));
    }


    private static ParseResult parseReader(BufferedReader reader) throws Exception {

        ParseResult result = new ParseResult();

        // Keeps categories in the order in which they appear in the playlist.
        Map<String, Category> categoryMap = new LinkedHashMap<>();

        String line;

        String tvgId = null;
        String tvgName = null;
        String logoUrl = null;
        String tvgCountry = null;
        String groupTitle = null;
        String channelName = null;
        String userAgent = null;
        String referrer = null;

        int channelNumber = 0;

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            // Read channel information.
            if (line.startsWith("#EXTINF:")) {

                int commaIndex = line.indexOf(',');

                String attributes = commaIndex >= 0
                        ? line.substring(0, commaIndex)
                        : line;

                channelName = commaIndex >= 0
                        ? line.substring(commaIndex + 1).trim()
                        : null;

                tvgId = extractAttribute(TVG_ID_PATTERN, attributes);
                tvgName = extractAttribute(TVG_NAME_PATTERN, attributes);
                logoUrl = extractAttribute(TVG_LOGO_PATTERN, attributes);
                tvgCountry = extractAttribute(TVG_COUNTRY_PATTERN, attributes);
                groupTitle = extractAttribute(GROUP_TITLE_PATTERN, attributes);

                userAgent = extractAttribute(USER_AGENT_PATTERN, attributes);
                referrer = extractAttribute(REFERRER_PATTERN, attributes);

                continue;
            }

            // Read VLC-specific options.
            if (line.startsWith("#EXTVLCOPT:")) {

                String option = line.substring("#EXTVLCOPT:".length()).trim();

                String lowerOption = option.toLowerCase(Locale.ROOT);

                if (lowerOption.startsWith("http-user-agent=")) {
                    userAgent = option.substring(option.indexOf('=') + 1).trim();
                } else if (lowerOption.startsWith("http-referrer=")
                        || lowerOption.startsWith("http-referer=")) {
                    referrer = option.substring(option.indexOf('=') + 1).trim();
                }

                continue;
            }

            // Some playlists store request headers in JSON format.
            if (line.startsWith("#EXTHTTP:")) {

                String headers = line.substring("#EXTHTTP:".length()).trim();

                String jsonUserAgent =
                        extractAttribute(JSON_USER_AGENT_PATTERN, headers);

                String jsonReferrer =
                        extractAttribute(JSON_REFERRER_PATTERN, headers);

                if (jsonUserAgent != null) {
                    userAgent = jsonUserAgent;
                }

                if (jsonReferrer != null) {
                    referrer = jsonReferrer;
                }

                continue;
            }

            // Alternative group declaration.
            if (line.startsWith("#EXTGRP:")) {
                groupTitle = line.substring("#EXTGRP:".length()).trim();
                continue;
            }

            // Ignore other M3U comments and directives.
            if (line.startsWith("#")) {
                continue;
            }

            // A non-comment line may be a channel stream URL.
            String lowerLine = line.toLowerCase(Locale.ROOT);

            boolean isStreamUrl =
                    line.contains("://")
                            || line.startsWith("/")
                            || lowerLine.contains(".m3u8")
                            || lowerLine.contains(".mp4")
                            || lowerLine.contains(".ts");

            if (!isStreamUrl) {
                continue;
            }

            channelNumber++;

            if (channelName == null || channelName.trim().isEmpty()) {
                channelName = "Channel " + channelNumber;
            }

            if (groupTitle == null || groupTitle.trim().isEmpty()) {
                groupTitle = "General";
            }

            // Create the channel.
            PlaylistItem channel = new PlaylistItem();

            channel.setId(
                    "ch_" + channelNumber + "_" + Math.abs(line.hashCode())
            );

            channel.setName(channelName);
            channel.setStreamUrl(line);
            channel.setLogoUrl(logoUrl);
            channel.setGroupTitle(groupTitle);

            channel.setTvgId(tvgId);
            channel.setTvgName(tvgName);
            channel.setUserAgent(userAgent);
            channel.setReferrer(referrer);

            // Determine country information.
            String countryCode = CountryUtils.extractCountryCode(
                    tvgCountry,
                    tvgId,
                    groupTitle
            );

            channel.setCountryCode(countryCode);

            if (countryCode != null && !countryCode.trim().isEmpty()) {
                channel.setCountryName(
                        CountryUtils.getCountryNameFromCode(countryCode)
                );
            }

            result.channels.add(channel);

            // Group the channel into its category.
            String categoryKey = groupTitle.toLowerCase(Locale.ROOT);

            Category category = categoryMap.get(categoryKey);

            if (category == null) {
                category = new Category(groupTitle, groupTitle);

                categoryMap.put(categoryKey, category);
                result.categories.add(category);
            }

            category.addChannel(channel);

            // Reset channel-specific information for the next entry.
            tvgId = null;
            tvgName = null;
            logoUrl = null;
            tvgCountry = null;
            groupTitle = null;
            channelName = null;
            userAgent = null;
            referrer = null;
        }

        return result;
    }
    private static String extractAttribute(Pattern pattern, String str) {
        if (str == null) {
            return null;
        }
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}


// Old Code
//public class M3UParser {
//    private static final Pattern TVG_ID_PATTERN = Pattern.compile("tvg-id=\"([^\"]*)\"", 2);
//    private static final Pattern TVG_NAME_PATTERN = Pattern.compile("tvg-name=\"([^\"]*)\"", 2);
//    private static final Pattern TVG_LOGO_PATTERN = Pattern.compile("tvg-logo=\"([^\"]*)\"", 2);
//    private static final Pattern TVG_COUNTRY_PATTERN = Pattern.compile("tvg-country=\"([^\"]*)\"", 2);
//    private static final Pattern GROUP_TITLE_PATTERN = Pattern.compile("group-title=\"([^\"]*)\"", 2);
//    private static final Pattern USER_AGENT_PATTERN = Pattern.compile("user-agent=\"([^\"]*)\"", 2);
//
//    /* loaded from: classes5.dex */
//    public static class ParseResult {
//        public List<PlaylistItem> channels = new ArrayList();
//        public List<Category> categories = new ArrayList();
//    }
//
//    public static ParseResult parse(InputStream inputStream) throws Exception {
//        byte b;
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//        bufferedInputStream.mark(4);
//        byte[] bArr = new byte[4];
//        int read = bufferedInputStream.read(bArr);
//        bufferedInputStream.reset();
//        if (read >= 4 && bArr[0] == 80 && bArr[1] == 75 && ((b = bArr[2]) == 3 || b == 5 || b == 7)) {
//            ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);
//            while (true) {
//                ZipEntry nextEntry = zipInputStream.getNextEntry();
//                if (nextEntry == null) {
//                    break;
//                }
//                String lowerCase = nextEntry.getName().toLowerCase();
//                if (nextEntry.isDirectory() || (!lowerCase.endsWith(".m3u") && !lowerCase.endsWith(".m3u8") && !lowerCase.endsWith(".txt"))) {
//                }
//            }
//            return parseReader(new BufferedReader(new InputStreamReader(zipInputStream, StandardCharsets.UTF_8)));
//        }
//        return parseReader(new BufferedReader(new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8)));
//    }
//
//    public static ParseResult parse(String str) throws Exception {
//        return parseReader(new BufferedReader(new StringReader(str)));
//    }
//
//    /* JADX WARN: Code restructure failed: missing block: B:40:0x00ea, code lost:
//        r14 = r14.trim();
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:41:0x00f4, code lost:
//        if (r14.contains("://") != false) goto L37;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:43:0x00fc, code lost:
//        if (r14.startsWith(com.google.firebase.sessions.settings."/") != false) goto L37;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:45:0x0104, code lost:
//        if (r14.contains(".m3u8") != false) goto L37;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:47:0x010c, code lost:
//        if (r14.contains(".mp4") != false) goto L37;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:49:0x0114, code lost:
//        if (r14.contains(".ts") == false) goto L35;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:50:0x0116, code lost:
//        if (r11 == null) goto L49;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:52:0x011c, code lost:
//        if (r11.isEmpty() == false) goto L40;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:53:0x011e, code lost:
//        r11 = "Channel " + r5;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:54:0x012b, code lost:
//        if (r10 == null) goto L48;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:56:0x0131, code lost:
//        if (r10.isEmpty() == false) goto L43;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:57:0x0133, code lost:
//        r10 = "General";
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:58:0x0134, code lost:
//        r2 = new com.iptv.online.player.live.tv.smart.data.model.PlaylistItem();
//        r2.setId("ch_" + r5 + "_" + java.lang.Math.abs(r14.hashCode()));
//        r2.setName(r11);
//        r2.setStreamUrl(r14);
//        r2.setLogoUrl(r8);
//        r2.setGroupTitle(r10);
//        r2.setTvgId(r6);
//        r2.setTvgName(r7);
//        r2.setUserAgent(r12);
//        r2.setReferrer(r13);
//        r4 = com.iptv.online.player.live.tv.smart.utils.CountryUtils.extractCountryCode(r9, r6, r10);
//        r2.setCountryCode(r4);
//        r2.setCountryName(com.iptv.online.player.live.tv.smart.utils.CountryUtils.getCountryNameFromCode(r4));
//        r0.channels.add(r2);
//        r4 = r10.toLowerCase();
//        r6 = (com.iptv.online.player.live.tv.smart.data.model.Category) r1.get(r4);
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:59:0x0192, code lost:
//        if (r6 != null) goto L46;
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:60:0x0194, code lost:
//        r6 = new com.iptv.online.player.live.tv.smart.data.model.Category(r10, r10);
//        r1.put(r4, r6);
//        r0.categories.add(r6);
//     */
//    /* JADX WARN: Code restructure failed: missing block: B:61:0x01a1, code lost:
//        r6.addChannel(r2);
//        r5 = r5 + 1;
//     */
//    /*
//        Code decompiled incorrectly, please refer to instructions dump.
//        To view partially-correct code enable 'Show inconsistent code' option in preferences
//    */
//    private static com.iptv.online.player.live.tv.smart.data.parser.M3UParser.ParseResult parseReader(java.io.BufferedReader r16) throws java.lang.Exception {
//        /*
//            Method dump skipped, instructions count: 430
//            To view this dump change 'Code comments level' option to 'DEBUG'
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.iptv.online.player.live.tv.smart.data.parser.M3UParser.parseReader(java.io.BufferedReader):com.iptv.online.player.live.tv.smart.data.parser.M3UParser$ParseResult");
//    }
//
//    private static String extractAttribute(Pattern pattern, String str) {
//        Matcher matcher = pattern.matcher(str);
//        if (matcher.find()) {
//            return matcher.group(1).trim();
//        }
//        return null;
//    }
//}
