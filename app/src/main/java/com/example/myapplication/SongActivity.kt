package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.myapplication.SongsAdapter.Companion.EXTRA_SONG
import com.example.myapplication.data.Song
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout

const val DEFAULT_TEXT_SIZE = 16
const val MIN_TEXT_SIZE = 12
const val MAX_TEXT_SIZE = 20
const val FIND_ALL_CHORDS = "([A-H]\\d)|([A-H]\\w{3}\\d)|([A-H]\\w{3})|([A-H]|m\\d|b|m)"
const val FIND_ALL_SPACE = "^\\s{3,}"
const val FIND_ALL_CHARTERS = "[~!@#\$%^/|()&*+-]"
class SongActivity : AppCompatActivity() {
    private lateinit var capoTV: TextView
    private lateinit var capoFret: TextView
    private lateinit var textSongTV: TextView
    private lateinit var song: Song
    private lateinit var sharedPrefSwitchChords: SharedPreferences
    private lateinit var sharedPrefTextSize: SharedPreferences
    private lateinit var sharedPrefSwitchCapo: SharedPreferences
    private var currentTextSize: Int = DEFAULT_TEXT_SIZE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        val nameSongMTB: MaterialToolbar = findViewById(R.id.toolAppBar)

        textSongTV = findViewById(R.id.textSong)
        capoTV = findViewById(R.id.capo)
        capoFret = findViewById(R.id.capoFret)

        song = intent.getSerializableExtra(EXTRA_SONG) as Song

        fun inflateSongActivity() {
            nameSongMTB.title = song.songName
            nameSongMTB.subtitle = song.songAuthor
            textSongTV.text = song.songText
        }

        sharedPrefSwitchCapo =
            getSharedPreferences("PrefSwitchCapo", MODE_PRIVATE)

        // Беру установленный размер текста как фильтр
        sharedPrefTextSize =
            getSharedPreferences("PrefsTextSize", Context.MODE_PRIVATE)
        val chosenTextSize = sharedPrefTextSize
            .getInt("currentTextSize", currentTextSize)

        // Ставлю Switch в нужное состояние
        sharedPrefSwitchChords =
            getSharedPreferences("PrefSwitchChords", Context.MODE_PRIVATE)
        val chordsIsNotChosen = sharedPrefSwitchChords
            .getBoolean("switch_state_chords", false)

        // Устанавливаю фильтры
        if (!chordsIsNotChosen) {

            inflateSongActivity()

            // Удаляю аккорды
            deleteChords()
            textSongTV.textSize = chosenTextSize.toFloat()

        } else {

            inflateSongActivity()
            textSongTV.textSize = chosenTextSize.toFloat()

        }

