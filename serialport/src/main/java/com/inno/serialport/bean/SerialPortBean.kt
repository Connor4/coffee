package com.inno.serialport.bean

import kotlinx.serialization.Serializable

@Serializable
data class PullBufInfo(val id: Int, val pollBuf: ByteArray)

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
//	uint16_t id;
//	int8_t PollBuf[16];
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
//7E
//02
//01
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
//7E

