package com.example.springbootboilerplate.memo;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.member.MemberRepository;
import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.memo.domain.Memo;
import com.example.springbootboilerplate.memo.dto.MemoListResponseDto;
import com.example.springbootboilerplate.memo.dto.MemoRequestDto;
import com.example.springbootboilerplate.memo.dto.MemoResponseDto;
import com.example.springbootboilerplate.rocket.RocketRepository;
import com.example.springbootboilerplate.rocket.domain.Rocket;
import com.example.springbootboilerplate.upload.S3FileUploadService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
    private final RocketRepository rocketRepository;

    // 메모 생성
    public boolean uploadMemo(MultipartFile photo, MemoRequestDto memoRequestDto) {
        // 1. 사진 S3 업로드
        String photoUrl;
        try {
            photoUrl = s3FileUploadService.uploadFile(photo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Rocket rocket = rocketRepository.findById(memoRequestDto.getRocketId()).get();

        // 2. 닉네임으로 유저 가져오기
        Member member = memberRepository.findByNickname(memoRequestDto.getNickname())
            .orElseThrow(() -> new GeneralException(Code.NOT_FOUND, "유저를 DB에서 찾을 수 없습니다."));

        // 2. DB에 정보 저장
        memoRepository.save(memoRequestDto.toMemo(photoUrl, member, rocket));
        return true;
    }

    // 메모 목록 조회
    public MemoListResponseDto getMemos(String rocket) {
        Rocket rocketEntity = rocketRepository.findByRocketName(rocket)
            .orElseThrow(() -> new GeneralException(Code.NOT_FOUND, "로켓을 DB에서 찾을 수 없습니다."));

        List<Memo> memos = memoRepository.findAllByRocket(rocketEntity);
        System.out.println(memos.size());
        List<MemoResponseDto> memoResponseDtos = new ArrayList<>();
        for (Memo memo : memos) {
            memoResponseDtos.add(new MemoResponseDto(
                memo.getPhotoUrl(),
                memo.getMember().getNickname(),
                memo.getDescription()
            ));
        }

        // 로켓 생성 일자
        LocalDateTime createAt = rocketEntity.getCreatedAt();
        DateTimeFormatter createAtFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd");
        String formattedCreateAt = createAt.format(createAtFormatter);

        // 로켓 도착 일자
        LocalDateTime arrivedAt = rocketEntity.getFinalArrival();
        DateTimeFormatter arrivedFormatter = DateTimeFormatter.ofPattern("dd");
        String formattedArrivedAt = arrivedAt.format(arrivedFormatter);

        // 생성 일자 + 도착 일자 = 기간
        String period = formattedCreateAt + " - " + formattedArrivedAt;

        return new MemoListResponseDto(
            rocketEntity.getRocketName(),
            period,
            memoResponseDtos
        );
    }

    // 랜덤 필터링 조회
    public MemoResponseDto getFilterdRandomMemo(String rocket) {
        // 1. 로켓에 있는 메모들 중에서 랜덤으로 하나를 뽑아 (랜덤인데 한번 뽑으면 고정되어야 함)
        Rocket rocketEntity = rocketRepository.findByRocketName(rocket)
            .orElseThrow(() -> new GeneralException(Code.NOT_FOUND, "로켓을 DB에서 찾을 수 없습니다."));
        List<Memo> memos = memoRepository.findAllByRocket(rocketEntity);
        int memoCount = memos.size();

        int min_val = 0;
        int max_val = memoCount - 1;
        Random rand = new Random(10);
        int randomNum = min_val + rand.nextInt((max_val - min_val) + 1);

        Memo randomMemo = memos.get(randomNum);

        // 2. 텍스트 필터링 로직을 만들어
        // 2-1. split 할 문자열 준비
        String text = randomMemo.getDescription();
        // 2-2. " "를 구분자로 문자열 split
        String[] textString = text.split(" ");

        // 문장 > 단어로 분리하여 리스트 생성
        List<String> splittedText = Arrays.asList(textString);
        int wordCount = splittedText.size(); // 문장의 단어 개수

        int filteredWordCount = wordCount / 2;
        min_val = 0;
        max_val = wordCount - 1;
        List<Integer> randList = new ArrayList<>(); // 몇 번째 단어인지 인덱스
        for (int i=0; i<filteredWordCount; i++) {
            rand = new Random();
            randomNum = min_val + rand.nextInt((max_val - min_val) + 1);
            randList.add(randomNum);
        }

        // 3. 필터링 달라지는 로직을 만들어
        // 단어 > *
        List<String> splittedTextCopy = new ArrayList<>(splittedText);
        for (Integer ran : randList) {
            // 단어의 길이
            String word = splittedText.get(ran.intValue());
            String star = "*";
            String repeatedStar = star.repeat(word.length());
            splittedText.set(ran, repeatedStar);
        }

        String filteredDescription = String.join(" ", splittedText);

        // 4. 디데이 계산
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime arriveAt = rocketEntity.getFinalArrival();

        Period period = Period.between(LocalDate.from(current), LocalDate.from(arriveAt));

        int dDay = period.getDays();
        String dDayString;
        if (dDay == 0) dDayString = "D - DAY";
        else dDayString = dDay + "일 남음";

        return new MemoResponseDto(
            dDayString,
            randomMemo.getPhotoUrl(),
            randomMemo.getMember().getNickname(),
            filteredDescription
        );
    }

}
