package com.meossamos.smore.addDB;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.global.util.HashTagUtil;
import com.meossamos.smore.global.util.RandomUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddDataTest {
    @Autowired
    private RecruitmentArticleService recruitmentArticleService;
    @Autowired
    private MemberService memberService;
    private final Random random = new Random();
    @Autowired
    private StudyService studyService;


    @Test
    public void addTest() {
        for (int i = 0; i < 1000; i++) {
            addData(i);
        }
        }

    private void addData(int idx) {
        String title = RandomUtil.getRandomTitle();
        String content = RandomUtil.getRandomContent();
        String introduction = RandomUtil.getRandomIntroduction();
        String region = RandomUtil.getRandomRegion();
        int ClipCount = random.nextInt(200);
        int randomImageNum = random.nextInt(5) + 1;
        int maxMember = random.nextInt(50) + 1;
        int currentMember = random.nextInt(maxMember) + 1;
        String thumbnailUrl = "https://picsum.photos/400/600?random=" + idx;
        StringBuilder imageUrls = new StringBuilder();
        LocalDate endDate = LocalDate.from(RandomUtil.getRandomEndDate());
        String profileImageUrl = "https://picsum.photos/200/200?random=" + idx;


        for (int i = 0; i < randomImageNum; i++) {
            imageUrls.append("https://picsum.photos/2000/4000?random=").append(i + 1);
            // 마지막 URL이 아니라면 콤마를 추가
            if (i < randomImageNum - 1) {
                imageUrls.append(",");
            }
        }

        List<String> hashTags = HashTagUtil.getRandomHashTags();

        String memberName = RandomUtil.getRandomMemberName();
        String memberEmail = RandomUtil.getRandomEmail();
        String memberPassword = RandomUtil.getRandomPassword(random.nextInt(10) + 3);
        LocalDate memberBirthDate = RandomUtil.getRandomBirthday();
        String memberRigion = RandomUtil.getRandomRegion();

        Member savedMember = memberService.saveInitMember(memberPassword + memberEmail, memberPassword, memberName, memberBirthDate, memberRigion, profileImageUrl);
        Study savedStudy = studyService.saveStudy(title, currentMember, imageUrls.toString(), introduction, savedMember);

        RecruitmentArticle savedRecruitmentArticle = recruitmentArticleService.save(title, content, introduction, region, thumbnailUrl, imageUrls.toString(), LocalDate.now(), endDate, true, maxMember, hashTags, savedMember.getId(), savedStudy.getId(), ClipCount);

    }
}
