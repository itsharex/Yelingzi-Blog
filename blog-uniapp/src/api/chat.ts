import request, { buildQuery } from '@/utils/request'

//聊天列表
export const getChatListService = (parmas : { cursor : number, direction : string, limit : number, type : string }, device : string) => {
	return request.post('/api/chat/list', parmas, { 'x-host': device })
}


//获取新消息总数
export const getNewChatCountBySingleService = (parmas : { device : string, id : number }) => {
	return request.get(buildQuery('/api/chat/single/new', { cursor: parmas.id }), null, { 'x-host': parmas.device })
}

export const getNewChatCountByGroupService = (parmas : { device : string, id : number }) => {
	return request.get(buildQuery('/api/chat/group/new', { cursor: parmas.id }), null, { 'x-host': parmas.device })
}

//发送文字消息
export const sendMessageService = (parmas : { type : string, message : string }, device : string) => {
	return request.post('/api/chat/send', parmas,
		{ 'x-host': device }
	)
}

//发送表情
export const sendEmojiMessageService = (parmas : { type : string, message : string }, device : string) => {
	return request.post('/api/chat/send/emoji', parmas,
		{ 'x-host': device }
	)
}

//发送图片消息
export function sendSingleImageService(filePath : string, deviceId : string) {
	return request.upload<{ msgId : string }>(
		'/api/chat/send/image',
		filePath,
		{ type: 'single' },
		{ 'x-host': deviceId },
		'image'
	)
}