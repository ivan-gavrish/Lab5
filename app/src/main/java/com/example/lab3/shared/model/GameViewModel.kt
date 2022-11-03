package com.example.lab3.shared.model

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.ui.gamescreen.Colors
import com.example.lab3.ui.gamescreen.RandomColor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel : ViewModel(), RandomColor {
    private lateinit var _email: String
    val email: String get() = _email
    private lateinit var _difficulty: String
    val difficulty: String get() = _difficulty
    private val colorNames = Colors.colorNames()
    private val colorValues = Colors.colorValues()
    private var _leftWord = MutableLiveData<String>()
    val leftWord: LiveData<String> get() = _leftWord
    private var _rightWord = MutableLiveData<String>()
    val rightWord: LiveData<String> get() = _rightWord
    private var _leftWordColor = MutableLiveData<String>()
    val leftWordColor: LiveData<String> get() = _leftWordColor
    private var _rightWordColor = MutableLiveData<String>()
    val rightWordColor: LiveData<String> get() = _rightWordColor
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score
    private var _wordsCount = MutableLiveData<Int>()
    private var wordsCounter = 0
    private var _auth: FirebaseAuth
    val auth: FirebaseAuth get() = _auth

    init {
        getNextWords()
        getNextWordColors()
        _auth = FirebaseAuth.getInstance()
    }


    fun setEmail(email: String) {
        _email = email
    }

    fun setDifficulty(difficulty: String = "Easy") {
        _difficulty = difficulty
    }

    override fun randomColorName(): String = colorNames.random()

    override fun randomColorValue(): String = colorValues.random()

    private fun incrementWordsCount() {
        ++wordsCounter
        _wordsCount.value = wordsCounter
    }

    private fun getNextWords() {
        _leftWord.value = randomColorName()
        _rightWord.value = randomColorName()
        incrementWordsCount()
    }

    private fun getNextWordColors() {
        _leftWordColor.value = randomColorValue()
        _rightWordColor.value = randomColorValue()
    }

    fun nextWords(easyDifficulty: String): Boolean {
        if (difficulty != easyDifficulty) {
            if (score.value == 100) {
                return false
            }
        }

        getNextWords()
        getNextWordColors()
        return true
    }

    fun isUserAnswerCorrect(colorName: String, colorValue: Int, userAnswer: String): Boolean {
        val hexValue = String.format("#%06X", 0xFFFFFF and colorValue)
        val colors = Colors.colors
        val color = colors[hexValue]

        var isCorrect = false
        val answerYes = "yes"
        val answerNo = "no"
        if (colorName == color && userAnswer.lowercase() == answerYes
            || (colorName != color && userAnswer.lowercase() == answerNo)) {
            isCorrect = true
            _score.value = _score.value!!.plus(10)
        }

        return isCorrect
    }

    fun reinitializeGameData() {
        _score.value = 0
        wordsCounter = 0
        _wordsCount.value = wordsCounter
        getNextWords()
        getNextWordColors()
    }
}