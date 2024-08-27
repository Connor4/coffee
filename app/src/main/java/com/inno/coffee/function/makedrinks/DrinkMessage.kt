package com.inno.coffee.function.makedrinks

import com.inno.coffee.utilities.INVALID_INT
import java.util.LinkedList
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class DrinkMessage {

    companion object {
        private val lock = ReentrantLock()
        private const val MAX_POOL_SIZE = 10
        private val poolList = LinkedList<DrinkMessage>()

        fun obtainMessage(id: Int): DrinkMessage {
            lock.withLock {
                val message: DrinkMessage
                if (poolList.isNotEmpty() and (poolList.size < MAX_POOL_SIZE)) {
                    message = poolList.first.apply { productId = id }
                } else {
                    message = DrinkMessage().apply { productId = id }
                }
                return message
            }
        }

        fun recycleMessage(message: DrinkMessage) {
            lock.withLock {
                if (poolList.size < MAX_POOL_SIZE) {
                    message.productId = INVALID_INT
                    message.next = null
                    poolList.offer(message)
                }
            }
        }

    }


    var next: DrinkMessage? = null
    var productId: Int = INVALID_INT

    override fun toString(): String {
        val next = if (next != null) {
            "next.productId ${next.toString()}"
        } else {
            ""
        }
        return "productId: $productId $next"
    }
}