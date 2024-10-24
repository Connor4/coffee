package com.inno.coffee

import com.inno.coffee.utilities.previewFormula

fun main() {
    println("for $previewFormula")
//    val value = if (previewFormula.cups?.double != -1) 2 else 1
    val repeatTimes =
        if (previewFormula.cups == null) 1 else if (previewFormula.cups?.double != -1) 2 else 1
    println("value $repeatTimes")
}