"use client";
import { useState } from "react";
import {
	TermsAgreeStep,
	EmailPasswordStep,
} from "@/app/(auth)/components/sign-up";

// type SignUpStep = "약관동의" | "이메일및비밀번호" | "기본회원정보" | "지역" | "사용자운동정보";
type SignUpStep = "약관동의" | "이메일및비밀번호";

export const useStep = (initialStep: SignUpStep) => {
	const [currentStep, setCurrentStep] = useState(initialStep);

	const setStep = (nextStep: SignUpStep) => {
		setCurrentStep(nextStep);
	};

	const stepComponents = {
		약관동의: TermsAgreeStep,
		이메일및비밀번호: EmailPasswordStep,
	};

	const getCurrentComponent = () => {
		return stepComponents[currentStep] || null;
	};

	const onNext = () => {
		const stepKeys: SignUpStep[] = Object.keys(stepComponents) as SignUpStep[];
		const currentStepIndex = stepKeys.indexOf(currentStep);
		const nextStep = stepKeys[currentStepIndex + 1];
		if (nextStep) {
			setCurrentStep(nextStep);
		}
	};

	const CurrentComponent = getCurrentComponent();

	return { currentStep, setStep, onNext, CurrentComponent };
};
