import {
	MeetUpPageSection,
	TagItem,
} from "@/app/(meetups)/components/meetupMainpage";
import { dummyTags } from "../dummyTags";

const PopularTags = () => {
	const popularTags = dummyTags.slice(0, 8);
	return (
		<MeetUpPageSection sectionTitle="인기 태그">
			<div className="w-full lg:flex lg:justify-between lg:flex-row lg:gap-y-0 grid grid-cols-4 gap-y-4">
				{popularTags.map((tag, index) => (
					<TagItem
						tag={tag}
						key={index}
					/>
				))}
			</div>
		</MeetUpPageSection>
	);
};

export default PopularTags;
