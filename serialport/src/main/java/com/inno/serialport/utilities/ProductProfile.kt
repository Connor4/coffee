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