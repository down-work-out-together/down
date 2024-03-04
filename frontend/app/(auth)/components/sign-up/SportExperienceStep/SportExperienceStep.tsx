"use client";
import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";

import { sportInfoFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

import { sports, levels } from "@/constants/user/sport";
import {
	Button,
	FormControl,
	FormField,
	FormItem,
	FormMessage,
	Select,
	SelectTrigger,
	SelectValue,
	SelectContent,
	SelectItem,
} from "@/components/ui";

const SportExperienceStep = () => {
	const { method } = useCommonForm({
		schema: sportInfoFieldSchema,
		checkMode: "onBlur",
		defaultValues: {
			sport: "",
			level: "",
		},
	});

	const handleSubmit = async () => {};

	return (
		<FormFieldWrapper
			method={method}
			onSubmit={handleSubmit}
		>
			<div className="flex flex-col w-full space-y-2">
				<FormField
					control={method.control}
					name="sport"
					render={({ field }) => (
						<FormItem>
							<Select
								onValueChange={field.onChange}
								defaultValue={field.value}
							>
								<FormControl>
									<SelectTrigger>
										<SelectValue placeholder="운동 선택" />
									</SelectTrigger>
								</FormControl>
								<SelectContent>
									{sports.map((sport) => (
										<SelectItem
											key={sport}
											value={sport}
										>
											{sport}
										</SelectItem>
									))}
								</SelectContent>
							</Select>
							<FormMessage />
						</FormItem>
					)}
				/>
				<FormField
					control={method.control}
					name="level"
					render={({ field }) => (
						<FormItem>
							<Select
								onValueChange={field.onChange}
								defaultValue={field.value}
							>
								<FormControl>
									<SelectTrigger>
										<SelectValue placeholder="경력" />
									</SelectTrigger>
								</FormControl>
								<SelectContent>
									{levels.map((level) => (
										<SelectItem
											key={level}
											value={level}
										>
											{level}
										</SelectItem>
									))}
								</SelectContent>
							</Select>
							<FormMessage />
						</FormItem>
					)}
				/>
			</div>
			<Button
				className="w-full"
				type="submit"
			>
				다음
			</Button>
		</FormFieldWrapper>
	);
};

export default SportExperienceStep;
