package com.example.springbootboilerplate.memo;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.base.dto.DataResponseDto;
import com.example.springbootboilerplate.memo.dto.MemoRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/memo")
@Slf4j
public class MemoController {
    private final MemoService memoService;

    /**
     * 메모 생성 API - 정연
     * [POST] /memo
     */
    @PostMapping("")
    public DataResponseDto<Object> uploadMemo(
        @RequestPart(value = "photo") MultipartFile photo,
        @RequestPart(value = "dto") MemoRequestDto memoRequestDto // nickname, description
    ) {
        // validation: 내용이 42자 이내
        if (memoRequestDto.getDescription().length() > 42) {
            throw new GeneralException(Code.BAD_REQUEST, "본문은 42자 이내로 작성해주세요.");
        }

        try {
            memoService.uploadMemo(photo, memoRequestDto);
            return DataResponseDto.of(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
