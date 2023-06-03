package com.v01d24.callinfo.registry;

import android.text.Html;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.v01d24.callinfo.PhoneNumberScore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CFPhoneNumberInfoParser {
    private static final Pattern scorePattern = Pattern.compile(
            "score (negative|neutral|positive)"
    );
    private static final Pattern extraInfoPattern = Pattern.compile(
            "<span[^>]*>([^<]+)</span>",
            Pattern.MULTILINE
    );
    private final String infoHtml;
    private List<String> info;

    public CFPhoneNumberInfoParser(@NonNull String infoHtml) {
        this.infoHtml = infoHtml;
    }

    public @Nullable PhoneNumberScore parseScore() {
        Matcher matcher = scorePattern.matcher(infoHtml);
        if (matcher.find()) {
            String rawScore = matcher.group(1);
            if (rawScore != null) {
                return PhoneNumberScore.valueOf(rawScore.toUpperCase());
            }
        }
        return null;
    }

    public @Nullable String parseSummary() {
        List<String> info = parseInfo();
        return info.size() > 0 ? info.get(0) : null;
    }

    private @NonNull List<String> parseInfo() {
        if (info != null) {
            return info;
        }
        info = new ArrayList<>();
        Matcher matcher = extraInfoPattern.matcher(infoHtml);
        while (matcher.find()) {
            info.add(Html.fromHtml(matcher.group(1)).toString());
        }
        return info;
    }
}
