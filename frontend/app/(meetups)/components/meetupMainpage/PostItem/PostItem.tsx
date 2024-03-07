"use client";

import { useRouter } from "next/navigation";

type PostTypeProps = {
	postId: number;
	post: {
		title: string;
		body: string;
		location: string;
		numberOfParticipants: number;
		likes: number;
		tag: string[];
	};
};

const PostItem = ({ postId, post }: PostTypeProps) => {
	const router = useRouter();
	const onClick = () => {
		router.push(`/meetups/${postId}`);
	};
	return (
		<div
			onClick={onClick}
			className="w-full max-w-96 lg:max-w-64 space-y-2 py-2 cursor-pointer hover:-translate-y-2 border-stone-400 transition-transform"
		>
			<div className="w-full max-w-96 lg:max-w-64 h-[11.25rem] bg-stone-200 rounded-md" />
			<h3 className="truncate">{post.title}</h3>
			<p className="text-sm">{post.location}</p>
			<p className="text-sm">인원: {post.numberOfParticipants}</p>
		</div>
	);
};

export default PostItem;
