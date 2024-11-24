package com.example.helloandroid.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.helloandroid.R

class SoundPoolManager(context : Context) {
	private val soundPool : SoundPool
	private val soundMap: MutableMap<String, Int> = mutableMapOf()

	init {
		val audioAttributes = AudioAttributes.Builder()
			.setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
			.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
			.build()

		soundPool = SoundPool.Builder()
			.setMaxStreams(5)
			.setAudioAttributes(audioAttributes)
			.build()

		loadSounds(context)
	}

	private fun loadSounds(context : Context){
		soundMap["0"] = soundPool.load(context, R.raw.so_anger, 1)
		soundMap["1"] = soundPool.load(context, R.raw.laser_shot, 1)
		soundMap["2"] = soundPool.load(context, R.raw.ui_click, 1)
		soundMap["3"] = soundPool.load(context, R.raw.fist_punch, 1)
	}

	fun playSound(key: String){
		val soundId = soundMap[key]
		if(soundId != null){
			soundPool.play(soundId, 1f,1f, 1, 0, 1f)
		}
	}

	fun release(){
		soundPool.release()
	}
}