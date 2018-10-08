package b.com.myapplication.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import b.com.myapplication.HomeActivity;
import b.com.myapplication.LoginActivity;
import b.com.myapplication.R;
import b.com.myapplication.constant.Constant;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String title;
    private String body;
    private String uid;
    private String reference_id;
    private String type;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if (remoteMessage.getData().size() > 0) {

            uid = remoteMessage.getData().get("uid");
            body = remoteMessage.getData().get("body");
            type = remoteMessage.getData().get("type");
            reference_id = remoteMessage.getData().get("reference_id");
            title = remoteMessage.getData().get("title");

            //Bundle[{other_key=true, google.delivered_priority=high, google.sent_time=1538560860550, google.ttl=2419200, google.original_priority=high, gcm.notification.e=1, gcm.notification.badge=1, gcm.notification.sound=default, gcm.notification.title=vcnfdjdj, uid=gngkffjj, body=dbdbd, from=359135775624, type=chat, gcm.notification.sound2=default, title=vcnfdjdj, click_action=ChatActivity, google.message_id=0:1538560860560586%730b87eb730b87eb, gcm.notification.body=dbdbd, gcm.notification.icon=icon, gcm.notification.type=chat, gcm.notification.reference_id=vcnfdjdj_gngkffjj, google.c.a.e=1, gcm.notification.uid=gngkffjj, gcm.notification.click_action=ChatActivity, fcm_token=, collapse_key=b.com.myapplication}]

        }
        sendNotification(body, title,uid,reference_id,type);
    }

    //getting the title and the body

    private void sendNotification(String body, String title, String uid, String reference_id, String type) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("body", body);
        intent.putExtra("title", title);
        intent.putExtra("uid", uid);
        intent.putExtra("reference_id", reference_id);
        intent.putExtra("type", type);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constant.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(this.title)
                .setContentText(body)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(Constant.CHANNEL_ID, Constant.CHANNEL_NAME, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(1, notificationBuilder.build());
    }
}

