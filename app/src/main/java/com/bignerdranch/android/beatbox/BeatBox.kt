package com.bignerdranch.android.beatbox

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException
import java.lang.Exception

private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"
private const val MAX_SOUNDS = 5

class BeatBox(private val assets : AssetManager){

    val sounds : List<Sound>
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_SOUNDS)
        .build()

    init{
        sounds = loadSounds()
    }

    private fun loadSounds(): List<Sound> {
        //AssetManager.list(String) lists filenames contained in the folder path you pass in.
        val soundNames = assets.list(SOUNDS_FOLDER)!!

        val sounds = mutableListOf<Sound>()
        soundNames.forEach { filename ->
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound = Sound(assetPath)
            try{
                load(sound)
            sounds.add(sound)
            }catch (ioe : IOException){
                Log.e(TAG, "Could not load sound $filename", ioe)
            }
        }

        return sounds
    }

    private fun load(sound : Sound){
        val assetFileDescriptor : AssetFileDescriptor
        = assets.openFd(sound.assetPath)

        val soundId = soundPool.load(assetFileDescriptor, 1)
        sound.soundId = soundId
    }


    fun play(sound : Sound){
        sound.soundId?.let {
            soundPool.play(it, 1.0f, 1.0f, 1,0,  sound.speed)
        }
    }
    //@param rate playback rate (1.0 = normal playback, range 0.5 to 2.0)

    fun changePlaybackSpeed(rate : Float){
        sounds.forEach{
            it.speed = rate
        }
    }

    fun release(){
        soundPool.release()
    }
}