import {
	PostComments,
	PostContent,
	PostOverview,
} from "@/app/(meetups)/components/meetupPostDetailPage";

const MeetupsPostDetailPage = ({ params }: { params: { postId: number } }) => {
	const { postId } = params;
	console.log(postId);

	return (
		<div className="container mt-16">
			<div className="grid grid-cols-3 gap-4 min-h-[60vh]">
				<div className="col-span-2">
					<PostContent />
				</div>
				<div className="cols-span-1">
					<PostOverview />
				</div>
			</div>
			<div>
				<PostComments />
			</div>
		</div>
	);
};

export default MeetupsPostDetailPage;
