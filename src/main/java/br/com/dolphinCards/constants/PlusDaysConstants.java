package br.com.dolphinCards.constants;

import java.util.HashMap;

public class PlusDaysConstants {
    public static final HashMap<String, Integer> TYPE = new HashMap<String, Integer>();

    static {
        TYPE.put("HARD", 3);
        TYPE.put("EASY", 7);
        TYPE.put("WRONG", 1);
    }
}
