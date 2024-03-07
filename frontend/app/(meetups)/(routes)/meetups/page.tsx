import React from "react";
import {
	MeetUpTotalLists,
	PopularPosts,
	PopularTags,
} from "@/app/(meetups)/components/meetupMainpage";

const MeetupsPage = () => {
	return (
		<div className="container flex flex-col min-h-screen mt-8">
			<PopularPosts />
			<PopularTags />
			<MeetUpTotalLists />
		</div>
	);
};

export default MeetupsPage;
