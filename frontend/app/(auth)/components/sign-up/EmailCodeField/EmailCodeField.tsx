"use client";
import { useState } from "react";
import { FieldValues } from "react-hook-form";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";

import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";
import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";
import { emailCodeFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

import {
	EmailCodeResponse,
	EmailCodeSendingStatus,
} from "@/app/(auth)/types/signup";

import { postCheckEmailCode, postSendEmailCode } from "@/api/signup";

import { cn } from "@/lib/cn";
import {
	Input,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
	FormControl,
	Button,
} from "@/components/ui";

const EmailCodeField = () => {
	const { signUpUserInfo, setSignUpUserInfo } = useSignupContext();

	const email = signUpUserInfo.email;
	const isEmailChecked = signUpUserInfo.emailCheck;

	const { method } = useCommonForm({
		schema: emailCodeFieldSchema,
		checkMode: "onSubmit",
		defaultValues: { emailCode: "" },
	});

	const [emailCodeSendingStatus, setEmailCodeSendingStatus] =
		useState<EmailCodeSendingStatus>(null);
	const handleSendCode = async () => {
		if (!email || !isEmailChecked) return;
		try {
			setEmailCodeSendingStatus("sending");
			const response = await postSendEmailCode(email);
			if (response.status === 200) {
				setEmailCodeSendingStatus("success");
			}
		} catch (error) {
			console.log(error);
			setEmailCodeSendingStatus("error");
		}
	};

	const [emailCodeCheckResponse, setEmailCodeCheckResponse] =
		useState<EmailCodeResponse | null>(null);
	const handleCheckCode = async (value: FieldValues) => {
		try {
			const emailCode = value.emailCode;
			const response = await postCheckEmailCode(email, emailCode);
			setSignUpUserInfo({ ...signUpUserInfo, emailCode });
			setEmailCodeCheckResponse(response.data);
		} catch (error) {
			if (error instanceof Error) {
				console.log(error);
				setSignUpUserInfo({ ...signUpUserInfo, emailCode: "" });
				setEmailCodeCheckResponse({ success: false, message: error.message });
			} else {
				console.log(error);
			}
		}
	};

	return (
		<FormFieldWrapper
			method={method}
			onSubmit={handleCheckCode}
		>
			<FormField
				control={method.control}
				name="emailCode"
				render={({ field }) => (
					<FormItem>
						<div className="flex space-x-2">
							<FormControl>
								<Input
									className={cn(
										emailCodeSendingStatus === "success" ? "block" : "hidden"
									)}
									placeholder="인증코드를 입력해주세요."
									{...field}
								/>
							</FormControl>

							{emailCodeSendingStatus !== "success" && (
								<Button
									disabled={
										!isEmailChecked || emailCodeSendingStatus === "sending"
									}
									onClick={handleSendCode}
									type="button"
								>
									인증코드 전송
								</Button>
							)}
							{emailCodeSendingStatus === "success" && (
								<Button>인증코드 확인</Button>
							)}
						</div>
						<FormMessage />
						{!emailCodeCheckResponse && (
							<p
								className={cn(
									emailCodeSendingStatus === "error" && "text-destructive",
									"text-sm text-stone-500"
								)}
							>
								{emailCodeSendingStatus === "sending" &&
									"인증코드 전송중입니다. 잠시만 기다려주세요."}
								{emailCodeSendingStatus === "success" &&
									"인증코드가 전송되었습니다. 메일이 오지 않았다면 스팸메일함을 확인해주세요."}
								{emailCodeSendingStatus === "error" &&
									"인증코드 전송 중 오류가 발생했습니다. 다시 시도해주세요."}
							</p>
						)}
						{
							<p
								className={cn(
									emailCodeCheckResponse?.success
										? "text-stone-500"
										: "text-destructive",
									"text-sm"
								)}
							>
								{emailCodeCheckResponse?.message}
							</p>
						}
					</FormItem>
				)}
			/>
		</FormFieldWrapper>
	);
};

export default EmailCodeField;
