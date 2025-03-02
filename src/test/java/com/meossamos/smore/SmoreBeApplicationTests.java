package com.meossamos.smore;

import com.meossamos.smore.domain.alarm.alarm.entity.Alarm;
import com.meossamos.smore.domain.alarm.alarm.service.AlarmService;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticleDoc;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleDocService;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleHashTag.entity.RecruitmentArticleHashTag;
import com.meossamos.smore.domain.article.recruitmentArticleHashTag.service.RecruitmentArticleHashTagService;
import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.article.studyArticle.service.StudyArticleService;
import com.meossamos.smore.domain.chat.dm.dto.DmRoomResponseDto;
import com.meossamos.smore.domain.chat.dm.entity.DmRoom;
import com.meossamos.smore.domain.chat.dm.service.DmRoomService;
import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import com.meossamos.smore.domain.chat.groupChat.service.GroupChatRoomService;
import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import com.meossamos.smore.domain.chat.message.service.ChatMessageService;
import com.meossamos.smore.domain.member.hashTag.entity.MemberHashTag;
import com.meossamos.smore.domain.member.hashTag.service.MemberHashTagService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.domain.study.document.entity.StudyDocument;
import com.meossamos.smore.domain.study.document.service.StudyDocumentService;
import com.meossamos.smore.domain.study.hashTag.entity.StudyHashTag;
import com.meossamos.smore.domain.study.hashTag.service.StudyHashTagService;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.service.StudyScheduleService;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import com.meossamos.smore.global.util.HashTagUtil;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SmoreBeApplicationTests {
    @Autowired
    private StudyService studyService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private GroupChatRoomService groupChatRoomService;
    @Autowired
    private DmRoomService dmRoomService;
    @Autowired
    private RecruitmentArticleService recruitmentArticleService;
    @Autowired
    private StudyHashTagService studyHashTagService;
    @Autowired
    private MemberHashTagService memberHashTagService;
    @Autowired
    private StudyArticleService studyArticleService;
    @Autowired
    private StudyDocumentService studyDocumentService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private StudyMemberService studyMemberService;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private RecruitmentArticleHashTagService recruitmentArticleHashTagService;
    @Autowired
    private StudyScheduleService studyScheduleService;
    @Autowired
    private RecruitmentArticleDocService recruitmentArticleDocService;


    private Long nextId = 0L;
    private List<Member> memberList = new ArrayList<>();
    private Study study;
    private DmRoomResponseDto dmRoom;
    private RecruitmentArticle recruitmentArticle;
    private StudyArticle studyArticle;
    private StudyDocument studyDocument;
    private Alarm alarm;
    private StudyMember[] studyMember;
    private ChatMessage[] chatMessages;
    private GroupChatRoom groupChatRoom;
    private StudyHashTag[] studyHashTag;
    private RecruitmentArticleHashTag[] recruitmentArticleHashTag;
    private StudySchedule studySchedule;

    private final Random random = new Random();
    private int leaderNum;
    private int[] memberNums;

    // 1. 멤버 생성
    // 2. 스터디 생성
    // 3. 멤서 스터디 등록
    // 4. 멤버 권한 필요 항목

    @Test
    @Order(1)
    public void saveMemberTest() {
        Long maxMemberId = memberService.getMaxMemberId();
        nextId = (maxMemberId != null ? maxMemberId : 0L) + 1;

        Member member1 = saveMember("email" + nextId + "email.com", "password" + nextId, "user" + nextId, LocalDate.now(), "region" + (nextId % 3), "https://picsum.photos/200/200?random=1");
        Member member2 = saveMember("email" + (nextId + 1) + "email.com", "password" + (nextId + 1), "user" + (nextId + 1), LocalDate.now(), "region" + ((nextId + 1) % 3), "https://picsum.photos/200/200?random=1");
        Member member3 = saveMember("email" + (nextId + 2) + "email.com", "password" + (nextId + 2), "user" + (nextId + 2), LocalDate.now(), "region" + ((nextId + 2) % 3), "https://picsum.photos/200/200?random=1");
        Member member4 = saveMember("email" + (nextId + 3) + "email.com", "password" + (nextId + 3), "user" + (nextId + 3), LocalDate.now(), "region" + ((nextId + 3) % 3), "https://picsum.photos/200/200?random=1");
        Member member5 = saveMember("email" + (nextId + 4) + "email.com", "password" + (nextId + 4), "user" + (nextId + 4), LocalDate.now(), "region" + ((nextId + 4) % 3), "https://picsum.photos/200/200?random=1");

        this.memberList = Arrays.asList(member1, member2, member3, member4, member5);

        System.out.println(this.memberList);
    }

    @Test
    @Order(2)
    public void saveStudyTest() {
        leaderNum = random.nextInt(5);
        int randomMemberNum = random.nextInt(20 - 1 + 1) + 1; // 1부터 20까지의 랜덤 정수
        Study study = saveStudy("title" + nextId, randomMemberNum, "https://picsum.photos/400/600?random=1", "introduction" + nextId, memberList.get(leaderNum));

        this.study = study;

        System.out.println(study);
    }

    @Test
    @Order(2)
    public void saveChatRoomTest() {
        int first = random.nextInt(5);

        int second;
        do {
            second = random.nextInt(5);
        } while (first == second);


        DmRoomResponseDto dmRoom = saveChatRoom(memberList.get(first), memberList.get(second));

        this.dmRoom = dmRoom;

        System.out.println(dmRoom);
    }

    @Test
    @Order(4)
    public void saveRecruitmentArticleTest() {
        int randomNum = random.nextInt(100 - 5 + 1) + 5; // 5부터 100까지의 랜덤 정수
        int randomClipCount = random.nextInt(100);

        List<String> hashTags = HashTagUtil.getRandomHashTags();
        String hashTag = HashTagUtil.mergeHashTagList(hashTags);

        int randomImageNum = random.nextInt(5);
        StringBuilder imageUrls = new StringBuilder();

        for (int i = 0; i < randomImageNum; i++) {
            imageUrls.append("https://picsum.photos/400/600?random=1");
            // 마지막 URL이 아니라면 콤마를 추가
            if (i < randomImageNum - 1) {
                imageUrls.append(",");
            }
        }

        RecruitmentArticle recruitmentArticle = saveRecruitmentArticle("title" + nextId, "content" + nextId, "introduction", "region" + ((nextId + 1) % 3), imageUrls.toString(), LocalDateTime.now(), LocalDateTime.now(), true, randomNum, hashTag,  memberList.get(leaderNum), study, randomClipCount);

        this.recruitmentArticle = recruitmentArticle;

        System.out.println(recruitmentArticle);
    }

    @Test
    @Order(4)
    public void saveStudyArticleTest() {
        List<String> attachments = new ArrayList<>();
        attachments.add("attachment" + nextId);

        List<String> hashTags = HashTagUtil.getRandomHashTags();
        String hashTag = HashTagUtil.mergeHashTagList(hashTags);
        StudyArticle studyArticle = saveStudyArticle("title" + nextId, "content" + nextId, "https://picsum.photos/200/200?random=1", attachments, hashTag, memberList.get(leaderNum), study);

        this.studyArticle = studyArticle;

        System.out.println(studyArticle);
    }

    @Test
    @Order(4)
    public void saveStudyDocumentTest() {
        StudyDocument studyDocument = saveStudyDocument("name" + nextId, "url" + nextId, "type" + nextId, study, studyArticle);

        this.studyDocument = studyDocument;

        System.out.println(studyDocument);
    }

    @Test
    @Order(3)
    public void saveAlarmTest() {
        int first = random.nextInt(5);

        int second;
        do {
            second = random.nextInt(5);
        } while (first == second);

        Alarm alarm = saveAlarm(memberList.get(first), "message" + nextId, memberList.get(second).getId());

        this.alarm = alarm;

        System.out.println(alarm);
    }

    @Test
    @Order(3)
    public void saveStudyMemberTest() {
        List<Integer> candidates = new ArrayList<>();

        // leaderNum이 0~4 범위에 있도록 보장
        if (leaderNum < 0 || leaderNum >= 5) {
            throw new IllegalArgumentException("leaderNum must be between 0 and 4");
        }

        // leaderNum을 제외한 숫자 추가 (무조건 4개 들어감)
        for (int i = 0; i < 5; i++) {
            if (i != leaderNum) {
                candidates.add(i);
            }
        }

        // 리스트를 무작위로 섞음
        Collections.shuffle(candidates, random);

        // 최소 3개 이상의 요소를 선택 가능하도록 보장됨
        this.memberNums = new int[]{ candidates.get(0), candidates.get(1), candidates.get(2) };

        // studyMember 배열 초기화
        this.studyMember = new StudyMember[]{
                saveStudyMember(memberList.get(leaderNum), study, true, true, true, true),
                saveStudyMember(memberList.get(memberNums[0]), study, false, false, false, false),
                saveStudyMember(memberList.get(memberNums[1]), study, true, false, false, true),
                saveStudyMember(memberList.get(memberNums[2]), study, false, true, true, false)
        };

        System.out.println(Arrays.toString(this.studyMember));
    }


    @Test
    @Order(4)
    public void saveChatMessageTest() {
        ChatMessage[] chatMessages = new ChatMessage[30];
        for (int i = 0; i < 30; i++) {
            chatMessages[i] = saveChatMessage(dmRoom.getRoomId().toString(), memberList.get((i + 2) % 5).getId().toString(), "message" + (nextId + (i % 10)), "attachment" + (nextId + ((i + 1) % 10)));
        }

        this.chatMessages = chatMessages;

        System.out.println(Arrays.toString(chatMessages));
    }

    @Test
    @Order(3)
    public void saveGroupChatRoomTest() {
        GroupChatRoom groupChatRoom = saveGroupChatRoom(study);

        this.groupChatRoom = groupChatRoom;

        System.out.println(groupChatRoom);
    }

    @Test
    @Order(4)
    public void saveStudyHashTagTest() {
        List<String> hashTags = HashTagUtil.getRandomHashTags();
        StudyHashTag[] studyHashTag = new StudyHashTag[hashTags.size()];

        hashTags.forEach(hashTag -> {
            saveStudyHashTag(hashTag, study);
        });

        this.studyHashTag = studyHashTag;

        System.out.println(Arrays.toString(this.studyHashTag));
    }

    @Test
    @Order(2)
    public void saveMemberHashTagTest() {
        memberList.forEach(member -> {
            List<String> hashTags = HashTagUtil.getRandomHashTags();
            hashTags.forEach(hashTag -> {
                saveMemberHashTag(hashTag, member);
            });
        });

        System.out.println("Member HashTag saved");
    }

    @Test
    @Order(4)
    public void saveRecruitmentArticleHashTagTest() {
        List<String> hashTags = HashTagUtil.getRandomHashTags();
        RecruitmentArticleHashTag[] recruitmentArticleHashTag = new RecruitmentArticleHashTag[hashTags.size()];

        hashTags.forEach(hashTag -> {
            saveRecruitmentArticleHashTag(hashTag, recruitmentArticle);
        });

        this.recruitmentArticleHashTag = recruitmentArticleHashTag;

        System.out.println(Arrays.toString(this.recruitmentArticleHashTag));
    }

    @Test
    @Order(4)
    public void saveStudyScheduleTest() {
        StudySchedule studySchedule = saveStudySchedule("title" + nextId, "content" + nextId, LocalDateTime.now(), LocalDateTime.now(), false, memberList.get(leaderNum), study);

        this.studySchedule = studySchedule;

        System.out.println(studySchedule);
    }

    @Test
    @Order(5)
    public void findRecruitmentArticleByHashTagTest() {
        List<String> hashTags = HashTagUtil.getRandomHashTags();
        List<String> hashTags2 = new ArrayList<>();
        hashTags2.add("백엔드");
        hashTags2.add("postgresql");
        hashTags2.add("프론트");
        List<String> hashTags3 = new ArrayList<>();
        List<RecruitmentArticleDoc> recruitmentArticles = findRecruitmentArticleByHashTag(hashTags2, 2, 12);
        List<RecruitmentArticleDoc> recruitmentArticles2 = findRecruitmentArticleByHashTag(hashTags2, 3, 12);
        List<RecruitmentArticleDoc> recruitmentArticles3 = findRecruitmentArticleByHashTag(hashTags2, 12, 12);
        List<RecruitmentArticleDoc> recruitmentArticles4 = findRecruitmentArticleByHashTag(hashTags3, 1, 12);


        System.out.print("find RecruitmentArticle by HashTag: ");
        hashTags.forEach(hashTag -> System.out.print(hashTag + ", "));

        System.out.print("\nfounded RecruitmentArticles: ");
        recruitmentArticles.forEach(recruitmentArticle -> System.out.print(recruitmentArticle.getId() + ", "));

        System.out.println("\n\n\n\n");
    }


    private Member saveMember(String email, String password, String nickname, @Nullable LocalDate birthdate, @Nullable String region, @Nullable String profileImageUrl) {
        return memberService.saveMember(email, password, nickname, birthdate, region, profileImageUrl);
    }

    private Study saveStudy(String title, Integer memberCnt, @Nullable String imageUrls, @Nullable String introduction, Member leader) {
        return studyService.saveStudy(title, memberCnt, imageUrls, introduction, leader);
    }

    private DmRoomResponseDto saveChatRoom(Member member1, Member member2) {
        return dmRoomService.createDmRoom(member1.getId(), member2.getId());
    }

    private RecruitmentArticle saveRecruitmentArticle(String title, String content, String introduction, @Nullable String region, @Nullable String imageUrls, LocalDateTime startDate, LocalDateTime endDate, Boolean isRecruiting, Integer maxMember, String hashTags, Member member, Study study, Integer clipCount) {
        return recruitmentArticleService.saveRecruitmentArticle(title, content, introduction, region, imageUrls, startDate, endDate, isRecruiting, maxMember, hashTags, member, study, clipCount);
    }

    private MemberHashTag saveMemberHashTag(String hashTag, Member member) {
        return memberHashTagService.saveMemberHashTag(hashTag, member);
    }

    private StudyHashTag saveStudyHashTag(String hashTag, Study study) {
        return studyHashTagService.saveStudyHashTag(hashTag, study);
    }

    private RecruitmentArticleHashTag saveRecruitmentArticleHashTag(String hashTag, RecruitmentArticle recruitmentArticle) {
        return recruitmentArticleHashTagService.saveRecruitmentArticleHashTag(hashTag, recruitmentArticle);
    }

    private StudyArticle saveStudyArticle(String title, String content, @Nullable String imageUrls, @Nullable List<String> attachments, String hashTags, Member member, Study study) {
        return studyArticleService.saveStudyArticle(title, content, imageUrls,  attachments, hashTags,  member, study);
    }

    private StudySchedule saveStudySchedule(String title, String content, LocalDateTime startDate, LocalDateTime endDate, boolean allDay, Member member, Study study) {
        return studyScheduleService.saveStudySchedule(title, content, startDate, endDate, allDay, member, study);
    }

    private StudyDocument saveStudyDocument(String name, String url, String type, Study study, StudyArticle studyArticle) {
        return studyDocumentService.saveStudyDocument(name, url, type, study, studyArticle);
    }

    private Alarm saveAlarm(Member member, String message, @Nullable Long senderId) {
        return alarmService.saveAlarm(member, message, senderId);
    }

    private StudyMember saveStudyMember(Member member, Study study, Boolean permissionRecruitManage, Boolean permissionArticleManage, Boolean permissionCalendarManage, Boolean permissionSettingManage) {
        return studyMemberService.saveStudyMember(member, study, permissionRecruitManage, permissionArticleManage, permissionCalendarManage, permissionSettingManage);
    }

    private ChatMessage saveChatMessage(String roomId, String senderId, String message, String attachment) {
        return chatMessageService.saveChatMessage(roomId, senderId, message, attachment);
    }

    private GroupChatRoom saveGroupChatRoom(Study study) {
        return groupChatRoomService.saveGroupChatRoom(study);
    }

    private List<RecruitmentArticleDoc> findRecruitmentArticleByHashTag(List<String> hashTags, int page, int size) {
        return recruitmentArticleDocService.findByHashTags(hashTags, page, size);
    }
}
