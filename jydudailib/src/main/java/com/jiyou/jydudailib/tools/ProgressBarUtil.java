 package com.jiyou.jydudailib.tools;

 import android.app.Activity;
 import android.graphics.Color;
 import android.view.ViewGroup;
 import android.widget.ProgressBar;
 import android.widget.RelativeLayout;
 import android.widget.RelativeLayout.LayoutParams;

 public class ProgressBarUtil
 {
   private static RelativeLayout relativeLayout = null;
   public static void showProgressBar(final Activity mActivity) {
     mActivity.runOnUiThread(new Runnable()
     {
       public void run()
       {
         if (ProgressBarUtil.relativeLayout == null)
         {
              ProgressBarUtil.relativeLayout = new RelativeLayout(mActivity);
              ProgressBarUtil.relativeLayout.setClickable(true);
              ProgressBarUtil.relativeLayout.setBackgroundColor(Color.parseColor("#CC000000"));
              LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
              ProgressBarUtil.relativeLayout.setLayoutParams(lp1);
              ProgressBar bar1 = new ProgressBar(mActivity, null, android.R.attr.progressBarStyleLarge);
              LayoutParams lp2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
              lp2.addRule(RelativeLayout.CENTER_IN_PARENT , RelativeLayout.TRUE);
              bar1.setLayoutParams(lp2);
              ProgressBarUtil.relativeLayout.addView(bar1);
         }
         if (ProgressBarUtil.relativeLayout.getParent() == null){
           ((ViewGroup)mActivity.findViewById(android.R.id.content)).addView(ProgressBarUtil.relativeLayout);
       }
       }
     });
   }
 
   public static void hideProgressBar(final Activity mActivity)
   {
     if (relativeLayout!=null&&relativeLayout.getParent() != null)
       mActivity.runOnUiThread(new Runnable()
       {
         public void run()
         {
           ((ViewGroup)mActivity.findViewById(android.R.id.content)).removeView(ProgressBarUtil.relativeLayout);
             relativeLayout = null;
         }
       });
   }
 }

