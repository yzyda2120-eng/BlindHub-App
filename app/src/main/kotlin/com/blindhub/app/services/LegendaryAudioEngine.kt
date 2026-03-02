package com.blindhub.app.services

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import java.io.File

/**
 * محرك الصوت الأسطوري - Legendary Audio Engine (v3.0)
 * صُمم ليكون الأسرع والأكثر دقة في الاستجابة للمكفوفين، مع تقليل وقت التأخير (Latency).
 */
class LegendaryAudioEngine(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var currentFile: File? = null

    /**
     * بدء التسجيل الفوري بجودة عالية (HD Audio)
     */
    fun startInstantRecording() {
        try {
            currentFile = File(context.filesDir, "audio_legend_${System.currentTimeMillis()}.m4a")
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(currentFile?.absolutePath)
                prepare()
                start()
            }
            Log.d("LegendaryAudio", "Recording started: ${currentFile?.absolutePath}")
        } catch (e: Exception) {
            Log.e("LegendaryAudio", "Instant recording failed", e)
        }
    }

    /**
     * إيقاف التسجيل واستخراج الملف فوراً
     */
    fun stopInstantRecording(): String? {
        return try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            Log.d("LegendaryAudio", "Recording finalized")
            currentFile?.absolutePath
        } catch (e: Exception) {
            Log.e("LegendaryAudio", "Stop failed", e)
            null
        }
    }

    /**
     * تشغيل صوتي فوري مع استجابة سريعة للمس
     */
    fun playInstant(filePath: String, onComplete: () -> Unit) {
        stopAnyPlayback()
        player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            try {
                setDataSource(filePath)
                prepare()
                start()
                setOnCompletionListener { 
                    stopAnyPlayback()
                    onComplete()
                }
            } catch (e: Exception) {
                Log.e("LegendaryAudio", "Instant playback failed", e)
            }
        }
    }

    fun stopAnyPlayback() {
        player?.apply {
            if (isPlaying) stop()
            release()
        }
        player = null
    }
}
