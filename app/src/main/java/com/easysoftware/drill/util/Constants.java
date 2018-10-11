package com.easysoftware.drill.util;

public class Constants {
    public interface IntentExtra {
        String TEXT_LIST = "IntentExtra.text_list";
        String TYPE = "IntentExtra.type";
    }

    public interface TYPE {
        int IDIOM = 0;
        int POEM = 1;
    }

    public interface Level {
        String LEVEL_KEY = "level";
        int BASIC = 0;
        int INTERMEDIATE = 1;
        int ADVANCED = 2;
    }
}
