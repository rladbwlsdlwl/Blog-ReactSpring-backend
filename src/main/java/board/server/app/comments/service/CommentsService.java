package board.server.app.comments.service;

import board.server.app.board.entity.Board;
import board.server.app.board.repository.JdbcTemplateBoardRepository;
import board.server.app.comments.dto.CommentsResponseDto;
import board.server.app.comments.entity.Comments;
import board.server.app.comments.repository.JdbcTemplateCommentsRepository;
import board.server.app.member.repository.JdbcTemplateMemberRepository;
import board.server.error.errorcode.CustomExceptionCode;
import board.server.error.exception.BusinessLogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommentsService {
    @Autowired
    private JdbcTemplateCommentsRepository jdbcTemplateCommentsRepository;
    @Autowired
    private JdbcTemplateBoardRepository jdbcTemplateBoardRepository;
    @Autowired
    private JdbcTemplateMemberRepository jdbcTemplateMemberRepository;


    // 댓글 읽기
    public Map<Long, List> getComments(List<Long> boardIdList){
        Map<Long, List> commentsMapList = new HashMap<>();

        for(Long boardId: boardIdList){
            List<CommentsResponseDto> responseDtoList = jdbcTemplateCommentsRepository.findByBoardId(boardId)
                    .stream()
                    .map(CommentsResponseDto::new)
                    .toList();

            commentsMapList.put(boardId, responseDtoList);
        }

        return commentsMapList;
    }

    // 댓글 추가
    public Comments setComments(Comments comments){
        validatePresentBoardId(comments.getBoardId());
        validatePresentMemberId(comments.getAuthor());
        validatePresentId(comments.getParentId());

        return jdbcTemplateCommentsRepository.save(comments);
    }

    // 댓글 수정
    public void updateComments(Comments comments){
        validatePresentId(comments.getId());

        jdbcTemplateCommentsRepository.update(comments);
    }

    // 댓글 삭제
    public void removeComments(Long commentsId, Long userId){
        validatePresentMemberId(userId);
        Comments comments = validatePresentId(commentsId);
        Long boardId = comments.getBoardId(), commentsAuthor = comments.getAuthor();
        Long author = validatePresentBoardId(boardId).getAuthor();

        // 댓글 작성자 또는 게시글 작성자만 허용
        if(author != userId && commentsAuthor != userId)
            throw new BusinessLogicException(CustomExceptionCode.MEMBER_NO_PERMISSION);


        jdbcTemplateCommentsRepository.delete(commentsId);
    }


    // 대댓글 존재 여부 확인 - 무결성 제약 조건, id <- parent_id
    // 댓글 존재 여부 확인
    private Comments validatePresentId(Long id) {
        if(id != null && id != 0){
            return jdbcTemplateCommentsRepository.findById(id).orElseThrow(() ->
                    new BusinessLogicException(CustomExceptionCode.COMMENTS_NO_PERMISSION)
            );
        }
        return null;
    }

    // 유저 확인
    private void validatePresentMemberId(Long author) {
        jdbcTemplateMemberRepository.findById(author).orElseThrow(() ->
                new BusinessLogicException(CustomExceptionCode.MEMBER_NOT_FOUND)
        );
    }

    // 게시글 확인
    private Board validatePresentBoardId(Long boardId) {
        return jdbcTemplateBoardRepository.findById(boardId).orElseThrow(() ->
                new BusinessLogicException(CustomExceptionCode.BOARD_NOT_FOUND)
        );
    }

}
