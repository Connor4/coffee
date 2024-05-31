package com.inno.coffee.data.home

import javax.inject.Inject

class SerialPortRepository @Inject constructor(
    private val dataSource: SerialPortDataSource
) {

}