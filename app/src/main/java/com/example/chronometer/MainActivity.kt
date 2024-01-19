package com.example.chronometer

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var chronoMeter: ChronoMeter
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button

    private var isTimerRunning = false
    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    private val handler = Handler(Looper.getMainLooper())
    private val updateTask = object : Runnable {
        override fun run() {
            if (isTimerRunning) {
                elapsedTime = System.currentTimeMillis() - startTime
                updateChronoMeter(elapsedTime)
                handler.postDelayed(this, 10)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chronoMeter = findViewById(R.id.chronoMeter)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)

        startButton.setOnClickListener {
            startTimer()
        }

        stopButton.setOnClickListener {
            stopTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
        }
        // Інші дії вашої активності
    }

    private fun startTimer() {
        if (!isTimerRunning) {
            startTime = System.currentTimeMillis() - elapsedTime
            isTimerRunning = true
            handler.postDelayed(updateTask, 10)
        }
    }

    private fun stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false
            handler.removeCallbacks(updateTask)
        }
    }

    private fun resetTimer() {
        elapsedTime = 0
        updateChronoMeter(elapsedTime)
    }
    private fun updateChronoMeter(elapsedTime: Long) {
        chronoMeter.text = formatElapsedTime(elapsedTime)
    }

    private fun formatElapsedTime(elapsedTime: Long): String {
        val seconds = elapsedTime / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val millis = elapsedTime % 100

        return String.format("%02d:%02d:%02d.%02d", hours, minutes % 60, seconds % 60, millis)
    }

    // Додайте код для обробки подій, якщо необхідно
}

class ChronoMeter(context: Context, attrs: AttributeSet? = null) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    private var startTime: Long = 0
    private var isRunning: Boolean = false

    private val handler = Handler(Looper.getMainLooper())

    private val updateTask = object : Runnable {
        override fun run() {
            if (isRunning) {
                updateText()
                handler.postDelayed(this, 10) // оновлення кожні 10 мілісекунд
            }
        }
    }

    init {
        reset()
    }

    fun start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime()
            isRunning = true
            handler.post(updateTask)
        }
    }

    fun stop() {
        if (isRunning) {
            handler.removeCallbacks(updateTask)
            isRunning = false
        }
    }

    fun reset() {
        startTime = System.currentTimeMillis()
        updateText()
    }

    private fun elapsedTime(): Long {
        return System.currentTimeMillis() - startTime
    }

    private fun updateText() {
        val elapsedTime = elapsedTime()
        val sdf = SimpleDateFormat("HH:mm:ss.SS", Locale.getDefault()) // Формат для відображення мілісекунд
        val formattedTime = sdf.format(Date(elapsedTime))
        text = formattedTime
    }
}