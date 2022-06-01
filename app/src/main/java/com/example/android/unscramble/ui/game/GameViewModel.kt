package com.example.android.unscramble.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val _score = MutableLiveData(0)
    private val _currentWordCount = MutableLiveData(0)
    private val _currentScrambledWord = MutableLiveData<String>()

    private var wordList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord
    val score: LiveData<Int>
        get() = _score
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount


    /*
    * Picks a word from allWordList then converts it to char and shuffles it
    * after that it checks if word has been used before if not it increases the currentWordCount
    * and passes shuffled word to _currentScrambledWord
    */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordList.add(currentWord)
        }
    }

    /*
   * Returns true if the current word count is less than MAX_NO_OF_WORDS.
   * Updates the next word.
   */
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        } else
            return false
    }

    /*
    * Re-initializes the game data to restart the game.
    */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWord()
    }


    init {
        getNextWord()
    }

}