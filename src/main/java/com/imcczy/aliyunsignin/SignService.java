package com.imcczy.aliyunsignin;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by imcczy on 16/5/30.
 */

public class SignService extends AccessibilityService {
    private AccessibilityNodeInfo rootNodeInfo;
    private int previousDate = 0;
    private int nowDate = 0;
    SharedPreferences time;
    private String TAG = getClass().getSimpleName();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String classname = event.getClassName().toString();

        //对比时间，每天只进行一次签到
        previousDate = time.getInt("date", 0);
        String now = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        nowDate = Integer.valueOf(now);

        if (nowDate-previousDate == 0 ) {
            Log.i(TAG,"已签到");
            return;
        }

        rootNodeInfo = event.getSource();
        if (rootNodeInfo == null)
            return;

        //找到签到按钮，并点击

        if ("com.alibaba.aliyun.biz.home.MainActivity".equals(classname)){
            List<AccessibilityNodeInfo> list_1 = event.getSource().findAccessibilityNodeInfosByViewId("com.alibaba.aliyun:id/zhuanQu");
            if (!list_1.isEmpty()) {
                list_1.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        else if ("com.alibaba.aliyun.biz.products.student.StudentActivity".equals(classname)){
            List<AccessibilityNodeInfo> list_2 = event.getSource().findAccessibilityNodeInfosByText("赚权益");
            if (!list_2.isEmpty()) {
                list_2.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        else if ("com.alibaba.aliyun.biz.products.student.EarnRewardsActivity".equals(classname)){
            List<AccessibilityNodeInfo> list_3 = event.getSource().findAccessibilityNodeInfosByViewId("com.alibaba.aliyun:id/sign_button");
            if (!list_3.isEmpty()) {
                list_3.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                time.edit().putInt("date", nowDate).apply();
            }
        }
        else return;
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onServiceConnected() {
        time = getSharedPreferences("aliyun", 0);
        super.onServiceConnected();
    }
}
