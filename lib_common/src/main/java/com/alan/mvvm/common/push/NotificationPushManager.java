package com.alan.mvvm.common.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.alan.mvvm.base.BaseApplication;
import com.alan.mvvm.common.R;


public class NotificationPushManager {
    private static volatile NotificationPushManager instance;
    //通知渠道ID
    private static final String CHANNEL_ID = "101";
    //通知ID
    private static final int NOTIFY_ID = 102;
    private Context context;
    private final NotificationManagerCompat managerCompat;


    public static NotificationPushManager getInstance() {
        if (instance == null) {
            synchronized (NotificationPushManager.class) {
                if (instance == null) {
                    instance = new NotificationPushManager();
                }
            }
        }
        return instance;
    }

    private NotificationPushManager() {
        context = BaseApplication.mContext;
        managerCompat = NotificationManagerCompat.from(context);
    }


    /**
     * 悬挂式,支持6.0以上系统
     *
     * @param context
     */
    public void sendNotification(Context context, String title, String content, PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = new Notification.Builder(context.getApplicationContext(), CHANNEL_ID);
            builder.setSmallIcon(R.drawable.icon_login_logo);
            builder.setAutoCancel(true);
            builder.setWhen(System.currentTimeMillis());
            builder.setContentTitle(title);
            builder.setContentText(content);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            managerCompat.notify(NOTIFY_ID, builder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID);
            builder.setSmallIcon(R.drawable.icon_login_logo);
            builder.setAutoCancel(true);
            builder.setWhen(System.currentTimeMillis());
            builder.setContentTitle(title);
            builder.setContentText(content);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            managerCompat.notify(NOTIFY_ID, builder.build());
        }

    }

    /**
     * 创建通知渠道
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannel() {
        //26及以上
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Ring", android.app.NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.canBypassDnd();//可否绕过请勿打扰模式
        notificationChannel.canShowBadge();//桌面lanchener显示角标
        notificationChannel.enableLights(true);//闪光
        notificationChannel.shouldShowLights();//闪光
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);//锁屏显示通知
        notificationChannel.enableVibration(true);//是否允许震动
        notificationChannel.setVibrationPattern(new long[]{100, 100, 200});//设置震动方式（事件长短）
        notificationChannel.getAudioAttributes();//获取系统响铃配置
        notificationChannel.getGroup();//获取消息渠道组
        notificationChannel.setBypassDnd(true);
        notificationChannel.setDescription("Ring通知");
        notificationChannel.setLightColor(Color.GREEN);//制定闪灯是灯光颜色
        notificationChannel.setShowBadge(true);
        managerCompat.createNotificationChannel(notificationChannel);
    }

    /**
     * 取消所有通知
     */
    public void cancelNotification() {
        managerCompat.cancel(NOTIFY_ID);
    }
}