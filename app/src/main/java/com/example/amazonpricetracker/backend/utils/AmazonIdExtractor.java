package com.example.amazonpricetracker.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmazonIdExtractor {

    private static final Pattern ASIN_PATTERN = Pattern.compile(
            "(?:/dp/|/gp/product/|/ASIN/|/asin/)([A-Z0-9]{10})"   // URL paths
                    + "|(?:[?&;](?:ASIN|asin)=)([A-Z0-9]{10})"           // Query params
                    + "|\\b([A-Z0-9]{10})\\b",                            // Standalone
                    Pattern.CASE_INSENSITIVE

    );

    /**
     * Extracts the ASIN from a given URL.
     *
     *
     */
    public String extractASIN(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        Matcher matcher = ASIN_PATTERN.matcher(input);

        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null) {
                    String asin = matcher.group(i);

                    if (isValidASINFormat(asin)) {
                        return asin;

                    }
                }
            }
        }
        return null;
    }

    private boolean isValidASINFormat(String asin) {
        if (asin == null || asin.trim().isEmpty()) {
            return false;
        }

        return asin.length() == 10 && asin.matches("[A-Z0-9]+$");
    }
}
