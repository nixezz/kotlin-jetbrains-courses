package jetbrains.kotlin.course.last.push

fun getPattern(): String {
    println(
        "Do you want to use a pre-defined pattern or a custom one? " +
                "Please input 'yes' for a pre-defined pattern or 'no' for a custom one"
    )
    do {
        when (safeReadLine()) {
            "yes" -> {
                return choosePattern()
            }
            "no" -> {
                println("Please, input a custom picture")
                return safeReadLine()
            }
            else -> println("Please input 'yes' or 'no'")
        }
    } while (true)
}

fun choosePattern(): String {
    do {
        println("Please choose a pattern. The possible options: ${allPatterns().joinToString(", ")}")
        val name = safeReadLine()
        val pattern = getPatternByName(name)
        pattern?.let {
            return@choosePattern pattern
        }
    } while (true)
}

fun chooseGenerator(): String {
    var toContinue = true
    var generator = ""
    println("Please choose the generator: 'canvas' or 'canvasGaps'.")
    do {
        when (val input = safeReadLine()) {
            "canvas", "canvasGaps" -> {
                toContinue = false
                generator = input
            }
            else -> println("Please, input 'canvas' or 'canvasGaps'")
        }
    } while (toContinue)
    return generator
}

fun safeReadLine(): String = readlnOrNull() ?: error("Your input is incorrect, sorry")

fun fillPatternRow(patternRow: String, patternWidth: Int) = if (patternRow.length <= patternWidth) {
    val filledSpace = "$separator".repeat(patternWidth - patternRow.length)
    "$patternRow$filledSpace"
} else {
    error("patternRow length > patternWidth, please check the input!")
}

fun getPatternHeight(pattern: String) = pattern.lines().size

fun repeatHorizontally(pattern: String, n: Int, patternWidth: Int): String {
    val pictureRows = pattern.lines()
    val sb = StringBuilder()
    for (row in pictureRows) {
        val currentRow = fillPatternRow(row, patternWidth)
        sb.append(currentRow.repeat(n))
        sb.append(newLineSymbol)
    }
    return sb.toString()
}

fun dropTopLine(image: String, width: Int, patternHeight: Int, patternWidth: Int): String = if (patternHeight > 1) {
    val expectedFirstLineLength = patternWidth * width
    val firstNewLineIndex = image.indexOf(newLineSymbol)
    if (firstNewLineIndex != -1 && firstNewLineIndex == expectedFirstLineLength) {
        image.drop(firstNewLineIndex + newLineSymbol.length)
    } else {
        val lines = image.lines()
        if (lines.isNotEmpty()) lines.drop(1).joinToString(newLineSymbol) else image
    }
} else image

fun baseGenerator(firstLine: String, secondLine: String, height: Int): String {
    val sb = StringBuilder()
    sb.append(firstLine)
    return when {
        height < 1 -> ""
        height == 1 -> sb.toString()
        else -> {
            sb.append(secondLine)
            sb.append(repeatVertically(firstLine, secondLine, height))
            sb.toString()
        }
    }
}

fun repeatVertically(firstLine: String, secondLine: String, height: Int): String {
    val currentHeight = makeEvenNumber(height) / 2 - 1
    val pattern = "$firstLine$secondLine".repeat(currentHeight)
    return if (height % 2 == 0) {
        pattern
    } else {
        "$pattern$firstLine"
    }
}

fun makeEvenNumber(number: Int) = if (number % 2 == 0) {
    number
} else {
    number - 1
}

fun canvasGenerator(pattern: String, width: Int, height: Int): String {
    val patternWidth = getPatternWidth(pattern)
    val patternHeight = getPatternHeight(pattern)
    val repeatedPattern = repeatHorizontally(pattern, width, patternWidth)
    val dropped = dropTopLine(repeatedPattern, width, patternHeight, patternWidth)
    return "$repeatedPattern${baseGenerator(dropped, dropped, height - 1)}"
}

fun addEmptyWindow(patternRow: String, patternWidth: Int, toAddAfter: Boolean = false): String {
    val separator = "$separator".repeat(patternWidth)
    return if (toAddAfter) {
        "$patternRow$separator"
    } else {
        "$separator$patternRow"
    }
}

fun repeatHorizontallyWithGaps(pattern: String, n: Int, toAddSeparatorAfter: Boolean): String {
    val pictureRows = pattern.lines()
    val patternWidth = getPatternWidth(pattern)
    val sb = StringBuilder()
    for (row in pictureRows) {
        val currentRow = fillPatternRow(row, patternWidth)

        val currentRwSb = StringBuilder()
        val currentWidth = makeEvenNumber(n) / 2
        val patternToRepeat = addEmptyWindow(currentRow, patternWidth, toAddSeparatorAfter).repeat(currentWidth)
        if (patternToRepeat.isEmpty()) {
            currentRwSb.append(currentRow)
        } else {
            currentRwSb.append(patternToRepeat)
        }
        if (n % 2 != 0 && patternToRepeat.isNotEmpty()) {
            currentRwSb.append(patternToRepeat.dropLast(patternToRepeat.length - currentRow.length))
        }
        sb.append(currentRwSb.toString())
        sb.append(newLineSymbol)
    }
    return sb.toString()
}

fun canvasWithGapsGenerator(pattern: String, width: Int, height: Int): String {
    val firstLine = repeatHorizontallyWithGaps(pattern, width, true)
    val secondLine = repeatHorizontallyWithGaps(pattern, width, false)
    return baseGenerator(firstLine, secondLine, height)
}

fun applyGenerator(pattern: String, generatorName: String, width: Int, height: Int): String {
    return when (generatorName) {
        "canvas" -> canvasGenerator(pattern, width, height)
        "canvasGaps" -> canvasWithGapsGenerator(pattern, width, height)
        else -> error("generator name is invalid")
    }
}

fun main() {
    val pattern = getPattern()
    val generatorName = chooseGenerator()
    println("Please input the width of the resulting picture:")
    val width = safeReadLine().toInt()
    println("Please input the height of the resulting picture:")
    val height = safeReadLine().toInt()

    println("The pattern:$newLineSymbol${pattern.trimIndent()}")

    println("The generated image:")
    println(applyGenerator(pattern, generatorName, width, height))
}
