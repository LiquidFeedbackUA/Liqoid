/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.liquidroid.gui;

import android.content.SharedPreferences;
import android.graphics.Color;
import de.quadrillenschule.liquidroid.InitiativesTabActivity;
import de.quadrillenschule.liquidroid.LiqoidApplication;
import de.quadrillenschule.liquidroid.model.Initiative;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author andi
 */
public class RecentEventsIssueItemView extends IssueItemView {

    public RecentEventsIssueItemView(InitiativesTabActivity activity, Initiative initiative) {
        super(activity, initiative);
    }

    @Override
    protected String getStatusText() {
        DateFormat formatter = new SimpleDateFormat(activity.getDateFormat());
        return " <b><font color=black> " + initiative.lastEvent() + "</font> - <font color=blue>" + formatter.format(initiative.dateForLastEvent()) + "</font></b>";
    }

   @Override
    protected long itemSpecificDelta() {
        return System.currentTimeMillis() - initiative.dateForLastEvent().getTime();
    }
    @Override
    protected int itemSpecificColorcode() {
        long delta = System.currentTimeMillis() - initiative.dateForLastEvent().getTime();
        long oneday = 1000 * 60 * 60 * 24;
        SharedPreferences gp = ((LiqoidApplication) activity.getApplication()).getGlobalPreferences();
        if (delta < Long.parseLong(gp.getString(LiqoidApplication.REDLIMIT_PREF, oneday + ""))) {
            return Color.argb(255, 255, 100, 100);
        }
        if (delta < Long.parseLong(gp.getString(LiqoidApplication.ORANGELIMIT_PREF, oneday * 3 + ""))) {
            return Color.argb(255, 255, 140, 100);
        }
        if (delta < Long.parseLong(gp.getString(LiqoidApplication.YELLOWLIMIT_PREF, oneday * 5 + ""))) {
            return Color.argb(255, 255, 255, 160);
        }
        return Color.LTGRAY;
    }
}
