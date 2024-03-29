package com.ecnu.meethere.user.utils;

public class UsernamePasswordValidUtils {
    public static boolean isValidUsername(String username) {
        if (username == null) return false;
        int[] codePoints = username.codePoints().toArray();
        if (codePoints.length > 16)
            return false;
        int chCnt = 0;
        for (int codePoint : codePoints)
            if (codePoint >= 0x4e00 && codePoint <= 0x9f65)
                chCnt += 2;
            else if (Character.isDigit(codePoint) || Character.isAlphabetic(codePoint) || codePoint == '_')
                ++chCnt;
            else
                return false;
        return chCnt > 0 && chCnt <= 16;
    }

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        int[] codePoints = password.codePoints().toArray();
        if (codePoints.length > 16 || codePoints.length < 8)
            return false;
        for (int codePoint : codePoints)
            if (Character.isWhitespace(codePoint))
                return false;
            else if (codePoint >= 0x7f || codePoint <= 0x20)
                return false;
        return true;
    }

}
