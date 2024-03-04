// 회원가입 양식필드 공통
export interface StepProps {
	onNext: () => void;
}

export type EmailCheckResponse = {
	success: boolean;
	data?: { checkedEmail: string; available: boolean } | { description: string };
	message: string;
};

export type EmailCodeSendingStatus = "sending" | "success" | "error" | null;

export type EmailCodeResponse = {
	success: boolean;
	data?: { checkedEmail: string; result: boolean } | { errorMessage: string };
	message: string;
};

export type NickNameCheckResponse = {
	success: boolean;
	data?: { nickName: string; available: boolean } | Object;
	message: string;
};

export interface NewUserInfo {
	email: string;
	password: string;
	nickName: string;
	gender: "male" | "female";
	birth: string;
	userName: string;
	code: string;
	termsAgree: boolean;
}
