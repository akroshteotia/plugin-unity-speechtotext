package com.voice.plugin;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class UnityBridge extends UnityPlayerActivity implements RecognitionListener{
	private static final String LOG_TAG = "Speech";
	public static Context mContext;
	public static SpeechRecognizer speech = null;
	static UnityBridge listener;
	public static Intent recognizerIntent;
	public static String outPutText;
	
    public static void Init()
    {
    	Handler  handler= new Handler(Looper.getMainLooper());
    	handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				listener = new UnityBridge();
				speech = SpeechRecognizer.createSpeechRecognizer(UnityPlayer.currentActivity.getBaseContext());
				 recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				 recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,				 "en");
				 recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,UnityPlayer.currentActivity.getBaseContext().getPackageName());
				 recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
				 recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
				 speech.setRecognitionListener(listener);
				 //UnityPlayer.UnitySendMessage("GameManager", "Result", "Success");
				 Log.d(LOG_TAG, "init");
			}
		});    	
    }
    
	public static void StartSpeech()
	{
		Handler  handler= new Handler(Looper.getMainLooper());
    	handler.post(new Runnable() {
			
			@Override
			public void run() {
				speech.startListening(recognizerIntent);
				Log.i(LOG_TAG, "startListening");
			}
		});  
	}
	
	public static void StopSpeech()
	{
		Handler  handler= new Handler(Looper.getMainLooper());
    	handler.post(new Runnable() {
			
			@Override
			public void run() {
				speech.stopListening();
			}
		});  
	}


	@Override
	public void onBeginningOfSpeech() {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onBeginningOfSpeech");
		//UnityPlayer.UnitySendMessage("GameManager", "Result", "onBeginningOfSpeech");
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onBufferReceived: " + buffer);
		//UnityPlayer.UnitySendMessage("GameManager", "Result", "onBufferReceived");
	}

	@Override
	public void onEndOfSpeech() {
		// TODO Auto-generated method stub
		 Log.i(LOG_TAG, "onEndOfSpeech");
	}

	@Override
	public void onError(int errorCode) {
		String errorMessage = getErrorText(errorCode);
		// TODO Auto-generated method stub
		Log.d(LOG_TAG, "FAILED " + errorMessage);
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onEvent");
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onPartialResults");
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onReadyForSpeech");
		//UnityPlayer.UnitySendMessage("GameManager", "Result", "onReadyForSpeech");
	}

	@Override
	public void onResults(Bundle results) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onResults");
		 ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		 
		 JSONArray jsArray = new JSONArray(matches);
		 String text = jsArray.toString();
		 UnityPlayer.UnitySendMessage("ListnerManager", "Result", text);
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
	}
	
	public static String getErrorText(int errorCode) {
	 String message;
	 switch (errorCode) {
	 	case SpeechRecognizer.ERROR_AUDIO:
	 		message = "Audio recording error";
	 break;
	 	case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
	 		message = "Insufficient permissions";
	 break;
	 	case SpeechRecognizer.ERROR_NETWORK:
	 		message = "Network error";
	 break;
	 	case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
	 		message = "Network timeout";
	 break;
	 	case SpeechRecognizer.ERROR_NO_MATCH:
	 		message = "No match";
	 break;
	 	case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
	 		message = "RecognitionService busy";
	 break;
		case SpeechRecognizer.ERROR_SERVER:
			message = "error from server";
	 break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			message = "No speech input";
	 break;
	 default:
		 message = "Didn't understand, please try again.";
	 break;
	 }
	 return message;
	 }
}
