package com.example.boardv1.board;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 데이터베이스 세상의 테이블을 자바 세상에 모델링한 결과 = 엔티티
 */

@NoArgsConstructor // 디폴트 생성자
@Data // 게터, 세터, 투스트링
@Entity // 해당 어노테이션을 보고, 컴퍼넌트 스캔 후, 데이터베이스 테이블을 생성한다.
@Table(name = "board_tb")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    @CreationTimestamp
    private Timestamp createdAt;

    public void 게시글수정(String content) {
        this.content = content;
    }

}
