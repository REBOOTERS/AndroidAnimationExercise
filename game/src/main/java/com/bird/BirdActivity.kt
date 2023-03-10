package com.bird

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.engineer.android.game.R

class BirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_bird)
        val choice = arrayOf(true, false)
        val view = findViewById<GameFlabbyBird>(R.id.game_board)
        view.customBg = choice.random()
    }
}