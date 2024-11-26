package com.inno.coffee

fun main() {
    var intValue: Int = 0 // Initial value (all flags are 0)

    // Set flag 2
    intValue = setFlag(intValue, 2)
    println("After setting flag 2: ${
        intValue.toString(2).padStart(32, '0')
    }") // Output: 00000000...00000100

    // Check if flag 2 is set
    println("Is flag 2 set? ${isFlagSet(intValue, 2)}") // Output: true

    // Clear flag 2
    intValue = clearFlag(intValue, 2)
    println("After clearing flag 2: ${
        intValue.toString(2).padStart(32, '0')
    }") // Output: 00000000...00000000

    // Toggle flag 1
    intValue = toggleFlag(intValue, 1)
    println("After toggling flag 1: ${
        intValue.toString(2).padStart(32, '0')
    }") // Output: 00000000...00000010

    // Toggle flag 1 again
    intValue = toggleFlag(intValue, 1)
    println("After toggling flag 1 again: ${intValue.toString(2).padStart(32, '0')}") //

    intValue = toggleFlag(intValue, 2)
    println("After toggling flag 1 again: ${intValue.toString(2).padStart(32, '0')}") //
}

fun setFlag(intValue: Int, flagIndex: Int): Int {
    require(flagIndex in 0..31) { "Flag index must be between 0 and 31" }
    return intValue or (1 shl flagIndex)
}

fun clearFlag(intValue: Int, flagIndex: Int): Int {
    require(flagIndex in 0..31) { "Flag index must be between 0 and 31" }
    return intValue and (1 shl flagIndex).inv()
}

fun isFlagSet(intValue: Int, flagIndex: Int): Boolean {
    require(flagIndex in 0..31) { "Flag index must be between 0 and 31" }
    return (intValue and (1 shl flagIndex)) != 0
}

fun toggleFlag(intValue: Int, flagIndex: Int): Int {
    require(flagIndex in 0..31) { "Flag index must be between 0 and 31" }
    return intValue xor (1 shl flagIndex)
}

