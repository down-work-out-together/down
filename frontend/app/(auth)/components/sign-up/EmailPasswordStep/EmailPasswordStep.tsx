"use client";
import { useEffect } from "react";
import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";

import {
	EmailCheckField,
	EmailCodeField,
	PasswordField,
} from "@/app/(auth)/components/sign-up";

import { StepProps } from "@/app/(auth)/types/signup";

const EmailPasswordStep = ({ onNext }: StepProps) => {
	const { signUpUserInfo } = useSignupContext();
	useEffect(() => {
		const termsAgree = signUpUserInfo.termsAgree;
		const email = signUpUserInfo.email;
		const emailCheck = signUpUserInfo.emailCheck;
		const emailCode = signUpUserInfo.emailCode;
		const password = signUpUserInfo.password;

		if (termsAgree && email && emailCheck && emailCode && password) onNext();

		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [signUpUserInfo]);

	return (
		<div className="flex flex-col space-y-4">
			<EmailCheckField />
			<EmailCodeField />
			<PasswordField />
		</div>
	);
};

export default EmailPasswordStep;
