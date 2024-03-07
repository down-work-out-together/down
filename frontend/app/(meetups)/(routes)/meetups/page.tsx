import React from "react";
import { PopularPosts } from "@/app/(meetups)/components";

const MeetupsPage = () => {
	return (
		<div className="container flex flex-col min-h-screen">
			<PopularPosts />
		</div>
	);
};

export default MeetupsPage;
