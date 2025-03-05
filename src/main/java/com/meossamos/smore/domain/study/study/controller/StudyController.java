package com.meossamos.smore.domain.study.study.controller;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    // 유저 스터디 목록 조회
    @GetMapping("/api/study/my-studies")
    public ResponseEntity<List<StudyDto>> getStudiesForMember(@AuthenticationPrincipal Member member) {
        List<StudyDto> studyDtos = studyService.getStudiesForMember(member);
        return new ResponseEntity<>(studyDtos, HttpStatus.OK); // 200 OK
    }

    // 스터디 정보 조회
    @GetMapping("/api/study/{study_Id}")
    public StudyDto getStudyById(@PathVariable("study_Id") Long studyId) {
        return studyService.getStudyById(studyId);
    }

    // 스터디 정보 수정
    @PutMapping("/api/study/{study_Id}/introduction")
    public ResponseEntity<StudyDto> updateStudyIntroductions(
            @PathVariable("study_Id") Long studyId,
            @RequestBody StudyDto studyDto) {

        StudyDto updatedStudyDto = studyService.updateStudyIntroductions(studyId, studyDto);
        return new ResponseEntity<>(updatedStudyDto, HttpStatus.OK);
    }
}
