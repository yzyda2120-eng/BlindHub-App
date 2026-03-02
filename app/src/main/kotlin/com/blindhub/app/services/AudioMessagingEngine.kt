package com.blindhub.app.services

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import java.io.File
import java.io.IOException

/**
 * محرك الرسائل والمنشورات الصوتية الاحترافي - Audio Messaging Engine
 * يوفر تسجيل وتشغيل عالي الجودة وبدون مشاكل للمكفوفين.
 */
class AudioMessagingEngine(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var audioFile: File? = null

    /**
     * بدء تسجيل منشور أو رسالة صوتية جديدة
     */
    fun startRecording() {
        try {
            audioFile = File(context.cacheDir, "temp_audio_${System.currentTimeMillis()}.3gp")
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()
            }
            Log.d("AudioEngine", "Recording started: ${audioFile?.absolutePath}")
        } catch (e: IOException) {
            Log.e("AudioEngine", "Recording failed", e)
        }
    }

    /**
     * إيقاف التسجيل وحفظ الملف
     */
    fun stopRecording(): String? {
        return try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            Log.d("AudioEngine", "Recording stopped")
            audioFile?.absolutePath
        } catch (e: Exception) {
            Log.e("AudioEngine", "Stop recording failed", e)
            null
        }
    }

    /**
     * تشغيل منشور أو رسالة صوتية
     */
    fun playAudio(filePath: String, onComplete: () -> Unit) {
        stopPlayback()
        player = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
                start()
                setOnCompletionListener { 
                    stopPlayback()
                    onComplete()
                }
            } catch (e: IOException) {
                Log.e("AudioEngine", "Playback failed", e)
            }
        }
    }

    fun stopPlayback() {
        player?.release()
        player = null
    }
}
