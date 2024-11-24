package com.example.helloandroid.audio

import android.app.AlertDialog
import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TTSManager(context : Context) : TextToSpeech.OnInitListener {
	private var tts: TextToSpeech = TextToSpeech(context, this)
	private var isInitialized = false

	override fun onInit(status : Int){
		if (status == TextToSpeech.SUCCESS){
			val result = tts!!.setLanguage(Locale("pl"))
			isInitialized = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
			if(!isInitialized)
				println("Polski język nie jest obsługiwany na tym urządzeniu")
		}
	}

	fun speak(text: String){
		if(isInitialized)
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
	}

	fun release(){
		tts!!.stop()
		tts!!.shutdown()
	}
}