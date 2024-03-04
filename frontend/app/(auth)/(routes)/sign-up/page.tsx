"use client";
import { useRouter } from "next/navigation";

import { useProfile } from "@/hooks/user/useProfile";

import { SignUpWrapper } from "@/app/(auth)/components/sign-up";

const SignUpPage = () => {
	const user = useProfile();
	const router = useRouter();

	if (user) {
		router.push("/");
	}
	return (
		<section className="w-full flex flex-col items-center justify-center">
			<SignUpWrapper />
		</section>
	);
};

export default SignUpPage;
