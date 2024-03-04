"use client";
import { useState } from "react";
import * as z from "zod";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";
import { basicUserInfoFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

import { postCheckNickname, postSignUp } from "@/api/signup";

import { StepProps, NickNameCheckResponse } from "@/app/(auth)/types/signup";

import { cn } from "@/lib/cn";

import {
	Input,
	Button,
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
	Select,
	SelectTrigger,
	SelectValue,
	SelectContent,
	SelectItem,
} from "@/components/ui";

const BasicUserInfoStep = ({ onNext }: StepProps) => {
	const { signUpUserInfo, setSignUpUserInfo } = useSignupContext();
	const { method } = useCommonForm({
		schema: basicUserInfoFieldSchema,
		checkMode: "onBlur",
		defaultValues: {
			nickname: "",
			gender: "male",
			birthYear: "",
		},
	});
	const [nickNameCheckResponse, setNickNameCheckResponse] =
		useState<NickNameCheckResponse | null>(null);

	const handleCheckNickname = async (nickname: string) => {
		if (!nickname) {
			method.setError("nickname", { message: "닉네임을 입력해주세요." });
			setNickNameCheckResponse(null);
			return;
		}

		try {
			const response = await postCheckNickname(nickname);
			setSignUpUserInfo({ ...signUpUserInfo, nickname });
			setNickNameCheckResponse(response.data);
		} catch (error) {
			if (error instanceof Error) {
				setSignUpUserInfo({ ...signUpUserInfo, nickname: "" });
				setNickNameCheckResponse({ success: false, message: error.message });
			} else {
				console.log(error);
			}
		}
	};

	const handleSignUp = async (
		values: z.infer<typeof basicUserInfoFieldSchema>
	) => {
		if (!nickNameCheckResponse?.success) return;

		const gender = values.gender;
		const birthYear = values.birthYear;

		setSignUpUserInfo({ ...signUpUserInfo, gender, birthYear });

		const newUserInfo = {
			email: signUpUserInfo.email,
			password: signUpUserInfo.password,
			nickName: signUpUserInfo.nickname,
			gender: signUpUserInfo.gender,
			birth: signUpUserInfo.birthYear,
			userName: signUpUserInfo.nickname,
			code: signUpUserInfo.emailCode,
			termsAgree: signUpUserInfo.termsAgree,
		};

		console.log(newUserInfo);

		try {
			const response = await postSignUp(newUserInfo);
			console.log(response);
			onNext();
		} catch (error) {
			console.log(error);
		}
	};

	return (
		<FormFieldWrapper
			method={method}
			onSubmit={handleSignUp}
		>
			<FormField
				control={method.control}
				name="nickname"
				render={({ field }) => (
					<FormItem>
						<FormLabel>닉네임</FormLabel>
						<div className="flex gap-2">
							<FormControl>
								<Input
									placeholder="닉네임을 입력해주세요."
									{...field}
								/>
							</FormControl>
							<Button
								type="button"
								onClick={() => handleCheckNickname(field.value)}
							>
								중복확인
							</Button>
						</div>
						<FormMessage />
						{nickNameCheckResponse && (
							<p
								className={cn(
									"text-sm font-medium",
									nickNameCheckResponse.success
										? "text-stone-500"
										: "text-destructive"
								)}
							>
								{nickNameCheckResponse.message}
							</p>
						)}
					</FormItem>
				)}
			/>

			<FormField
				control={method.control}
				name="gender"
				render={({ field }) => (
					<FormItem>
						<FormLabel>성별</FormLabel>
						<Select
							onValueChange={field.onChange}
							defaultValue={field.value}
						>
							<FormControl>
								<SelectTrigger>
									<SelectValue placeholder="성별을 선택해주세요." />
								</SelectTrigger>
							</FormControl>
							<SelectContent>
								<SelectItem value="male">남자</SelectItem>
								<SelectItem value="female">여자</SelectItem>
							</SelectContent>
						</Select>
						<FormMessage />
					</FormItem>
				)}
			/>

			<FormField
				control={method.control}
				name="birthYear"
				render={({ field }) => {
					const currentYear = new Date().getFullYear();
					const minAge = 14;
					const maxAge = 65;
					const startYear = currentYear - maxAge;
					const endYear = currentYear - minAge;

					return (
						<FormItem>
							<FormLabel>출생연도</FormLabel>
							<Select
								onValueChange={field.onChange}
								defaultValue={field.value}
							>
								<FormControl>
									<SelectTrigger>
										<SelectValue placeholder="출생연도를 선택해주세요." />
									</SelectTrigger>
								</FormControl>
								<SelectContent>
									{[...Array(endYear - startYear + 1).keys()].map((i) => (
										<SelectItem
											key={i}
											value={(endYear - i).toString()}
										>
											{endYear - i}
										</SelectItem>
									))}
								</SelectContent>
							</Select>
							<FormMessage />
						</FormItem>
					);
				}}
			/>

			<Button
				className="w-full"
				type="submit"
			>
				다음
			</Button>
		</FormFieldWrapper>
	);
};

export default BasicUserInfoStep;
