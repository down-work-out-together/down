import { z } from "zod";

export const emailCheckFieldSchema = z.object({
	email: z
		.string()
		.min(1, "이메일을 입력해주세요.")
		.email("유효하지 않은 이메일 형식입니다."),
});

export const emailCodeFieldSchema = z.object({
	emailCode: z
		.string()
		.min(1, "이메일 인증코드를 입력해주세요.")
		.length(6, { message: "인증코드를 올바르게 입력해주세요." }),
});

export const passwordFieldSchema = z
	.object({
		password: z
			.string()
			.min(1, "비밀번호를 입력해주세요.")
			.refine(
				(value) => /^(?=.*[a-zA-Z])(?=.*\d).{8,20}$/.test(value),
				"영문과 숫자를 포함하여 8~20자로 입력해 주세요."
			),
		passwordCheck: z.string().min(1, "비밀번호 확인을 입력해주세요."),
	})
	.refine((data) => data.password === data.passwordCheck, {
		message: "비밀번호와 비밀번호 확인이 일치하지 않습니다.",
		path: ["passwordCheck"],
	});

export const basicUserInfoFieldSchema = z.object({
	nickname: z
		.string()
		.min(1, "닉네임을 입력해주세요")
		.max(15, { message: "15자 이내로 입력해주세요." }),
	gender: z.enum(["male", "female"], {
		required_error: "성별을 선택해주세요.",
	}),
	birthYear: z.string().min(1, "출생연도를 선택해주세요."),
});

export const regionFieldSchema = z
	.object({
		regionDepth1: z.string().min(1, "지역을 선택해주세요"),
		regionDepth2: z.string().min(1, "세부 지역을 선택해주세요."),
	})
	.refine(
		(data) => {
			if (data.regionDepth2 && !data.regionDepth1) {
				return false;
			}
			return true;
		},
		{
			message: "지역을 먼저 선택해주세요.",
			path: ["regionDepth1"],
		}
	);

export const sportInfoFieldSchema = z.object({
	sport: z.string().min(1, "운동을 선택해주세요"),
	level: z.enum(["입문자", "초보자", "중급자", "마스터", "고인물", "신"], {
		required_error: "경력을 선택해주세요.",
	}),
});
