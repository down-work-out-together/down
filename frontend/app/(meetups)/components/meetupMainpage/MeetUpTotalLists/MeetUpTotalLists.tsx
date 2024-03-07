import {
	MeetUpPageSection,
	PostItem,
} from "@/app/(meetups)/components/meetupMainpage";
import { Command, CommandInput } from "@/components/ui";

import { dummyPosts } from "../dummyPosts";

const MeetUpTotalLists = () => {
	return (
		<MeetUpPageSection sectionTitle="모임 전체보기">
			<Command className="rounded-lg border lg:w-96">
				<CommandInput placeholder="어떤 모임을 찾으세요?" />
			</Command>
			<div className="w-full grid lg:grid-cols-4 gap-x-4 grid-cols-1">
				{dummyPosts.map((post, index) => (
					<div
						key={index}
						className="flex justify-center items-center"
					>
						<PostItem
							key={index}
							postId={index + 1}
							post={post}
						/>
					</div>
				))}
			</div>
		</MeetUpPageSection>
	);
};

export default MeetUpTotalLists;
