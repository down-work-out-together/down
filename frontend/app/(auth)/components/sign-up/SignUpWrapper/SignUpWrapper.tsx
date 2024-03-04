"use client";
import { useStep } from "@/app/(auth)/hooks/sign-up/useStep";
import { SignupContextProvider } from "@/app/(auth)/contexts/sign-up/SignUpContext";

const SignUpWrapper = () => {
	const INITIAL_STEP = "약관동의";
	const { currentStep, onNext, CurrentComponent } = useStep(INITIAL_STEP);

	const getHeaderText = (step: string) => {
		switch (step) {
			case "약관동의":
				return "‘다운’ 서비스 이용 약관에 동의해주세요.";
			case "이메일및비밀번호":
				return "이메일 간편 회원가입을 진행합니다.";
			case "기본회원정보":
				return "모임에 필요한 프로필을 작성해주세요.";
			case "지역":
				return "내가 모일 동네는 어디인가요?";
			case "사용자운동정보":
				return "할 줄 아는 운동을 모두 알려주세요.";
			default:
				return "이메일 간편 회원가입을 진행합니다.";
		}
	};

	return (
		<SignupContextProvider>
			<div className="max-w-[32rem] w-full py-28 px-10 flex flex-col gap-8">
				<h1 className="font-semibold text-xl">{getHeaderText(currentStep)}</h1>
				{CurrentComponent && <CurrentComponent onNext={onNext} />}
			</div>
		</SignupContextProvider>
	);
};

export default SignUpWrapper;
