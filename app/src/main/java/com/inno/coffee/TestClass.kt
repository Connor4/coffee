package com.inno.coffee

import java.util.concurrent.atomic.AtomicInteger

fun main() {
    val atomic = AtomicInteger(0)
    val step1 = atomic.getAndIncrement()
    val step2 = atomic.getAndIncrement()
    val step3 = atomic.getAndIncrement()
    println("step1: $step1, step2: $step2, step3: $step3 ${atomic.get()}")

}