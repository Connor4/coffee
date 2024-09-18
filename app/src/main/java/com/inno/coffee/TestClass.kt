package com.inno.coffee

fun main() {
    val list1 = mutableListOf(2, 3)
    val list2 = mutableListOf(2, 3, 5, 6)
    val result = list2.none { 1 == it }
    print("result $result")
}