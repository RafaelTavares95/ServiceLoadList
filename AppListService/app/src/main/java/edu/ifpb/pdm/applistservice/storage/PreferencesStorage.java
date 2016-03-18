package edu.ifpb.pdm.applistservice.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rafael on 17/03/2016.
 *
 * Classe pra criar um deposito de preferencias compartilhadas
 */
public class PreferencesStorage {
    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public PreferencesStorage(Context context){
        settings =  context.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
    }

    public void saveHash(String hash){
        editor.putString("hash", hash);
        editor.commit();
    }

    public void saveOffSet(int off){
        editor.putInt("offset", off);
        editor.commit();
    }

    public String getHash(){
        return settings.getString("hash", "");
    }

    public int getOffset(){
        return settings.getInt("offset", 0);
    }
}
