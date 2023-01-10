package com.annimon.ffmpegbot;

import java.text.DecimalFormat;

public class TextUtils {

    public static String safeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">" ,"&gt;");
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[] { "Bi", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#")
                .format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
