import { FieldValues } from "react-hook-form";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";
import { passwordFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import {
	FormField,
	FormItem,
	FormMessage,
	FormControl,
	Input,
	Button,
} from "@/components/ui";

const PasswordField = () => {
	const { signUpUserInfo, setSignUpUserInfo } = useSignupContext();
	const { method } = useCommonForm({
		schema: passwordFieldSchema,
		checkMode: "onChange",
		defaultValues: {
			password: "",
			passwordCheck: "",
		},
	});
	const handlePassword = (value: FieldValues) => {
		setSignUpUserInfo({ ...signUpUserInfo, password: value.password });
	};

	return (
		<FormFieldWrapper
			method={method}
			onSubmit={handlePassword}
		>
			<div className="space-y-2">
				<FormField
					control={method.control}
					name="password"
					render={({ field }) => (
						<FormItem>
							<FormControl>
								<Input
									type="password"
									placeholder="비밀번호는 영문과 숫자를 포함하여 8~20자로 입력해 주세요."
									{...field}
								/>
							</FormControl>
							<FormMessage />
						</FormItem>
					)}
				/>

				<FormField
					control={method.control}
					name="passwordCheck"
					render={({ field }) => (
						<FormItem>
							<FormControl>
								<Input
									type="password"
									placeholder="비밀번호를 다시 입력해주세요."
									{...field}
									onChange={field.onChange}
								/>
							</FormControl>
							<FormMessage />
						</FormItem>
					)}
				/>
			</div>

			<Button className="w-full">회원가입하기</Button>
		</FormFieldWrapper>
	);
};

export default PasswordField;
