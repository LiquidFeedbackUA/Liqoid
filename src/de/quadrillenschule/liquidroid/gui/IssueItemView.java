/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.liquidroid.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Vibrator;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.quadrillenschule.liquidroid.InitiativesTabActivity;
import de.quadrillenschule.liquidroid.LiqoidApplication;
import de.quadrillenschule.liquidroid.model.Initiative;
import de.quadrillenschule.liquidroid.tools.Utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import de.quadrillenschule.liquidroid.R;
import de.quadrillenschule.liquidroid.model.LQFBInstance;

/**
 *
 * @author andi
 */
public class IssueItemView extends LinearLayout {

    InitiativesTabActivity activity;
    public Initiative initiative;
    CheckBox favStarCheckBox;
    ToggleButton issueButton;
    TextView statusLine, instanceView, areaView;
    LinearLayout expandView;
    ImageView colorView;
    RelativeLayout topLinesRelativeLayout;
    TextView colorIndicator;
    private boolean expandview = false;

    public IssueItemView(InitiativesTabActivity activity, Initiative initiative) {
        super(activity);
        this.activity = activity;
        this.initiative = initiative;



        colorIndicator = new Button(activity, null, android.R.attr.buttonStyleSmall);
        colorIndicator.setBackgroundColor(itemSpecificColorcode());
        colorIndicator.setTypeface(Typeface.MONOSPACE);
        colorIndicator.setTextSize(11);
        colorIndicator.setPadding(2, 2, 2, 2);
        expandButtonSetText();

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 0.1f);

        this.addView(colorIndicator, lp);

        RelativeLayout.LayoutParams rlp;
        topLinesRelativeLayout = new RelativeLayout(activity);
        topLinesRelativeLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        topLinesRelativeLayout.setPadding(0, 0, 0, 0);


