package com.meossamos.smore.domain.article.recruitmentArticle.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticleDoc;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleDocService;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiV1RecruitmentArticleController {
    private final RecruitmentArticleService recruitmentArticleService;
    private final RecruitmentArticleDocService recruitmentArticleDocService;

    @GetMapping("/recruitmentArticles")
    public RsData<?> getRecruitmentArticles(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "12") int size,
            @RequestParam(value = "hashTags", required = false) String hashTags
    ) {
        List<String> hashTagList = new ArrayList<>();
        if (hashTags == null) {
            hashTags = "";
        }

        // 해시태그가 콤마로 구분된 문자열로 저장되는 경우 String으로 사용
        hashTagList = List.of(hashTags.split(","));
        System.out.println("hashTags: " + hashTags);

        // RecruitmentArticleService 내부에서 RecruitmentArticleDocService의 findByHashTags 메서드를 호출하여 페이지 결과를 얻음
        List<RecruitmentArticleDoc> resultPage = recruitmentArticleDocService.findByHashTags(hashTagList, page, size);


        return new RsData<>("200", "모집글 목록 조회 성공", resultPage);
    }

    @GetMapping("/recruitmentArticle/test")
    public RsData<?> test() {
        RecruitmentArticle testRecruitmentArticle = RecruitmentArticle.builder()
                .title("테스트")
                .content("테스트")
                .region("테스트")
                .imageUrls("테스트")
                .startDate(null)
                .endDate(null)
                .isRecruiting(true)
                .maxMember(1)
                .build();
        return new RsData<>("200", "테스트 성공", testRecruitmentArticle);
    }
}
