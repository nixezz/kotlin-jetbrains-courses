package jetbrains.kotlin.course.hangman

fun getGameRules(wordLength: Int, maxAttemptsCount: Int) = "Welcome to the game!$newLineSymbol$newLineSymbol" +
        "In this game, you need to guess the word made by the computer.$newLineSymbol" +
        "The hidden word will appear as a sequence of underscores, one underscore means one letter.$newLineSymbol" +
        "You have $maxAttemptsCount attempts to guess the word.$newLineSymbol" +
        "All words are English words, consisting of $wordLength letters.$newLineSymbol" +
        "Each attempt you should enter any one letter,$newLineSymbol" +
        "if it is in the hidden word, all matches will be guessed.$newLineSymbol$newLineSymbol" +
        "" +
        "For example, if the word \"CAT\" was guessed, \"_ _ _\" will be displayed first, " +
        "since the word has 3 letters.$newLineSymbol" +
        "If you enter the letter A, you will see \"_ A _\" and so on.$newLineSymbol$newLineSymbol" +
        "" +
        "Good luck in the game!"

fun isWon(complete: Boolean, attempts: Int, maxAttemptsCount: Int) = complete && attempts <= maxAttemptsCount

fun isLost(complete: Boolean, attempts: Int, maxAttemptsCount: Int) = !complete && attempts > maxAttemptsCount

fun isComplete(secret: String, currentGuess: String): Boolean = secret == currentGuess.replace(separator, "")

fun generateNewUserWord(secret: String, guess: Char, currentUserWord: String): String {
    var newUserWord: String = ""

    for (i in secret.indices) {
        newUserWord += if (secret[i] == guess) {
            "${secret[i]}$separator"
        } else {
            "${currentUserWord[i * 2]}$separator"
        }
    }

    return newUserWord.removeSuffix(separator)
}

fun generateSecret(): String = words.random()

fun getHiddenSecret(wordLength: Int): String = List(wordLength) { underscore }.joinToString(separator)

fun isCorrectInput(userInput: String): Boolean {
    if(userInput.length != 1) {
        println("The length of your guess should be 1! Try again")
        return false
    } else if(!userInput[0].isLetter()) {
        println("You should input only English letters! Try again!")
        return false
    }
    return true
}

fun safeUserInput(): Char {
    var guess: String
    do {
        print("Please input your guess.")
        guess = safeReadLine()
    } while (!isCorrectInput(guess))
    return guess.uppercase()[0]
}

fun getRoundResults(secret: String, guess: Char, currentUserWord: String): String {
    return if (!secret.contains(guess)) {
        println("Sorry, the secret does not contain the symbol: $guess. The current word is $currentUserWord")
        currentUserWord
    } else {
        generateNewUserWord(secret, guess, currentUserWord).also {
            println("Great! This letter is in the word! The current word is $it")
        }
    }
}

fun playGame(secret: String, maxAttemptsCount: Int): Unit {
    var attempts: Int = 0
    var currentGuess: String = getHiddenSecret(secret.length)
    var complete: Boolean = false

    do {
        attempts++
        val guess = safeUserInput()
        currentGuess = generateNewUserWord(secret, guess, currentGuess)
        println(getRoundResults(secret, guess, currentGuess))
        complete = isComplete(secret, currentGuess)
    } while(attempts <= maxAttemptsCount && !complete)

    if(isWon(complete, attempts, maxAttemptsCount)) {
        println("Congratulations! You guessed it!")
    }

    if(isLost(complete, attempts, maxAttemptsCount)) {
        println("Sorry, you lost! My word is $secret")
    }
}

fun main() {
    println(getGameRules(wordLength, maxAttemptsCount))
    playGame(generateSecret(), maxAttemptsCount)
}