        favStarCheckBox = new CheckBox(activity, null, android.R.attr.starStyle);
        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.addRule(RelativeLayout.ALIGN_LEFT);
        rlp.addRule(RelativeLayout.ALIGN_TOP);
        favStarCheckBox.setTextColor(Color.BLACK);
        //   favStarCheckBox.setText("   ");
        favStarCheckBox.setChecked(initiative.getArea().getInitiativen().isIssueSelected(initiative.issue_id));
        favStarCheckBox.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                onFavStarClick(arg0);
            }
        });
        favStarCheckBox.setId(4);

        topLinesRelativeLayout.addView(favStarCheckBox, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        statusLine = new TextView(activity);
        rlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_LEFT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.addRule(RelativeLayout.RIGHT_OF, favStarCheckBox.getId());
        statusLine.setText(Html.fromHtml(getStatusText()));
        statusLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        statusLine.setTextColor(Color.parseColor("#108020"));
        statusLine.setSingleLine();
        statusLine.setPadding(2, 2, 2, 2);
        statusLine.setId(2);
        topLinesRelativeLayout.addView(statusLine, rlp);

        instanceView = new TextView(activity);
        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.addRule(RelativeLayout.ALIGN_RIGHT);
        rlp.addRule(RelativeLayout.ALIGN_TOP);
        // rlp.addRule(RelativeLayout.RIGHT_OF, favStarCheckBox.getId());
        rlp.setMargins(0, 0, 5, 0);
        instanceView.setText(Html.fromHtml("<b><font color=black>" + initiative.getLqfbInstance().getShortName() + "</font></b>"));
        instanceView.setTextColor(Color.parseColor("#108020"));
        instanceView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        instanceView.setSingleLine();
        instanceView.setId(3);
        topLinesRelativeLayout.addView(instanceView, rlp);

        areaView = new TextView(activity);
        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //  rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlp.addRule(RelativeLayout.ALIGN_LEFT);
        rlp.addRule(RelativeLayout.ALIGN_TOP);
        rlp.addRule(RelativeLayout.BELOW, statusLine.getId());
        rlp.addRule(RelativeLayout.BELOW, instanceView.getId());
        rlp.addRule(RelativeLayout.RIGHT_OF, favStarCheckBox.getId());
       
        areaView.setText(Html.fromHtml("<b><font color=black>" + initiative.getArea().getName() + "</font></b>"));
        areaView.setTextColor(Color.parseColor("#108020"));
        areaView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        areaView.setSingleLine();
        areaView.setId(31);
        topLinesRelativeLayout.addView(areaView, rlp);



        issueButton = new ToggleButton(activity);//,null,android.R.attr.borderlessButtonStyle);
        issueButton.setTextColor(Color.BLACK);
        issueButtonSetText();
        issueButton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                expand();
            }
        });

        issueButton.setId(41);
        issueButton.setGravity(Gravity.LEFT);
        activity.registerForContextMenu(issueButton);

        rlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.BELOW, areaView.getId());
        rlp.addRule(RelativeLayout.ALIGN_TOP);
        rlp.addRule(RelativeLayout.ALIGN_LEFT);
        rlp.setMargins(2, 0, 2, 0);
        topLinesRelativeLayout.addView(issueButton, rlp);

        expandView = new LinearLayout(activity);
        expandView.setOrientation(VERTICAL);
        rlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.BELOW, issueButton.getId());
        rlp.addRule(RelativeLayout.ALIGN_TOP);
        rlp.addRule(RelativeLayout.ALIGN_LEFT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        expandView.setId(5);
        topLinesRelativeLayout.addView(expandView, rlp);
        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1f);
        this.addView(topLinesRelativeLayout, lp);
        this.forceLayout();
    }

    protected long itemSpecificDelta() {
        return System.currentTimeMillis() - initiative.issue_created.getTime();
    }

    protected int itemSpecificColorcode() {
        long delta = System.currentTimeMillis() - initiative.issue_created.getTime();
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

    protected String getStatusText() {
        DateFormat formatter = new SimpleDateFormat(activity.getDateFormat());
        return " <b><font color=black> " + activity.getString(initiative.getIntlStateResId()) + "</font> - <font color=blue>" + formatter.format(initiative.issue_created) + "</font></b>";
    }

    public void onFavStarClick(View arg0) {
        int issueid = initiative.issue_id;
        initiative.getArea().getInitiativen().setSelectedIssue(issueid, !initiative.getArea().getInitiativen().isIssueSelected(issueid));

        try {
            if (((LiqoidApplication) activity.getApplication()).getGlobalPreferences().getBoolean("vibrate", true)) {
                Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(30);
            }
        } catch (Exception e) {
            //its not a vibrator :/
        }
    }

    public void issueButtonSetText() {
        String text = "<b>Thema:</b> " + initiative.name + "<font color=\"#009010\"> / Inis: <b>" + ((int) 1 + initiative.getConcurrentInis().size()) + "</b></font>";
        issueButton.setText(Html.fromHtml(text));
        issueButton.setTextOff(Html.fromHtml(text));
        issueButton.setTextOn(Html.fromHtml(text));
    }

    public void expandButtonSetText() {
        String text = Utils.lessThanDays(itemSpecificDelta());
        String retval = "";
        for (Character c : text.toCharArray()) {
            retval += c + "\n";
        }

        colorIndicator.setText(retval);

    }

    public void expand() {
        if (expandview) {
            issueButtonSetText();
            statusLine.setText(Html.fromHtml(getStatusText()));
            expandView.removeAllViews();

            expandview = false;
        } else {
            TextView tv = new TextView(activity);
            tv.setText(activity.getString(R.string.initiatives) + ": " + ((int) 1 + initiative.getConcurrentInis().size()));
            tv.setTextColor(Color.BLACK);
            tv.setPadding(2, 2, 2, 2);
            expandView.addView(tv);
            expandView.addView(generateIniButton(initiative), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            for (Initiative i : initiative.getConcurrentInis()) {
                expandView.addView(generateIniButton(i), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }
            expandview = true;
        }
        colorIndicator.setPadding(2, 2, 2, 2);

        this.getParent().requestLayout();

    }

    public Button generateIniButton(final Initiative i) {
        Button retval = new Button(activity);
        retval.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                String strcontent="";
                if (i.getLqfbInstance().getApiversion().equals(LQFBInstance.API1)){
                strcontent="<b>Status: " + activity.getString(i.getIntlStateResId()) + "</b><br><br>\n\n" + i.current_draft_content;
                }
                 if (i.getLqfbInstance().getApiversion().equals(LQFBInstance.API2)){
                strcontent="<b>Status: " + activity.getString(i.getIntlStateResId()) + "</b><br><br>\n\n" + i.current_draft_content;
                }
                builder.setMessage(Html.fromHtml(strcontent)).setNeutralButton(R.string.open_browser, new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(i.getLqfbInstance().getWebUrl() + "initiative/show/" + i.id + ".html"));
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        activity.startActivity(myIntent);
                    }
                }).setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        intent.putExtra(Intent.EXTRA_TEXT, i.getLqfbInstance().getWebUrl() + "initiative/show/" + i.id + ".html");
                        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share)));
                    }
                }).setTitle(i.name);

                AlertDialog ad = builder.create();
                ad.show();

            }
        });
        String quorumcolor = "#009010";//green
        if (i.getQuorum() > i.supporter_count) {
            quorumcolor = "#d00010";//red
        }
        retval.setText(Html.fromHtml("<b>Ini:</b> " + i.name + "<font color=\"#009010\"><br>" + activity.getString(R.string.supporter) + ":</font><font color=\"" + quorumcolor + "\"><b>" + i.supporter_count + " / " + i.getQuorum() + "</b></font>"));
        retval.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        retval.setGravity(Gravity.LEFT);
        return retval;
    }
}
