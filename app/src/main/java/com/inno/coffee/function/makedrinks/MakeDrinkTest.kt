package com.inno.coffee.function.makedrinks

private var messageHead: DrinkMessage? = null

fun main() {
    val head = DrinkMessage.obtainMessage(1)
    head.next = DrinkMessage.obtainMessage(2)
    head.next?.next = DrinkMessage.obtainMessage(3)
    head.next?.next?.next = DrinkMessage.obtainMessage(4)
    head.next?.next?.next?.next = DrinkMessage.obtainMessage(5)
    messageHead = head

    println("Original chain: $messageHead")
    recycleMessage(0)
    println("After chain: $messageHead")
}

private fun recycleMessage(index: Int) {
    if (messageHead == null || index < 0) {
        return
    }

    if (index == 0) {
        messageHead = messageHead?.next
        return
    }

    var current = messageHead
    var currentIndex = 0
    while (current != null && currentIndex < index - 1) {
        current = current.next
        currentIndex++
    }
    if (current?.next != null) {
        val p = current.next
        current.next = current.next?.next
        DrinkMessage.recycleMessage(p!!)
    }
}