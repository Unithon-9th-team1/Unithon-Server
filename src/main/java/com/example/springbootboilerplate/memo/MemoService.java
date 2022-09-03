package com.example.springbootboilerplate.memo;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.member.MemberRepository;
import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.memo.dto.MemoRequestDto;
import com.example.springbootboilerplate.upload.S3FileUploadService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemoService {
    private final S3FileUploadService s3FileUploadService;

    private final MemoRepository memoRepository;

    private final MemberRepository memberRepository;

    public boolean uploadMemo(MultipartFile photo, MemoRequestDto memoRequestDto) {
        // 1. 사진 S3 업로드
        String photoUrl;
        try {
            photoUrl = s3FileUploadService.uploadFile(photo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // 2. 닉네임으로 유저 가져오기
        Member member = memberRepository.findByNickname(memoRequestDto.getNickname())
            .orElseThrow(() -> new GeneralException(Code.NOT_FOUND, "유저를 DB에서 찾을 수 없습니다."));

        // 2. DB에 정보 저장
        memoRepository.save(memoRequestDto.toMember(photoUrl, member)).getPhotoUrl();
        return true;
    }

}
