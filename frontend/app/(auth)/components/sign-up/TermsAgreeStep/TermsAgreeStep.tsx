import { useEffect, useState } from "react";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";

import { StepProps } from "@/app/(auth)/types/signup";

import { TermsAgreeItem } from "@/app/(auth)/components/sign-up";
import { Button } from "@/components/ui";

type AgreementItem = {
	id: string;
	label: string;
	required: boolean;
};

const agreementItems: AgreementItem[] = [
	{ id: "ageOver14", label: "(필수) 만 14세 이상입니다.", required: true },
	{ id: "termsOfUseAgreement", label: "(필수) 이용약관 동의", required: true },
	{
		id: "personalInfoConsent",
		label: "(필수) 개인정보 수집 및 이용 동의",
		required: true,
	},
	{
		id: "optionalInfoConsent",
		label: "(선택) 선택정보 수집 및 이용 동의",
		required: false,
	},
	{
		id: "marketingConsent",
		label: "(선택) 개인정보 마케팅 활용 동의",
		required: false,
	},
	{
		id: "marketingNotificationsConsent",
		label: "(선택) 마케팅 알림 수신 동의",
		required: false,
	},
];

const TermsAgreeStep = ({ onNext }: StepProps) => {
	const { signUpUserInfo, setSignUpUserInfo } = useSignupContext();
	const [submitBtnActive, setSubmitBtnActive] = useState(false);

	const [agree, setAgree] = useState({
		ageOver14: false,
		termsOfUseAgreement: false,
		personalInfoConsent: false,
		optionalInfoConsent: false,
		marketingConsent: false,
		marketingNotificationsConsent: false,
	});
	const [allAgree, setAllAgree] = useState(false);

	// 약관전체 전체 동의 선택
	const handleAllAgree = () => {
		if (!allAgree) {
			setAgree({
				ageOver14: true,
				termsOfUseAgreement: true,
				personalInfoConsent: true,
				optionalInfoConsent: true,
				marketingConsent: true,
				marketingNotificationsConsent: true,
			});
		} else {
			setAgree({
				ageOver14: false,
				termsOfUseAgreement: false,
				personalInfoConsent: false,
				optionalInfoConsent: false,
				marketingConsent: false,
				marketingNotificationsConsent: false,
			});
		}
	};

	// 약관 전체 동의 - 전체 동의 되었는지 체크해서 상태 변경
	useEffect(() => {
		if (Object.values(agree).findIndex((v) => v === false) === -1) {
			setAllAgree(true);
		} else {
			setAllAgree(false);
		}
	}, [agree]);

	// 필수 동의 체크 되면 버튼 활성화
	useEffect(() => {
		if (
			agree.ageOver14 &&
			agree.termsOfUseAgreement &&
			agree.personalInfoConsent
		) {
			setSubmitBtnActive(true);
		} else {
			setSubmitBtnActive(false);
		}
	}, [agree]);

	const handleSubmit = () => {
		if (!submitBtnActive) return;

		setSignUpUserInfo({ ...signUpUserInfo, termsAgree: true });
		onNext();
	};

	return (
		<div className="flex flex-col space-y-8">
			<div className="flex flex-col space-y-4">
				<TermsAgreeItem
					checked={allAgree}
					onClick={() => {
						setAllAgree(!allAgree);
						handleAllAgree();
					}}
					id="allAgree"
					label="약관 전체 동의하기 (선택 동의 포함)"
					classNameProps="pb-4 border-b"
					description="선택 동의 항목을 포함하여 전체 약관에 동의합니다."
				/>

				{agreementItems.map((item) => (
					<TermsAgreeItem
						key={item.id}
						checked={agree[item.id as keyof typeof agree]}
						onClick={() =>
							setAgree({
								...agree,
								[item.id]: !agree[item.id as keyof typeof agree],
							})
						}
						id={item.id}
						label={item.label}
					/>
				))}
			</div>

			<Button
				disabled={!submitBtnActive}
				onClick={handleSubmit}
			>
				동의하고 가입하기
			</Button>
		</div>
	);
};

export default TermsAgreeStep;
