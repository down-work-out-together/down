import instance from "@/lib/axios/instance";
import { NewUserInfo } from "@/app/(auth)/types/signup";

export const postEmailCheck = async (email: string) => {
	return await instance.post("/users/email/check", { email });
};

export const postSendEmailCode = async (email: string) => {
	return await instance.post("/users/email/send", { email });
};

export const postCheckEmailCode = async (email: string, code: string) => {
	return await instance.post("/users/email/verify", {
		email,
		code,
	});
};

export const postCheckNickname = async (nickName: string) => {
	return await instance.post("/users/nickname/check", { nickName });
};

export const postSignUp = async (newUserInfo: NewUserInfo) => {
	try {
		const response = await instance.post("/users", newUserInfo);
		return response.data;
	} catch (error) {
		return error;
	}
};
