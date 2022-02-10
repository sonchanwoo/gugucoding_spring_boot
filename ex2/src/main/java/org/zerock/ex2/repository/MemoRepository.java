package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.List;

//인터페이스 선언만으로도 스프링의 빈으로 등록
public interface MemoRepository extends JpaRepository<Memo, Long> {

	//쿼리메서드 : 메서드의 이름 자체가 질의문이 된다 -> 이름 잘 써야한다,,
	List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

	//정렬과 결합도 가능, 이름이 간단해져서 보통 정렬은 빼 놓고 만드는 경우가 많다.
	Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

	void deleteMemoByMnoLessThan(Long num);

	@Query("select memo from Memo memo order by memo.mno desc")
	List<Memo> getListDesc();

	//파라미터가 필드(컬럼)일 때 :xxx 으로 !
	//pageable을 줘서 할거면 countQuery속성 적용!
	@Query(value = "select memo from Memo memo where memo.mno > :mno", countQuery = "select count(memo) from Memo memo where memo.mno > :mno ")
	Page<Memo> getListWithQuery(Long mno, Pageable pageable);

	//파라미터가 필드(컬럼)일 때 :xxx 으로 !
	//delete, update, insert dml(select 제외)는 두 어노테이션을 붙여줘야 한다.
	@Query("update Memo memo set memo.memoText = :memoText where memo.mno = :mno")
	@Transactional
	@Modifying
	int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

	//파라미터가 객체(엔티티)일 때 : :#{} 으로 !
	@Query("update Memo memo set memo.memoText = :#{#param.memoText} where memo.mno = :#{#param.mno}")
	@Transactional
	@Modifying
	int updateMemoText(@Param("param") Memo memo);

	//JPQL의 장점은 Object도 받아올 수 있음
	//나중에 조인 등으로 적당한 엔티티타입이 존재하지 않을 경우에
	@Query("select memo.mno, memo.memoText, current_date from  Memo memo where memo.mno > :mno")
	List<Object[]> getListWithQueryObject(Long mno);

	//어쩔 수 없는 경우에 사용할 Native SQL도 있으며, nativeQuery를 true로 놓고 사용한다.
	@Query(value = "select * from tbl_memo where mno > 0", nativeQuery = true)
	List<Object[]> getNativeResult();
}
