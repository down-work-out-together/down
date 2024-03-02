import instance from "@/lib/axios/instance";
import { NewUserInfo } from "@/app/(auth)/types/signup";

export const postEmailCheck = async (email: string) => {
	try {
		const response = await instance.post("/users/email/check", { email });
		return response.data;
	} catch (error) {
		return error;
	}
};

export const postSendEmailCode = async (email: string) => {
	try {
		const response = await instance.post("/users/email/send", { email });
		return response.data;
	} catch (error) {
		return error;
	}
};

export const postCheckEmailCode = async (email: string, code: string) => {
	try {
		const response = await instance.post("/users/email/verify", {
			email,
			code,
		});
		return response.data;
	} catch (error) {
		return error;
	}
};

export const postCheckNickname = async (nickName: string) => {
	try {
		const response = await instance.post("/users/nickname/check", { nickName });
		return response.data;
	} catch (error) {
		return error;
	}
};

export const postSignUp = async (newUserInfo: NewUserInfo) => {
	try {
		const response = await instance.post("/users", newUserInfo);
		return response.data;
	} catch (error) {
		return error;
	}
};
