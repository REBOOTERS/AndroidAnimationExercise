package com.snatik.matches.common;

import android.content.Context;
import android.content.SharedPreferences;

public class Memory {

    private static final String SHARED_PREFERENCES_NAME = "com.snatik.matches";
    private static String highStartKey = "theme_%d_difficulty_%d";
    private static String bestTimeKey = "themetime_%d_difficultytime_%d";

    public static void save(int theme, int difficulty, int stars) {

        int highStars = getHighStars(theme, difficulty);

        if (stars > highStars) {
            SharedPreferences sharedPreferences = Shared.context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            String key = String.format(highStartKey, theme, difficulty);
            edit.putInt(key, stars).commit();
        }
    }

    public static void saveTime(int theme, int difficulty, int passedSecs) {

        int bestTime = getBestTime(theme, difficulty);

        if (passedSecs < bestTime || bestTime == -1) {
            SharedPreferences sharedPreferences = Shared.context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String timeKey = String.format(bestTimeKey, theme, difficulty);
            editor.putInt(timeKey, passedSecs);
            editor.commit();
        }
    }

    public static int getHighStars(int theme, int difficulty) {

        SharedPreferences sharedPreferences = Shared.context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        String key = String.format(highStartKey, theme, difficulty);
        return sharedPreferences.getInt(key, 0);
    }

    public static int getBestTime(int theme, int difficulty) {

        SharedPreferences sharedPreferences = Shared.context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String key = String.format(bestTimeKey, theme, difficulty);
        return sharedPreferences.getInt(key, -1);
    }

}
