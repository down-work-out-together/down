"use client";
import { useEffect, useState } from "react";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";

import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";
import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";
import { emailCheckFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

import { EmailCheckResponse } from "@/app/(auth)/types/signup";

import { postEmailCheck } from "@/api/signup";

import { cn } from "@/lib/cn";

import {
	FormField,
	FormItem,
	FormMessage,
	FormControl,
	Input,
} from "@/components/ui";

const EmailCheckField = () => {
	const { signUpUserInfo, setSignUpUserInfo } = useSignupContext();

	const { method } = useCommonForm({
		schema: emailCheckFieldSchema,
		checkMode: "onChange",
		defaultValues: { email: "" },
	});
	const { errors } = method.formState;

	const [emailCheckResponse, setEmailCheckResponse] =
		useState<EmailCheckResponse | null>(null);

	const handleEmailCheck = async (email: string) => {
		const isInputValid = await method.trigger("email");
		if (!isInputValid) return;

		try {
			const response = await postEmailCheck(email);
			setSignUpUserInfo({ ...signUpUserInfo, emailCheck: true, email });
			setEmailCheckResponse(response.data);
		} catch (error) {
			if (error instanceof Error) {
				console.log(error);
				setSignUpUserInfo({ ...signUpUserInfo, emailCheck: false, email: "" });
				setEmailCheckResponse({ success: false, message: error.message });
			} else {
				console.log(error);
			}
		}
	};

	const emailValue = method.watch("email");
	useEffect(() => {
		setEmailCheckResponse(null);
		setSignUpUserInfo({ ...signUpUserInfo, emailCheck: false, email: "" });
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [emailValue]);

	return (
		<FormFieldWrapper
			method={method}
			onSubmit={handleEmailCheck}
		>
			<FormField
				control={method.control}
				name="email"
				render={({ field }) => (
					<FormItem>
						<div className="flex space-x-2">
							<FormControl>
								<Input
									{...field}
									onBlur={() => handleEmailCheck(field.value)}
									placeholder="email@example.com"
									className={cn(errors.email ? "border-destructive" : "")}
								/>
							</FormControl>
						</div>
						<FormMessage
							className={cn(
								emailCheckResponse?.success
									? "text-stone-500"
									: "text-destructive",
								"text-sm"
							)}
						>
							{emailCheckResponse?.message}
						</FormMessage>
					</FormItem>
				)}
			/>
		</FormFieldWrapper>
	);
};

export default EmailCheckField;
