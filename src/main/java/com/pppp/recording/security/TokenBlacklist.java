package com.pppp.recording.security;

import java.util.HashSet;
import java.util.Set;

public class TokenBlacklist {
    private static final Set<String> blacklist = new HashSet<>();

    // 토큰을 블랙리스트에 추가하는 메서드
    public static void addToBlacklist(String token) {
        blacklist.add(token);
        System.out.println(blacklist);
    }

    // 토큰이 블랙리스트에 있는지 확인하는 메서드
    public static boolean isBlacklisted(String token) {
        System.out.println(blacklist);
        return blacklist.contains(token);
    }
}

