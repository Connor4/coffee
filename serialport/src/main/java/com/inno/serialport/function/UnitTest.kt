package com.inno.serialport.function

import com.inno.serialport.bean.ComponentList
import com.inno.serialport.bean.ProductInfo
import com.inno.serialport.bean.SingleComponent
import com.inno.serialport.bean.SingleTree
import com.inno.serialport.bean.TreeList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    val info = createInfo()
    println("info: $info")
    val infoString = Json.encodeToString(info)
    println("string: $infoString")
    val driver = RS485Driver()
    driver.send(infoString)
}

private fun createInfo(): ProductInfo {
    val productInfo = ProductInfo(
        productId = 0x0001,
        componentList = ComponentList(
            componentNum = 2,
            singleComponent = listOf(
                SingleComponent(
                    componentId = 0x0001,
                    dosage = shortArrayOf(0x34, 0x56, 0xAB, 0xEF)
                ),
                SingleComponent(
                    componentId = 0x0002,
                    dosage = shortArrayOf(0x12, 0x78, 0xCD, 0x02)
                )
            )
        ),
        treeList = TreeList(
            treeLen = 2,
            singleTree = listOf(
                SingleTree(
                    treeNr = 0x01,
                    componentId = 0x12,
                    sendComponentId = 0x56,
                    receiveComponentId = 0xAB,
                    conflictComponentId = 0xEF
                ),
                SingleTree(
                    treeNr = 0x0002,
                    componentId = 0x1234,
                    sendComponentId = 0x5678,
                    receiveComponentId = 0xAB,
                    conflictComponentId = 0xEF
                )
            )
        )
    )
    return productInfo
}