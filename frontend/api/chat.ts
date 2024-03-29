import instance from "@/lib/axios/instance";
import {
  CreateGroupChatRoomType,
  GetGroupChatMessagesType,
  GetGroupChatRoomType,
  InviteGroupChatRoomType,
  LeaveGroupChatRoomType,
} from "@/types/chat";

// 채팅방 [그룹]

// 채팅방 생성
export const createGroupChatRoom = async (body: CreateGroupChatRoomType) => {
  try {
    const response = await instance.post(`/group/chatRoom`, body);
    return response.data;
  } catch (error) {
    return error;
  }
};

// 채팅방 초대
export const inviteGroupChat = async (body: InviteGroupChatRoomType) => {
  try {
    const response = await instance.post(`/group/chatRoom/invite`, body);
    console.log(response.data);
    return response.data;
  } catch (error) {}
};

// 채팅방 정보 조회
export const getGroupChatRoom = async (chatRoomId: string | string[]) => {
  try {
    const response = await instance.get(`/group/chatRoom/${chatRoomId}`);
    return response.data;
  } catch (error) {
    return error;
  }
};

// 채팅방 목록 조회
export const getGroupChatRooms = async (userId: number) => {
  try {
    const response = await instance.get(`/group/chatRoom/list/${userId}`);
    return response.data;
  } catch {}
};

// 채팅방 퇴장
export const leaveGroupChatRoom = async (
  chatRoomId: number | string | string[],
  body: LeaveGroupChatRoomType
) => {
  try {
    const response = await instance.patch(
      `/group/chatRoom/${chatRoomId}/exit`,
      body
    );
    return response.data;
  } catch (error) {}
};

// 채팅 메세지 [그룹]

// 채팅 메세지 조회
export const getGroupChatMessages = async ({
  chatRoomId,
  lastChatMessageId,
  size,
}: GetGroupChatMessagesType) => {
  try {
    const response = await instance.get(`/chat/message`, {
      params: {
        chatRoomId,
        lastChatMessageId,
        size,
      },
    });
    return response.data;
  } catch (error) {
    return error;
  }
};

// 채팅방 [개인]

// 채팅방 퇴장
export const leavePrivateChatRoom = async (chatRoomId: number, body: any) => {
  try {
    const response = await instance.patch(
      `/private/chatRoom/${chatRoomId}/exit`,
      body
    );
    return response.data;
  } catch (error) {}
};

// 채팅방 목록 조회
