package com.inno.coffee.data.home

import com.inno.serialport.driver.RS485Driver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SerialPortDataSource @Inject constructor() {
    val driver = RS485Driver()

    fun getData() {
        driver.receive()
    }

}