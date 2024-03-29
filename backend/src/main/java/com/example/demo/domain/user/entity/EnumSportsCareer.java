package com.example.demo.domain.user.entity;

import lombok.Getter;

@Getter
public enum EnumSportsCareer {
    BEGINNER("입문자"),
    NOVICE("초보자"),
    INTERMEDIATE("중급자"),
    MASTER("마스터"),
    VETERAN("고인물"),
    GOD("신");

    private final String career;

    EnumSportsCareer(String career) {
        this.career = career;
    }
    
    public static EnumSportsCareer of(String career) {
        for (EnumSportsCareer sportsCareer : EnumSportsCareer.values()) {
            if (sportsCareer.career.equals(career)) {
                return sportsCareer;
            }
        }
        throw new IllegalArgumentException("해당하는 스포츠 경력이 없습니다.");
    }
}