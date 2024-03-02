"use client";
import Link from "next/link";
import { redirect } from "next/navigation";

import { useProfile } from "@/hooks/user/useProfile";
import { LoginForm } from "@/app/(auth)/components/login";

const LoginPage = () => {
	const user = useProfile();

	if (user) {
		redirect("/");
	}

	return (
		<section className="w-full flex flex-col items-center justify-center">
			<div className="md:w-[32rem] w-full py-28 px-10 flex flex-col gap-8 justify-center">
				<h1 className="font-semibold text-xl">로그인 하기</h1>
				<LoginForm />

				<div className="flex gap-2 items-center justify-center">
					<p className="text-xs text-stone-500">
						아직 다운의 회원이 아니신가요?
					</p>
					<Link
						href="/sign-up"
						className="text-sm py-1 text-red-400"
					>
						회원가입 하기
					</Link>
				</div>

				<div className="w-full flex items-center gap-4">
					<div className="border flex-grow" />
					<p className="text-sm">또는</p>
					<div className="border flex-grow" />
				</div>
			</div>
		</section>
	);
};

export default LoginPage;
