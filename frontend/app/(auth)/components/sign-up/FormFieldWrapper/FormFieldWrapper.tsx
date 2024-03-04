"use client";
import { UseFormReturn } from "react-hook-form";
import { Form } from "@/components/ui";

type FormFieldWrapperProps = {
	children: React.ReactNode;
	method: UseFormReturn;
	onSubmit: (values: any) => Promise<void>;
};

const FormFieldWrapper = ({
	children,
	method,
	onSubmit,
}: FormFieldWrapperProps) => {
	return (
		<Form {...method}>
			<form
				className="space-y-8"
				onSubmit={method.handleSubmit(onSubmit)}
			>
				{children}
			</form>
		</Form>
	);
};

export default FormFieldWrapper;
