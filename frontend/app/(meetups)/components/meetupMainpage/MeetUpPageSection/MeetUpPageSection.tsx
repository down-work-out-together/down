type MeetUpPageSectionType = {
	children: React.ReactNode;
	sectionTitle: string;
};

const MeetUpPageSection = ({
	sectionTitle,
	children,
}: MeetUpPageSectionType) => {
	return (
		<section className="w-full flex flex-col my-8">
			<div className="w-full flex flex-col md:max-w-[70rem] mx-auto space-y-8">
				<h1 className="text-2xl font-semibold">{sectionTitle}</h1>
				{children}
			</div>
		</section>
	);
};

export default MeetUpPageSection;
