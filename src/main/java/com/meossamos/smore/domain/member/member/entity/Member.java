package com.meossamos.smore.domain.member.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meossamos.smore.domain.alarm.alarm.entity.Alarm;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticleClip.entity.RecruitmentArticleClip;
import com.meossamos.smore.domain.article.recruitmentArticleComment.entity.RecruitmentArticleComment;
import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.chat.dm.entity.DmRoom;
import com.meossamos.smore.domain.member.hashTag.entity.MemberHashTag;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Member extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String bio;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private LocalDate birthdate;

    @Column(nullable = true)
    private String region;

    @Column(nullable = true)
    private String hashTags;

    @Enumerated(EnumType.STRING)
    private Authority authority;
    @Column(nullable = true)
    private String profileImageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Study> studyList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member1", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<DmRoom> dmRoomList1 = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member2", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<DmRoom> dmRoomList2 = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Alarm> alarmList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<StudyArticle> studyArticleList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<RecruitmentArticle> recruitmentArticleList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<StudyMember> studyMemberList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<StudySchedule> studyScheduleList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<MemberHashTag> memberHashTagList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<RecruitmentArticleClip> recruitmentArticleClipList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<RecruitmentArticleComment> recruitmentArticleCommentList = new ArrayList<>();
}
