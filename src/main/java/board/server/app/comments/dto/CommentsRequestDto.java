package board.server.app.comments.dto;


import board.server.app.comments.entity.Comments;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentsRequestDto {
    @NotNull
    private Long boardId;
    @NotNull
    private Long author;
    private Long parentId;
    @NotNull
    private String contents;

    public static Comments of(CommentsRequestDto commentsRequestDto){
        return Comments.builder()
                .boardId(commentsRequestDto.getBoardId())
                .author(commentsRequestDto.getAuthor())
                .parentId(commentsRequestDto.getParentId())
                .contents(commentsRequestDto.getContents())
                .build();
    }
}
