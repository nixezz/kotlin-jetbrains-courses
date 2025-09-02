package jetbrains.kotlin.course.almost.done

fun trimPicture(picture: String): String = picture.trimIndent()

fun applyBordersFilter(picture: String): String {
    val pictureWidth = getPictureWidth(picture)

    val horizontalBorder: String = "$borderSymbol".repeat(pictureWidth + 4)
    val pictureWithBorder = StringBuilder()

    pictureWithBorder.append(horizontalBorder).append(newLineSymbol)
    picture.lines().forEach {
        val separationNumber: Int = pictureWidth - it.length + 1
        pictureWithBorder
            .append(borderSymbol)
            .append(separator)
            .append(it)
            .append(separator.toString().repeat(separationNumber))
            .append(borderSymbol)
            .append(newLineSymbol)
    }
    pictureWithBorder.append(horizontalBorder)
    return pictureWithBorder.toString()
}

fun applySquaredFilter(picture: String): String {
    val pictureWidth: Int = getPictureWidth(picture)

    val horizontalBorder: String = "$borderSymbol".repeat((pictureWidth + 4) * 2)
    val pictureWithBorder = StringBuilder()

    pictureWithBorder.append(horizontalBorder).append(newLineSymbol)
    drawTwo(pictureWithBorder, picture, pictureWidth)
    pictureWithBorder.append(horizontalBorder).append(newLineSymbol)
    drawTwo(pictureWithBorder, picture, pictureWidth)
    pictureWithBorder.append(horizontalBorder)
    return pictureWithBorder.toString()
}

fun safeReadLine(): String = readln() ?: error("Null value was received")

private fun drawTwo(
    pictureWithBorder: StringBuilder,
    picture: String,
    pictureWidth: Int
) {
    picture.lines().forEach {
        val separationNumber: Int = pictureWidth - it.length + 1

        pictureWithBorder
            .append(borderSymbol)
            .append(separator)
            .append(it)
            .append(separator.toString().repeat(separationNumber))
            .append(borderSymbol)
            .append(borderSymbol)
            .append(separator)
            .append(it)
            .append(separator.toString().repeat(separationNumber))
            .append(borderSymbol)
            .append(newLineSymbol)
    }
}

fun applyFilter(picture: String, filter: String): String =
    when (filter) {
        "borders" -> applyBordersFilter(picture)
        "squared" -> applySquaredFilter(picture)
        else -> error("Invalid filter")
    }

fun chooseFilter(): String {
    println("Please choose the filter: 'borders' or 'squared'")
    do {
        when (val input = safeReadLine()) {
            "borders", "squared" -> return input
            else -> println("Please input 'borders' or 'squared'")
        }
    } while (true)
}

fun choosePicture(): String {
    do {
        println("Please, choose a picture. The possible options: ${allPictures().toList()}")
        val picture: String = safeReadLine()
        getPictureByName(picture)?.let {
            return@choosePicture it
        }
    } while (true)
}

fun getPicture(): String {
    println("Do you want to use a pre-defined picture or use a custom one? Please, input 'yes' for a pre-defined image or 'no' for a custom one")
    do {
        when (safeReadLine()) {
            "yes" -> return choosePicture()
            "no" -> {
                println("Please input a custom picture")
                return safeReadLine()
            }
            else -> println("Please input 'yes' or 'no'")
        }
    } while (true)
}

fun photoshop(): Unit {
    val picture: String = trimPicture(getPicture())
    val filter: String = chooseFilter()

    println("The old image:")
    println(picture)
    println("The transformed picture:")
    println(applyFilter(picture, filter))

}

fun main() {
    photoshop()
}
