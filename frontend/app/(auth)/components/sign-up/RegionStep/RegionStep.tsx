"use client";
import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";

import { StepProps } from "@/app/(auth)/types/signup";
import { regionFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

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

const RegionStep = ({ onNext }: StepProps) => {
	const { method } = useCommonForm({
		schema: regionFieldSchema,
		checkMode: "onBlur",
		defaultValues: {
			regionDepth1: "",
			regionDepth2: "",
		},
	});

	const handleSubmit = async () => {
		onNext();
	};

	return (
		<FormFieldWrapper
			method={method}
			onSubmit={handleSubmit}
		>
			<div className="flex w-full space-x-2">
				<FormField
					control={method.control}
					name="regionDepth1"
					render={({ field }) => (
						<FormItem className="w-1/3">
							<Select
								onValueChange={field.onChange}
								defaultValue={field.value}
							>
								<FormControl>
									<SelectTrigger>
										<SelectValue placeholder="지역" />
									</SelectTrigger>
								</FormControl>
								<SelectContent>
									<SelectItem value="서울">서울</SelectItem>
									<SelectItem value="경기">경기</SelectItem>
								</SelectContent>
							</Select>
							<FormMessage />
						</FormItem>
					)}
				/>
				<FormField
					control={method.control}
					name="regionDepth2"
					render={({ field }) => (
						<FormItem className="w-2/3">
							<Select
								onValueChange={field.onChange}
								defaultValue={field.value}
							>
								<FormControl>
									<SelectTrigger>
										<SelectValue placeholder="세부 지역" />
									</SelectTrigger>
								</FormControl>
								<SelectContent>
									<SelectItem value="강남구">강남구</SelectItem>
									<SelectItem value="강동구">강동구</SelectItem>
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

export default RegionStep;
