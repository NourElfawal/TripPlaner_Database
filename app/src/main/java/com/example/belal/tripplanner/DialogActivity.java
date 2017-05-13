package com.example.belal.tripplanner;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DialogActivity extends AppCompatActivity {
    TextView tripName;
    Button startTrip,laterTrip,cancelTrip;
    Ringtone mAlarmSound;
    String my_TripName_From_DB,start_End_Points;
      static  StringBuffer roundTrip_Buffer;
      static  int x;
      static String arr_Of_Round_Trip[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        mAlarmSound = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

         tripName = (TextView)findViewById(R.id.textViewOfTripName);
        startTrip= (Button) findViewById(R.id.startTripButton);
        laterTrip= (Button) findViewById(R.id.laterTripButton);
        cancelTrip= (Button) findViewById(R.id.cancelTripButton);

        MyDatabaseAdapter db =new MyDatabaseAdapter(DialogActivity.this) ;
        db.open();
        System.out.println("");
  //
        Cursor c = db.getContact(x);

        if (c.moveToFirst()) {
            ShowDataTripDialog(c);
        }

        tripName.setText(my_TripName_From_DB);

        cancelTrip.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAlarmSound.stop();
                final AlertDialog.Builder builder=new AlertDialog.Builder(DialogActivity.this);
                builder.setTitle("Confirm Request");
                builder.setMessage("Are you Sure to Cancel Your trip ?");
                builder.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyDatabaseAdapter db =new MyDatabaseAdapter(DialogActivity.this);
                        db.open();
                        if (db.deleteContact(x)){
                            Toast.makeText(DialogActivity.this, "Canceled successful.", Toast.LENGTH_SHORT).show();
                            onBackPressed();}
                        else {
                            Toast.makeText(DialogActivity.this, "Canceled  failed.", Toast.LENGTH_SHORT).show();
                        }
                           db.close();

                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                    }
                });

                builder.show();


            }
        });






      /*
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mAlarmSound.isPlaying())
                    mAlarmSound.play();
            }
        }, 500);

    */


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                if (!mAlarmSound.isPlaying())
                    mAlarmSound.play();
//            }
//        }, 500);







        startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MyDatabaseAdapter db =new MyDatabaseAdapter(DialogActivity.this);
                db.open();

                Cursor c = db.getStartAndEndPoint(x);

                if (c.moveToFirst()) {
                    showStart_End_Points(c);
                }




                if (db.updateStatusContact(x,"done" )) {
                    Toast.makeText(DialogActivity.this, "Done successful.", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(DialogActivity.this, "Done failed.", Toast.LENGTH_LONG).show();

                }

                String xY_Points[];
                xY_Points=  start_End_Points.split("#");
                String xx=xY_Points[0];
                String yy=xY_Points[1];

//------------------ Round-------------------------//
                Cursor c3 = db.getContact(x);

                if (c3.moveToFirst()) {
                    roundTrip_Buffer = new StringBuffer();
                    ShowDataTripDialogRound(c3);
                }

          arr_Of_Round_Trip=roundTrip_Buffer.toString().split("#");
        String trip_Type;
        trip_Type=arr_Of_Round_Trip[6];

                if(trip_Type.equals("One Direction Trip")){

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+xx+"&daddr="+yy+""));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);

                }
                else {

                    startActivity(new Intent(DialogActivity.this,RoundTripScreen.class));

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+xx+"&daddr="+yy+""));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);

                }

//--------------------End Round---------------------//
                //  to start Navigation on Maps
                onBackPressed();
            }
        });


