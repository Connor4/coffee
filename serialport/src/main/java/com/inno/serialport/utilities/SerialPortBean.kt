package com.inno.serialport.utilities

import kotlinx.serialization.Serializable

sealed class ReceivedData {
    data class ErrorData(var info: String, var reboot: Boolean = false) : ReceivedData()
    data class PartData(var info: String) : ReceivedData()
    data class HeartBeat(var heartbeatStatus: Boolean = false, var reboot: Boolean = false,
        var info: String = "") : ReceivedData()
}

@Serializable
data class PullBufInfo(val id: Int, val pollBuf: ByteArray = byteArrayOf())

@Serializable
data class SingleComponent(var componentId: Short, var dosage: ShortArray = ShortArray(4)) {
    override fun toString(): String {
        return "SingleComponent(componentId=$componentId, dosage=${dosage.contentToString()})"
    }
}

@Serializable
data class ComponentList(val componentNum: Short, val singleComponent: List<SingleComponent>) {
    override fun toString(): String {
        return "ComponentList(componentNum=$componentNum, singleComponent=$singleComponent)"
    }
}

@Serializable
data class SingleTree(
    val treeNr: Short,
    val componentId: Short,
    val sendComponentId: Short,
    val receiveComponentId: Short,
    val conflictComponentId: Short
) {
    override fun toString(): String {
        return "SingleTree(treeNr=$treeNr, componentId=$componentId, " +
                "sendComponentId=$sendComponentId, receiveComponentId=$receiveComponentId, " +
                "conflictComponentId=$conflictComponentId)"
    }
}

@Serializable
data class TreeList(val treeLen: Short, val singleTree: List<SingleTree>) {
    override fun toString(): String {
        return "TreeList(treeLen=$treeLen, singleTree=$singleTree)"
    }
}

@Serializable
data class ProductInfo(
    val productId: Short,
    val componentList: ComponentList,
    val treeList: TreeList
) {
    override fun toString(): String {
        return "ProductInfo(productId=$productId, componentList=$componentList, treeList=$treeList)"
    }
}


//typedef struct
//{
//	uint16_t id; // 错误码或者信息码
//	int8_t PollBuf[16]; // 详细内容，当前未定义
//	// PollData_t PollBuf;
//}PollBufInfo_t;
//
//typedef struct{
//	unsigned short componentId;
//	short dosage[4];
//}singleComponent_t;
//
//typedef struct{
//	unsigned short componentNum;
//	singleComponent_t singleComponent[MAX_COMPONET];
//}componentList_t;
//
//
//typedef struct{
//	unsigned short treeNr;
//	unsigned short componentId;
//	unsigned short sendComponentId;
//	unsigned short receiveComponentId;
//	unsigned short conflictComponentId;
//}singleTree_t;
//
//typedef struct{
//	unsigned short treeLen;
//	singleTree_t singleTree[MAX_TREE_CONNECTION];
//}treeList_t;
//
//typedef struct{
//	unsigned short productId;
//	componentList_t componentList;
//	treeList_t treeList;
//}productInfo_t;

//#define MAX_COMPONET 		64
//#define MAX_TREE_CONNECTION   64

//3.做产品指令
//MSG_MAKE_PRODUCT = 100
//#define MAX_COMPONET 		2
//#define MAX_TREE_CONNECTION 2
//详解：
//7E // flag
//02 // addr
//01 // control
//30 00 //len 48
//64 00 //cmd 100
//01 00 //product id
//02 00 //componentNum
//01 00 //componentId
//12 34 56 78 ab cd ef 01 //dosage[4]
//02 00 //componentId
//12 34 56 78 ab cd ef 02 //dosage[4]
//02 00 //treeLen
//01 00 //treeNr
//12 34 //componentId
//56 78 //sendComponentId
//ab cd //receiveComponentId
//ef 01 //conflictComponentId
//02 00 //treeNr
//12 34 //componentId
//56 78 //sendComponentId
//ab cd //receiveComponentId
//ef 02 //conflictComponentId
//d2 ad //crc
//7E // flag

// poll指令
//7e 02 01 02 00 01 00 cc 2b 7e //send
//7E 02 02 04 00 01 00 00 00 4F 4C 7E //reply
//7E //包头
//02 //地址
//02 //控制
//04 00 //长度
//64 00 //指令
//00 00 //结果： 0：success, 其他： fault
//FC BB //crc
//7E //reply