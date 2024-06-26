package com.yunstudio.insight.domain.like.service;

import com.yunstudio.insight.domain.answer.entity.Answer;
import com.yunstudio.insight.domain.answer.repository.AnswerRepository;
import com.yunstudio.insight.domain.like.entity.Like;
import com.yunstudio.insight.domain.like.repository.LikeRepository;
import com.yunstudio.insight.domain.user.entity.User;
import com.yunstudio.insight.global.exception.GlobalException;
import com.yunstudio.insight.global.response.CommonEmptyRes;
import com.yunstudio.insight.global.response.ResultCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final AnswerRepository answerRepository;

    /**
     * 좋아요 추가
     */
    @Transactional
    public CommonEmptyRes like(User user, Long answerId) {

        Answer answer = answerRepository.findById(answerId)
            .orElseThrow(() -> new GlobalException(ResultCase.ANSWER_NOT_FOUND));

        // 이미 좋아요를 눌렀으면 조기 반환
        boolean existsLike = likeRepository.existsByUserAndAnswer(user, answer);

        if (existsLike) {
            return new CommonEmptyRes();
        }

        // 본인이 작성한 답변에는 좋아요 불가
        if (isUserSameAuthor(user, answer)) {
            throw new GlobalException(ResultCase.SELF_LIKE_NOT_ALLOWED);
        }

        Like like = Like.builder()
            .user(user)
            .answer(answer)
            .build();

        likeRepository.save(like);

        return new CommonEmptyRes();
    }

    private boolean isUserSameAuthor(User user, Answer answer) {
        return answer.getAuthor().getId().equals(user.getId());
    }

    /**
     * 좋아요 취소.
     */
    @Transactional
    public CommonEmptyRes dislike(User user, Long answerId) {

        Answer answer = answerRepository.findById(answerId)
            .orElseThrow(() -> new GlobalException(ResultCase.ANSWER_NOT_FOUND));

        // 좋아요를 눌렀다면 삭제
        likeRepository.findByUserAndAnswer(user, answer)
            .ifPresent(likeRepository::delete);

        return new CommonEmptyRes();
    }
}
