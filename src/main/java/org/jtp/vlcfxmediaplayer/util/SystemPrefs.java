/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jtp.vlcfxmediaplayer.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Dub
 */
public class SystemPrefs {
    private static SystemPrefs instance = new SystemPrefs();
    
    private static Preferences prefs = Preferences.userNodeForPackage(SystemPrefs.class);
    
    private static final String lastDir = "lastDirectory";
    
    public String getLastUsedDirectory(){
        if(prefs.get(lastDir, null) != null){
            return prefs.get(lastDir, null);
        }
        return null;
    }
    public void setLastUsedDirectory(String dir){
        prefs.put(lastDir, dir);
    }

    public static SystemPrefs getInstance() {        
        return instance;
    }
    
    
}