laterTrip.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        MyDatabaseAdapter db =new MyDatabaseAdapter(DialogActivity.this);
        db.open();

        Cursor c = db.getStartAndEndPoint(x);

        if (c.moveToFirst()) {
            showStart_End_Points(c);
        }

        // push  Notification
        NotificationCompat.Builder   mNotification = new NotificationCompat.Builder(DialogActivity.this);
        mNotification.setSmallIcon(R.drawable.start);

        mNotification.setTicker(my_TripName_From_DB);
        mNotification.setContentTitle("Click to Start your Trip");
        mNotification.setContentText(my_TripName_From_DB);
        mNotification.setDefaults(Notification.DEFAULT_VIBRATE);

        //here when user click on the notification .. it will go to  Notification Activity
            mNotification.setAutoCancel(true);

        if (db.updateStatusContact(x,"done" )) {
            Toast.makeText(DialogActivity.this, "Done successful.", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(DialogActivity.this, "Done failed.", Toast.LENGTH_LONG).show();

        }

        String xY_Points[];
        xY_Points=  start_End_Points.split("#");
        String xx=xY_Points[0];
        String yy=xY_Points[1];

        Cursor c3 = db.getContact(x);

        if (c3.moveToFirst()) {
            roundTrip_Buffer = new StringBuffer();
            ShowDataTripDialogRound(c3);
        }

        arr_Of_Round_Trip=roundTrip_Buffer.toString().split("#");
        String trip_Type;
        trip_Type=arr_Of_Round_Trip[6];

        if(trip_Type.equals("One Direction Trip")){
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+xx+"&daddr="+yy+""));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");


            try {
                PendingIntent pendingIntent = PendingIntent.getActivity(DialogActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mNotification.setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); notificationManager.notify(0, mNotification.build());




            }
            catch (Exception e){
                Toast.makeText(DialogActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
        else {

            startActivity(new Intent(DialogActivity.this,RoundTripScreen.class));


            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+xx+"&daddr="+yy+""));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");


            try {
                PendingIntent pendingIntent = PendingIntent.getActivity(DialogActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mNotification.setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); notificationManager.notify(0, mNotification.build());




            }
            catch (Exception e){
                Toast.makeText(DialogActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

//--------------------End Round---------------------//
        //  to start Navigation on Maps
        onBackPressed();

        Toast.makeText(DialogActivity.this, "Later Clicked ", Toast.LENGTH_SHORT).show();

    }
});


    }

    private void ShowDataTripDialogRound(Cursor c) {

        roundTrip_Buffer.append(c.getString(1)+"#"+c.getString(2)+"#"+c.getString(3)+"#"+c.getString(4)+"#"+c.getString(5)+"#"+c.getString(6)+"#"+c.getString(7)+"#");

    }

    private void ShowDataTripDialog(Cursor c) {

    my_TripName_From_DB=c.getString(1);
    }
    private void showStart_End_Points(Cursor c) {

        start_End_Points=c.getString(1)+"#"+c.getString(2);
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        mAlarmSound.stop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            return false;
        }
        return true;
    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        MyDatabaseAdapter db =new MyDatabaseAdapter(DialogActivity.this);
//        db.open();
//        if (db.deleteContact(x)){
//            Toast.makeText(DialogActivity.this, "on Stop .", Toast.LENGTH_SHORT).show();
//            onBackPressed();}
//        else {
//        }
//        db.close();
//
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        //Toast.makeText(this, "on dest", Toast.LENGTH_SHORT).show();
//        MyDatabaseAdapter db =new MyDatabaseAdapter(DialogActivity.this);
//        db.open();
//        if (db.deleteContact(x)){
//            Toast.makeText(DialogActivity.this, "on pause .", Toast.LENGTH_SHORT).show();
//            onBackPressed();}
//        else {
//          //  Toast.makeText(DialogActivity.this, "Canceled  failed.", Toast.LENGTH_SHORT).show();
//        }
//        db.close();
//
//    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
     //   Toast.makeText(this, "on dest", Toast.LENGTH_SHORT).show();
        MyDatabaseAdapter db =new MyDatabaseAdapter(DialogActivity.this);
        db.open();
        if (db.deleteContact(x)){
            Toast.makeText(DialogActivity.this, "on panel .", Toast.LENGTH_SHORT).show();
            onBackPressed();}
        else {
           // Toast.makeText(DialogActivity.this, "Canceled  failed.", Toast.LENGTH_SHORT).show();
        }
        db.close();


    }



    public static class AlarmReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {

            System.out.println( intent.getIntExtra("Test",2017));
            x= intent.getIntExtra("Test",2017);

            Intent dialogIntent = new Intent(context, DialogActivity.class);
            dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dialogIntent);
        }


    }
//    public static class AlarmReceiver2 extends BroadcastReceiver {
//StringBuffer idBuffer;
//        long myTripId;
//        public void onReceive(Context context, Intent intent) {
////
////            System.out.println( intent.getIntExtra("Test2",2017));
////            x= intent.getIntExtra("Test2",2017);
//
//            MyDatabaseAdapter db = new MyDatabaseAdapter(context);
//            db.open();
//            Cursor c = db.getAllContacts();
//            if (c.moveToFirst()) {
//               // myBufferUpComingTrips = new StringBuffer();
//                idBuffer=new StringBuffer();
//                do {
//                    ShowDataTrip(c);
//                } while (c.moveToNext());
//            }
//
//            String arr[]=idBuffer.toString().split("#");
//            for (int i=0;i<arr.length;i++) {
//                Intent intent1 = new Intent(context, DialogActivity.AlarmReceiver.class);
//
//                int your_Id_After_Parse=Integer.parseInt(arr[i]);
//                intent1.putExtra("Test",your_Id_After_Parse);
//              PendingIntent  pendingIntent = PendingIntent.getBroadcast(context,your_Id_After_Parse, intent1, PendingIntent.FLAG_ONE_SHOT);
//
//                AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
//
//                // am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//                //       AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
//                System.out.println("      ***** After alarm *****   "+your_Id_After_Parse);
//
//
//                am.set(AlarmManager.RTC_WAKEUP, AddTripFragment.today.getTimeInMillis(), pendingIntent);
//
//
//            }
//
//            Intent dialogIntent = new Intent(context, DialogActivity.class);
//            dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(dialogIntent);
//
//            }
//
//        private void ShowDataTrip(Cursor c) {
//
//            myTripId=Long.parseLong(c.getString(0));
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+myTripId);
//            idBuffer.append(myTripId+"#");
//
//        }


   // }

}
