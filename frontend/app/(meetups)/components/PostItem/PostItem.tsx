type PostTypeProps = {
	post: {
		title: string;
		body: string;
		location: string;
		numberOfParticipants: number;
		likes: number;
		tag: string[];
	};
};

const PostItem = ({ post }: PostTypeProps) => {
	return (
		<div className="w-64 space-y-2">
			<div className="w-64 h-[11.25rem] bg-zinc-200 rounded-md" />
			<h3 className="truncate">{post.title}</h3>
			<p className="text-sm">{post.location}</p>
			<p className="text-sm">인원: {post.numberOfParticipants}</p>
		</div>
	);
};

export default PostItem;
