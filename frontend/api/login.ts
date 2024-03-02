import instance from "@/lib/axios/instance";

export const postUserLogin = async (email: string, password: string) => {
	try {
		const response = await instance.post("/user/login", { email, password });
		return response.data;
	} catch (error) {
		return error;
	}
};

export const getKakaoLogin = async () => {};

export const getLogout = async () => {
	try {
		const response = await instance.get("/user/login/logout");
		return response.data;
	} catch (error) {
		return error;
	}
};
