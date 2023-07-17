package com.example.mytools;

public class CheckTools {

    private static final String usernamePettern = "[a-zA-Z0-9_]+";
    private static final String emailPettern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    // 判断用户名是否合法
    public static Boolean checkUsername(String username) {
        // 判断长度是否合法
        if (username.length() < 6) return false;
        else return username.matches(usernamePettern) || username.matches(emailPettern);
    }
}
