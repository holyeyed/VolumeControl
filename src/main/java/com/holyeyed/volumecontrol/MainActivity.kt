package com.holyeyed.volumecontrol

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar

class MainActivity : Activity() {

    private lateinit var audioManager: AudioManager
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val btnIncrease = findViewById<Button>(R.id.btnIncrease)
        val btnDecrease = findViewById<Button>(R.id.btnDecrease)
        seekBar = findViewById<SeekBar>(R.id.seekBarVolume)

        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        seekBar.max = maxVolume
        seekBar.progress = currentVolume

        // Lắng nghe thay đổi từ SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                }
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        // Nút tăng âm lượng
        btnIncrease.setOnClickListener {
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_SHOW_UI
            )
            updateSeekBar()
        }

        // Nút giảm âm lượng
        btnDecrease.setOnClickListener {
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_SHOW_UI
            )
            updateSeekBar()
        }
    }

    private fun updateSeekBar() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        seekBar.progress = currentVolume
    }

    override fun onResume() {
        super.onResume()
        updateSeekBar() // Cập nhật lại thanh kéo khi quay lại app
    }
}