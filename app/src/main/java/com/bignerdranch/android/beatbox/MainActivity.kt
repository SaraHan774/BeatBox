package com.bignerdranch.android.beatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.beatbox.databinding.ActivityMainBinding
import com.bignerdranch.android.beatbox.databinding.ListItemSoundBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {



    private lateinit var beatBox : BeatBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beatBox = BeatBox(assets)

        val binding : ActivityMainBinding
        = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter(beatBox.sounds)
        }

        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    Log.d(TAG, "progress : $progress - fromUser : $fromUser")
                    binding.tvPlaybackSpeed.text =
                        String.format(getString(R.string.playback_speed), progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.d(TAG, "onStartTrackingTouch")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Log.d(TAG, "onStopTrackingTouch - final progress : ${seekBar?.progress}")
                    var progress = seekBar?.progress
                    progress?.let { convertProgressIntoRate(it) }?.let {
                        beatBox.changePlaybackSpeed(it)
                    }
                }
            }
        )

    }

    private fun convertProgressIntoRate(progress : Int) : Float{
        return if(progress == 0){
            0.5f
        }else{
            0.5f + (progress * 0.015f)
        }
    }

    //Creating ViewHolder
    private inner class SoundHolder(private val binding : ListItemSoundBinding):
            RecyclerView.ViewHolder(binding.root){

        init {
            //hook up your view model
            binding.viewModel = SoundViewModel(beatBox)
        }

        fun bind(sound : Sound){
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()
            }
        }
    }

    private inner class SoundAdapter(private val sounds : List<Sound>) :
            RecyclerView.Adapter<SoundHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )

            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }

        override fun getItemCount(): Int {
            return sounds.size
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        beatBox.release()
    }

}
