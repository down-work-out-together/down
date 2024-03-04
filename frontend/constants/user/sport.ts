import { LevelType, SportsType } from "@/types/sport";

export const sports: SportsType[] = [
	"골프",
	"농구",
	"당구",
	"등산",
	"러닝",
	"배구",
	"배드민턴",
	"복싱",
	"볼링",
	"사이클",
	"수영",
	"스쿼시",
	"야구",
	"요가",
	"주짓수",
	"축구",
	"크로스핏",
	"탁구",
	"테니스",
	"풋살",
	"헬스",
	"기타",
] as const;

export const levels: LevelType[] = [
	"입문자",
	"초보자",
	"중급자",
	"마스터",
	"고인물",
	"신",
];
