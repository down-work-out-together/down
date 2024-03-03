import { cn } from "@/lib/cn";
import { Checkbox } from "@/components/ui";

type TermsAgreeItemProps = {
	checked: boolean;
	onClick: () => void;
	id: string;
	label: string;
	classNameProps?: string;
	description?: string;
};

const TermsAgreeItem = ({
	checked,
	onClick,
	id,
	label,
	classNameProps,
	description,
}: TermsAgreeItemProps) => {
	return (
		<div className={cn("items-top flex space-x-2", classNameProps)}>
			<Checkbox
				id={id}
				checked={checked}
				onClick={onClick}
			/>
			<div className="grid gap-1.5 leading-none">
				<label
					htmlFor={id}
					className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
				>
					{label}
				</label>

				<p
					className={cn(
						description ? "text-sm text-muted-foreground" : "hidden"
					)}
				>
					{description}
				</p>
			</div>
		</div>
	);
};

export default TermsAgreeItem;
