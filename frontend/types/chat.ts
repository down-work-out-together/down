export interface GetGroupChatMessagesType {
  chatRoomId: number;
  lastChatMessageId: number;
  size: number;
}

export interface CreateGroupChatRoomType {
  userId: number | undefined;
  userIdList: Array<number | undefined>;
  chatRoomName: string;
}

export interface GetGroupChatRoomType {
  chatRoomId: number;
}

export interface InviteGroupChatRoomType {
  inviterId: number | undefined;
  targetId: number;
  chatRoomId: number;
}

export interface LeaveGroupChatRoomType {
  userId: number | undefined;
}
