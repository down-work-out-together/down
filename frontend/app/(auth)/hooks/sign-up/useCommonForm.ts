import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

interface CommonFormProps {
	schema: any;
	checkMode: "onChange" | "onBlur" | "onSubmit";
	defaultValues?: Record<string, any>;
}

export const useCommonForm = ({
	schema,
	checkMode,
	defaultValues,
}: CommonFormProps) => {
	const method = useForm({
		resolver: zodResolver(schema),
		mode: checkMode,
		defaultValues,
	});

	return {
		method,
	};
};
