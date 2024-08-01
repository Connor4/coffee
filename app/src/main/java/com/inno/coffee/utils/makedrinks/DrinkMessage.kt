package com.inno.coffee.utils.makedrinks

import okio.withLock
import java.util.LinkedList
import java.util.concurrent.locks.ReentrantLock

class DrinkMessage {

    companion object {
        private val lock = ReentrantLock()
        private const val MAX_POOL_SIZE = 10
        private val poolList = LinkedList<DrinkMessage>()

        fun obtainMessage(id: Int): DrinkMessage {
            lock.withLock {
                val message: DrinkMessage
                if (poolList.isNotEmpty() and (poolList.size < MAX_POOL_SIZE)) {
                    message = poolList.first.apply { actionId = id }
                } else {
                    message = DrinkMessage().apply { actionId = id }
                }
                return message
            }
        }

        fun recycleMessage(message: DrinkMessage) {
            lock.withLock {
                if (poolList.size < MAX_POOL_SIZE) {
                    message.actionId = -1
                    message.next = null
                    poolList.offer(message)
                }
            }
        }

    }


    var next: DrinkMessage? = null
    var actionId: Int = -1

    override fun toString(): String {
        val next = if (next != null) {
            "next.actionId ${next.toString()}"
        } else {
            ""
        }
        return "actionId: $actionId $next"
    }
}