        nameSongMTB.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }

    fun showSettingsSong(view: View) {
        dialog()
    }

    fun addToFavorites(item: MenuItem) {
        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_heart_fill)
        Toast.makeText(
            this,
            "Добавлено в избранное", Toast.LENGTH_SHORT
        ).show()
    }

    private fun deleteChords() {
        textSongTV.text = textSongTV.text.toString()
            .replace(Regex(FIND_ALL_CHARTERS)) { "" }
        textSongTV.text = textSongTV.text.toString()
            .replace(Regex(FIND_ALL_CHORDS)) { "" }
        textSongTV.text = textSongTV.text.toString()
            .replace(Regex(FIND_ALL_SPACE)) { "" }
    }

    private fun dialog() {
        // Отображаю настройки песни
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

        // Показываю меню капо
        val capos = resources.getStringArray(R.array.capos)
        val adapterCapos = ArrayAdapter(this, R.layout.drop_down_item, capos)
        val showCaposTV = dialog.findViewById<AutoCompleteTextView>(R.id.showCaposTV)
        showCaposTV.setAdapter(adapterCapos)

        // Включаю аккорды по умолчанию
        val switchChords: SwitchMaterial = dialog.findViewById(R.id.switchChords)
        switchChords.isChecked = sharedPrefSwitchChords
            .getBoolean("switch_state_chords", true)

        // Сохраняю состояние switch для аккордов
        fun saveSwitchStateChords(isChecked: Boolean) {
            val editor = sharedPrefSwitchChords.edit()
            editor.putBoolean("switch_state_chords", isChecked)
            editor.apply()
        }

        switchChords.setOnCheckedChangeListener { _, isChecked ->
            saveSwitchStateChords(isChecked)
            switchChords.isChecked = sharedPrefSwitchChords
                .getBoolean("switch_state_chords", isChecked)

            if (!isChecked) {
                deleteChords()
            } else {
                textSongTV.text = song.songText
            }

        }

        // Настраиваю логику каподастра
        val capoTIL: TextInputLayout = dialog.findViewById(R.id.capoTIL)
        val switchCapo: SwitchMaterial = dialog.findViewById(R.id.switchCapo)
        switchCapo.isChecked = sharedPrefSwitchCapo
            .getBoolean("switch_state_capo", false)

        // Сохраняю состояние switch для Capo
        fun saveSwitchStateCapo(isChecked: Boolean) {
            val editor = sharedPrefSwitchCapo.edit()
            editor.putBoolean("switch_state_capo", isChecked)
            editor.apply()
        }

        switchCapo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                capoTV.isVisible = true
                showCaposTV.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        val selectedCapo: String = showCaposTV.adapter
                            .getItem(position) as String
                        capoFret.text = selectedCapo
                    }
            } else {
                showCaposTV.onItemClickListener = null
                capoTV.isVisible = false
                capoFret.text = ""
            }

            saveSwitchStateCapo(isChecked)
        }

        // Реализую транспонирование

        val increasingKB: ImageButton = dialog.findViewById(R.id.increasingKeyButton)
        val decreasingKB: ImageButton = dialog.findViewById(R.id.decreasingKeyButton)

        // Нахождение аккордов в тексте
        fun extractChords(text: String): List<String> {
            val currentChords = Regex("(?<![A-H])([A-H][b#])")
            return currentChords.findAll(text).map { it.value }.toList()
        }

        decreasingKB.setOnClickListener {
            val currentChords = extractChords(textSongTV.text.toString()).toMutableList()
            val transposedChords = mutableListOf<String>()

            currentChords.forEach { oldChord ->
                when (oldChord) {
                    "C" -> transposedChords.add("H")
                    "C#" -> transposedChords.add("C")
                    "Db" -> transposedChords.add("C")
                    "D" -> transposedChords.add("C#")
                    "D#" -> transposedChords.add("D")
                    "E" -> transposedChords.add("D#")
                    "F" -> transposedChords.add("E")
                    "F#" -> transposedChords.add("F")
                    "Gb" -> transposedChords.add("F")
                    "G" -> transposedChords.add("F#")
                    "G#" -> transposedChords.add("G")
                    "A" -> transposedChords.add("G#")
                    "Ab" -> transposedChords.add("G")
                    "A#" -> transposedChords.add("A")
                    "H" -> transposedChords.add("A#")
                }
            }

            if (currentChords.size == transposedChords.size) {
                for (index in currentChords.indices) {
                    val chordRegex = Regex("\\b${currentChords[index]}\\b")
                    textSongTV.text =
                        textSongTV.text.replace(chordRegex, transposedChords[index])
                }
            } else {
                Toast.makeText(
                    this,
                    "Упс... Ошибка при транспонировании",
                    Toast.LENGTH_LONG
                ).show()
            }

                transposedChords.clear()
                currentChords.clear()
            }

            increasingKB.setOnClickListener {
                val currentChords = extractChords(textSongTV.text.toString()).toMutableList()
                val transposedChords = mutableListOf<String>()

                currentChords.forEach { oldChord ->
                    when (oldChord) {
                        "C" -> transposedChords.add("C#")
                        "C#" -> transposedChords.add("D")
                        "Db" -> transposedChords.add("D")
                        "D" -> transposedChords.add("D#")
                        "D#" -> transposedChords.add("E")
                        "E" -> transposedChords.add("F")
                        "F" -> transposedChords.add("F#")
                        "F#" -> transposedChords.add("G")
                        "Gb" -> transposedChords.add("G")
                        "G" -> transposedChords.add("G#")
                        "G#" -> transposedChords.add("A")
                        "A" -> transposedChords.add("A#")
                        "Ab" -> transposedChords.add("A")
                        "A#" -> transposedChords.add("H")
                        "H" -> transposedChords.add("C")
                    }
                }

                if (currentChords.size == transposedChords.size) {
                    for (index in currentChords.indices) {
                        val chordRegex = Regex("\\b${currentChords[index]}\\b")
                        textSongTV.text =
                            textSongTV.text.replace(chordRegex, transposedChords[index])
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Упс... Ошибка при транспонировании",
                        Toast.LENGTH_LONG
                    ).show()
                }

                transposedChords.clear()
                currentChords.clear()
            }

            // Настраиваю увеличение/уменьшение текста
            val increasingIB: ImageButton = dialog.findViewById(R.id.increasingTextButton)
            val decreasingIB: ImageButton = dialog.findViewById(R.id.decreasingTextButton)
            val textSizeTV: TextView = dialog.findViewById(R.id.textSizeTV)

            currentTextSize = sharedPrefTextSize
                .getInt("currentTextSize", currentTextSize)
            textSongTV.textSize = currentTextSize.toFloat()
            textSizeTV.text = currentTextSize.toString()

            // Сохраняю размер текста
            fun saveCurrentTextSize(currentTextSize: Int) {
                val editor = sharedPrefTextSize.edit()
                editor.putInt("currentTextSize", currentTextSize)
                editor.apply()
            }

            increasingIB.setOnClickListener {
                if (currentTextSize == MAX_TEXT_SIZE) {
                    Toast.makeText(
                        this, "Это максимальный размер текста",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    currentTextSize++
                    textSizeTV.text = currentTextSize.toString()
                    textSongTV.textSize = currentTextSize.toFloat()
                    saveCurrentTextSize(currentTextSize)
                }
            }

            decreasingIB.setOnClickListener {
                if (currentTextSize == MIN_TEXT_SIZE) {
                    Toast.makeText(
                        this, "Это минимальный размер текста",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    currentTextSize--
                    textSizeTV.text = currentTextSize.toString()
                    textSongTV.textSize = currentTextSize.toFloat()
                    saveCurrentTextSize(currentTextSize)
                }
            }
        }
    }