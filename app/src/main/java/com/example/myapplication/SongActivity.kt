package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.myapplication.SongsAdapter.Companion.EXTRA_SONG
import com.example.myapplication.data.Song
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout

const val DEFAULT_TEXT_SIZE = 14
const val MIN_TEXT_SIZE = 12
const val MAX_TEXT_SIZE = 18
class SongActivity : AppCompatActivity() {
    private lateinit var capoTV: TextView
    private lateinit var textSongTV: TextView
    private var currentTextSize: Int = DEFAULT_TEXT_SIZE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        val nameSongMTB: MaterialToolbar = findViewById(R.id.toolAppBar)
        textSongTV = findViewById(R.id.textSong)
        capoTV = findViewById(R.id.capo)

        //Загрузка сохранённого размера текста
        currentTextSize = loadCurrentTextSize(currentTextSize)
        textSongTV.textSize = currentTextSize.toFloat()

        val song = intent.getSerializableExtra(EXTRA_SONG) as Song
        nameSongMTB.title = song.songName
        nameSongMTB.subtitle = song.songAuthor
        textSongTV.text = song.songText

        nameSongMTB.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }

    fun showSettingsSong(view: View) {
        dialog()
    }

    private fun loadCurrentTextSize(textSize: Int): Int {
        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("currentTextSize", this.currentTextSize)
    }

    fun addToFavorites(item: MenuItem) {
        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_heart_fill)
        Toast.makeText(
            this,
            "Добавлено в избранное", Toast.LENGTH_SHORT
        ).show()
    }

    private fun dialog() {
        //Отображаю настройки песни
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setBackgroundDrawable(null)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

        //Показываю меню тональностей
        val keys = resources.getStringArray(R.array.keys)
        val adapterKeys = ArrayAdapter(this, R.layout.drop_down_item, keys)
        val autoCompleteTextView = dialog.findViewById<AutoCompleteTextView>(R.id.showKeysTV)
        autoCompleteTextView.setAdapter(adapterKeys)

        //Показываю меню капо
        val capos = resources.getStringArray(R.array.capos)
        val adapterCapos = ArrayAdapter(this, R.layout.drop_down_item, capos)
        val showCaposTV = dialog.findViewById<AutoCompleteTextView>(R.id.showCaposTV)
        showCaposTV.setAdapter(adapterCapos)

        //Включаю аккорды по умолчанию
        val switchChords: SwitchCompat = dialog.findViewById(R.id.switchChords)
        switchChords.isChecked = true
        switchChords.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                textSongTV.text.toString().replace(Regex("[a-zA-Z]"), "")
            }
        }

        //Настраиваю логику каподастра
        val switchCapos: SwitchCompat = dialog.findViewById(R.id.switchCapo)
        val capoTIL: TextInputLayout = dialog.findViewById(R.id.capoTIL)
        switchCapos.isChecked = false
        switchCapos.setOnClickListener {
            when (switchCapos.isChecked) {
                true -> {
                    capoTV.isVisible = true
                    capoTIL.isVisible = true
                }

                false -> {
                    capoTV.isVisible = false
                    capoTIL.isVisible = false
                }
            }
        }

        //Настраиваю увеличение/уменьшение текста
        val increasingIB: ImageButton = dialog.findViewById(R.id.increasingTextButton)
        val decreasingIB: ImageButton = dialog.findViewById(R.id.decreasingTextButton)
        val textSizeTV: TextView = dialog.findViewById(R.id.textSizeTV)

        //Сохраняю currentTextSize
        fun saveCurrentTextSize(currentTextSize: Int) {
            val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt("currentTextSize", currentTextSize)
                apply()
            }
        }

        increasingIB.setOnClickListener {
            if (textSizeTV.text.equals(MAX_TEXT_SIZE.toString())) {
                Toast.makeText(
                    this, "Это максимальный размер текста",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                textSizeTV.text = (currentTextSize + 1).toString()
                textSongTV.textSize = currentTextSize.toFloat()
                currentTextSize++
                saveCurrentTextSize(currentTextSize)
            }
        }
        decreasingIB.setOnClickListener {
            if (textSizeTV.text.equals(MIN_TEXT_SIZE.toString())) {
                Toast.makeText(
                    this, "Это минимальный размер текста",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                textSizeTV.text = (currentTextSize - 1).toString()
                textSongTV.textSize = currentTextSize.toFloat()
                currentTextSize--
                saveCurrentTextSize(currentTextSize)
            }
        }
        //После завершения всех операций обновляю textSizeTV
        textSizeTV.text = currentTextSize.toString()
    }
}