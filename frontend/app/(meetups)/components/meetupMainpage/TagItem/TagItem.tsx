import React from "react";

type TagTypeProps = {
	tag: string;
};

const TagItem = ({ tag }: TagTypeProps) => {
	return (
		<div className="flex flex-col items-center space-y-2 py-2 cursor-pointer hover:-translate-y-2 transition-transform">
			<div className="w-20 h-20 bg-stone-200 rounded-lg" />
			<p className="text-sm font-stone-500">{tag}</p>
		</div>
	);
};

export default TagItem;
