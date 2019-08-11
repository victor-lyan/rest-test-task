package com.example.resttesttask.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginCache {
    
    public static Map<String, Boolean> loginTriesExpiration = new ConcurrentHashMap<>();
}
