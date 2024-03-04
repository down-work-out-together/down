"use client";
import React, {
	useContext,
	createContext,
	ReactNode,
	useMemo,
	useState,
} from "react";

export type SignUpUserInfo = {
	termsAgree: boolean;
	email: string;
	emailCheck: boolean;
	emailCode: string;
	password: string;
	nickname: string;
	gender: "male" | "female";
	birthYear: string;
	region: string;
	sport: string;
};

interface SignupContextValue {
	signUpUserInfo: SignUpUserInfo;
	setSignUpUserInfo: React.Dispatch<React.SetStateAction<SignUpUserInfo>>;
}

const SignupContext = createContext<SignupContextValue | null>(null);

const SignupContextProvider = ({ children }: { children: ReactNode }) => {
	const [signUpUserInfo, setSignUpUserInfo] = useState<SignUpUserInfo>({
		termsAgree: false,
		email: "",
		emailCheck: false,
		emailCode: "",
		password: "",
		nickname: "",
		gender: "male",
		birthYear: "",
		region: "",
		sport: "",
	});

	console.log(signUpUserInfo);

	const contextValue = useMemo(
		() => ({ signUpUserInfo, setSignUpUserInfo }),
		[signUpUserInfo, setSignUpUserInfo]
	);

	return (
		<SignupContext.Provider value={contextValue}>
			{children}
		</SignupContext.Provider>
	);
};

const useSignupContext = () => {
	const ctx = useContext(SignupContext);
	if (!ctx)
		throw new Error(
			"SignupContext를 찾을 수 없습니다. SignupContextProvider 내부에서 사용해야 합니다."
		);
	return ctx;
};

export { SignupContextProvider, useSignupContext };
