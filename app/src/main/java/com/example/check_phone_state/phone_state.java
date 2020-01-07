package com.example.check_phone_state;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

 public class phone_state extends BroadcastReceiver {

    static MediaRecorder recorder;
    static MediaRecorder outgoing;
    File audiofile;
    File outgoingfile;
    static boolean recordstart ;
    static boolean isRecordstartoutgoing;
    SharedPreferences sharedPreferences;
    static  boolean callringing ;
    dbhelper helper;
   static String number;
    int i=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("State1", Context.MODE_PRIVATE);
        helper = new dbhelper(context);
        String chk = preferences.getString("chk_state", "");
        if (chk.equals("true")) {
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                callringing = true;
                Toast.makeText(context, " Call Ringing" + number, Toast.LENGTH_SHORT).show();

            } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                if (callringing) {
                    Toast.makeText(context, " call started", Toast.LENGTH_SHORT).show();
                    File sampleDir = new File(Environment.getExternalStorageDirectory(), "/TestRecordingData");
                    if (!sampleDir.exists()) {
                        sampleDir.mkdirs();
                    }
                    String file_name = "IncomingCalle";
                    try {
                        audiofile = File.createTempFile(file_name, ".amr", sampleDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);

                    // recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                   /* recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                     recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        */


                    recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    recorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
        /*recorder.setAudioEncodingBitRate(16);
        recorder.setAudioSamplingRate(44100);
*/
                    recorder.setOutputFile(audiofile.getAbsolutePath());

                    try {
                        recorder.prepare();
                        recorder.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Date currentTime = Calendar.getInstance().getTime();
                    boolean result = helper.insertdata(number, currentTime.toString());

                     if (result) {
                        Toast.makeText(context, "record added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "record not added", Toast.LENGTH_SHORT).show();
                    }


                    recordstart = true;
                    number = "";

                    Toast.makeText(context, " recording done " + recordstart, Toast.LENGTH_SHORT).show();


                }}
            else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    if (recordstart) {
                        recorder.stop();
                        recordstart = false;
                        recorder = null;
                        Toast.makeText(context, "media recorder" + recordstart, Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(context, "call ended" + recordstart, Toast.LENGTH_SHORT).show();
                    callringing = false;
                }






        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {


            if (callringing == false) {

                Toast.makeText(context, "out going call start ", Toast.LENGTH_SHORT).show();
                File sampleDir = new File(Environment.getExternalStorageDirectory(), "/OutgoingCallData");
                if (!sampleDir.exists()) {
                    sampleDir.mkdirs();
                }
                //  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

                String file_name = "Outgoingcalle";
                try {
                    outgoingfile = File.createTempFile(file_name, ".amr", sampleDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                outgoing = new MediaRecorder();
                outgoing.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
               // outgoing.setAudioSource(MediaRecorder.AudioSource.MIC);
                //outgoing.setAudioSource(MediaRecorder.getAudioSourceMax());

                // recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
               // outgoing.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                //outgoing.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


            outgoing.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            outgoing.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);

                  /*recorder.setAudioEncodingBitRate(16);
                   recorder.setAudioSamplingRate(44100);
                   */

                outgoing.setOutputFile(outgoingfile.getAbsolutePath());

                try {
                    outgoing.prepare();
                    outgoing.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SharedPreferences preferences2 = context.getSharedPreferences("State1", Context.MODE_PRIVATE);
                String outnumber = preferences2.getString("outgoingno", "");
                Toast.makeText(context, "number is " + outnumber, Toast.LENGTH_SHORT).show();
                helper = new dbhelper(context);
                Date currentTime = Calendar.getInstance().getTime();
                boolean result = helper.insertdata(outnumber, currentTime.toString());

                if (result) {
                    Toast.makeText(context, "record added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "record not added", Toast.LENGTH_SHORT).show();
                }


                isRecordstartoutgoing = true;

                Toast.makeText(context, " recording done ", Toast.LENGTH_SHORT).show();
            } }

            else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                if (isRecordstartoutgoing) {
                    outgoing.stop();
                    isRecordstartoutgoing = false;
                    outgoing = null;
                    Toast.makeText(context, "media recorder" + isRecordstartoutgoing, Toast.LENGTH_SHORT).show();
                }
            }

    }


               else {

                   Toast.makeText(context, "plz enable it", Toast.LENGTH_SHORT).show();
               }



           }}





































