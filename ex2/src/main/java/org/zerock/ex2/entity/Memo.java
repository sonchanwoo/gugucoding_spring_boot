package org.zerock.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity//엔티티를 위한 클래스이며, jpa로 관리되는 엔티티인스턴스라는 것을 의미
//옵션에 따라서 자동으로 테이블을 생성할 수도 있음
@Table(name = "tbl_memo")//테이블의 이름은 tbl_meno임, 인덱스도 설정가능
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
    @Id//pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment.., 사용자가 입력하는게아니면 자동생성
    private Long mno;

    @Column(length = 200, nullable = false)//default도 지정할수있음
    //테이블에는 칼럼으로써 생성안하고 싶은 건 @Transient라는 어노테이션 사용
    private String memoText;
}
