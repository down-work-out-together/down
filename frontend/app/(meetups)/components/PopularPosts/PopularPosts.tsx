import React from "react";
import { dummyPosts } from "../dummyPosts";
import { PostItem } from "@/app/(meetups)/components";

const PopularPosts = () => {
	const popularPosts = dummyPosts.slice(0, 4);
	return (
		<section className="w-full flex flex-col my-16">
			<div className="flex flex-col max-w-[70rem] mx-auto space-y-8">
				<h1 className="text-2xl font-semibold">이번 주 대세 모임</h1>
				<div className="w-full flex justify-center gap-x-8">
					{popularPosts.map((post, index) => (
						<PostItem
							key={index}
							post={post}
						/>
					))}
				</div>
			</div>
		</section>
	);
};

export default PopularPosts;
