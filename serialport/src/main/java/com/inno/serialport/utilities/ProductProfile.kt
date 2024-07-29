package com.inno.serialport.utilities

import kotlinx.serialization.Serializable

@Serializable
data class ComponentProfile(val componentId: Short, val para: ShortArray = shortArrayOf())

@Serializable
data class ComponentProfileList(val componentNum: Short, val componentList: List<ComponentProfile>)

@Serializable
data class ProductProfile(val productId: Short, val preFlush: Short, val postFlush: Short, val
componentProfileList: ComponentProfileList)

//typedef struct{
//	unsigned short componentId;
//	short para[6];
//}componentProfile_t;
//
//typedef struct{
//	unsigned short componentNum;
//	componentProfile_t componentProfile[MAX_COMPONENT];
//}componentProList_t;
//
//typedef struct{
//	unsigned short productId;
//	unsigned short ifPreFlush;
//	unsigned short ifPostFlush;
//	componentProList_t componentList;
//}productProfile_t;

//productProfile_t productProfile1 = ////美式，先出水后咖啡
//{
//	1, //productId
//	0,
//	0,
//	{
//		7, //componentNum
//		{
//			{
//				FRONT_GRINDER_ID, //componentId
//				{//para
//					 140, 0, 0, 0, 0, 0////
//				},
//			},
//
//			{
//				LEFT_BREWER_ID, //componentId
//				{//para
//					 20, 800, 2000, 0, 0, 0////
//				},
//			},
//
//			{
//				LEFT_BOILER_ID, //componentId
//				{//para
//					 50, 150, 0, 1, 0, 0////
//				},
//			},
//
//			{
//				WATER_INPUT_PUMP_ID, //componentId
//				{//para
//					 0, 0, 0, 0, 0, 0////
//				},
//			},
//
//			{
//				WATER_INPUT_VALVE_ID, //componentId
//				{//para
//					 0, 0, 0, 0, 0, 0////
//				},
//			},
//
//			{
//				MIDILE_VALVE_LEFT_BOILER_ID, //componentId
//				{//para
//					 0, 0, 0, 0, 0, 0////
//				},
//			},
//
//			{
//				RIGHT_VALVE_LEFT_BOILER_ID, //componentId
//				{//para
//					 0, 0, 0, 0, 0, 0////
//				},
//			},
//
//		},
//	},
//};
// send
// 7E //包头
// 02 //地址
// 01 //控制
// 6C 00 //长度
// 64 00 //指令
// 01 00 // product id
// 00 00 // ifPreFlush
// 00 00 // ifPostFlush
// 07 00 // componentNum
// B8 0B // componentId
// 8C 00 00 00 00 00 00 00 00 00 00 00 // para
// E8 03
// 14 00 20 03 D0 07 00 00 00 00 00 00
// D0 07
// 32 00 96 00 00 00 01 00 00 00 00 00
// 88 13
// 00 00 00 00 00 00 00 00 00 00 00 00
// A0 0F
// 00 00 00 00 00 00 00 00 00 00 00 00
// A2 0F
// 00 00 00 00 00 00 00 00 00 00 00 00
// A3 0F
// 00 00 00 00 00 00 00 00 00 00 00 00
// C1 38 // crc
// 7E

// replay
//7E //包头
//02 //地址
//02 //控制
//04 00 //长度
//64 00 //指令
//00 00 //结果： 0：success, 其他： fault
//FC BB //crc
//7E
