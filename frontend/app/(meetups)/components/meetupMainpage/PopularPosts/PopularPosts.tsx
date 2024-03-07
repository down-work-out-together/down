import React from "react";
import { dummyPosts } from "../dummyPosts";
import {
	MeetUpPageSection,
	PostItem,
} from "@/app/(meetups)/components/meetupMainpage";

const PopularPosts = () => {
	const popularPosts = dummyPosts.slice(0, 4);
	return (
		<MeetUpPageSection sectionTitle="이번 주 대세 모임">
			<div className="w-full grid lg:grid-cols-4 gap-x-4 grid-cols-1">
				{popularPosts.map((post, index) => (
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

export default PopularPosts;
