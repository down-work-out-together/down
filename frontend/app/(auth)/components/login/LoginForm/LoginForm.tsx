"use client";
import { useState } from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { FieldValues, useForm } from "react-hook-form";

import { useLogin } from "@/app/(auth)/hooks/login/useLogin";

import { loginSchema } from "@/app/(auth)/constants/login/schema";

import {
	Form,
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui";
import { Input, Button } from "@/components/ui";

const LoginForm = () => {
	const method = useForm<z.infer<typeof loginSchema>>({
		resolver: zodResolver(loginSchema),
		defaultValues: {
			email: "",
			password: "",
			rememberMe: false,
		},
	});

	const login = useLogin();

	const onSubmit = async (value: FieldValues) => {
		await login(value.email, value.password);
	};

	return (
		<Form {...method}>
			<form
				onSubmit={method.handleSubmit(onSubmit)}
				className="space-y-8"
			>
				<div className="space-y-4">
					<FormField
						control={method.control}
						name="email"
						render={({ field }) => (
							<FormItem>
								<FormLabel>이메일</FormLabel>
								<div className="flex gap-2">
									<FormControl>
										<Input
											placeholder="이메일을 입력해주세요."
											{...field}
										/>
									</FormControl>
								</div>
								<FormMessage />
							</FormItem>
						)}
					/>

					<FormField
						control={method.control}
						name="password"
						render={({ field }) => (
							<FormItem>
								<FormLabel>비밀번호</FormLabel>
								<FormControl>
									<Input
										type="password"
										placeholder="비밀번호를 입력해주세요."
										{...field}
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>
				</div>

				<div className="flex items-center justify-between">
					<FormField
						control={method.control}
						name="rememberMe"
						render={({ field }) => {
							const { value, ...fieldProps } = field;
							return (
								<FormItem className="flex gap-2 items-center space-y-0">
									<FormLabel>로그인 상태 유지하기</FormLabel>
									<FormControl>
										<input
											type="checkbox"
											{...fieldProps} // value 속성을 제외한 나머지 속성들을 적용
										/>
									</FormControl>
									<FormMessage />
								</FormItem>
							);
						}}
					/>
					<button
						type="button"
						className="text-sm text-stone-500"
					>
						아이디/비밀번호 찾기
					</button>
				</div>

				<Button
					className="w-full"
					type="submit"
				>
					로그인
				</Button>
			</form>
		</Form>
	);
};

export default LoginForm;
