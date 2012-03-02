/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.liquidroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import de.quadrillenschule.liquidroid.model.Area;
import de.quadrillenschule.liquidroid.model.LQFBInstance;
import de.quadrillenschule.liquidroid.model.LQFBInstances;
import de.quadrillenschule.liquidroid.tools.CrashLog;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author andi
 */
public class LiqoidApplication extends Application {

    public LQFBInstances lqfbInstances;
    ArrayList<LQFBInstanceChangeListener> lqfbInstanceChangeListeners;

    public LiqoidApplication() {
        super();

    }

    @Override
    public void onCreate() {
        Thread.setDefaultUncaughtExceptionHandler(new CrashLog(new File(getExternalFilesDir(null), "liqoid.log")));

        lqfbInstances = new LQFBInstances(this);
        lqfbInstanceChangeListeners = new ArrayList<LQFBInstanceChangeListener>();


    }

    public void saveSelectedAreasToPrefs(){
        for (LQFBInstance lin : lqfbInstances) {
            SharedPreferences prefs = getSharedPreferences(lin.getPrefsName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            String selectedareas = "";
            for (Area a : lin.areas) {
                if (a.isSelected()) {
                    selectedareas += a.getId() + ":";
                }
            }
            editor.putString("selectedareas", selectedareas);
            editor.commit();
        }
       
    }
     public void loadSelectedAreasFromPrefs(){
        for (LQFBInstance lin : lqfbInstances) {
            SharedPreferences prefs = getSharedPreferences(lin.getPrefsName(), MODE_PRIVATE);
            String[] selectedareas_str = prefs.getString("selectedareas", "0").split(":",0);
            ArrayList<Integer> selectedAreas = new ArrayList<Integer>();
            for (String s : selectedareas_str) {
                try {
                    selectedAreas.add(Integer.parseInt(s));
                } catch (Exception e) {
                }
            }
            for (Integer i : selectedAreas) {
                try {
                    lin.areas.getById(i).setSelected(true);
                } catch (Exception e) {
                }
            }
        }
    }
    public void addLQFBInstancesChangeListener(LQFBInstanceChangeListener l) {
        lqfbInstanceChangeListeners.add(l);
    }

    public void removeLQFBInstancesChangeListener(LQFBInstanceChangeListener l) {
        lqfbInstanceChangeListeners.remove(l);
    }

    void fireLQFBInstanceChangedEvent() {
        for (LQFBInstanceChangeListener l : lqfbInstanceChangeListeners) {
            l.lqfbInstanceChanged();

        }
    }

    public void toast(Context context, CharSequence charseq) {
        Toast mytoast = Toast.makeText(context, charseq, Toast.LENGTH_LONG);
        mytoast.show();
    }
}
