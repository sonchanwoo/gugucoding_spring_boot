package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

	@Autowired
	MemoRepository memoRepository;

	@Test
	public void testClass() {
		System.out.println(memoRepository.getClass().getName());
	}

	@Test
	public void testInsertDummies() {
		IntStream.rangeClosed(1, 10).forEach(i -> {
			Memo memo = Memo.builder().memoText("sample..." + i).build();
			memoRepository.save(memo);//save : 없으면 insert, 있으면 update
		});
	}

	@Test
	public void testSelect() {
		Long mno = 10L;

		Optional<Memo> result = memoRepository.findById(mno);//findById는 즉시 sql을 실행한다.

		if (result.isPresent()) {
			Memo memo = result.get();
			System.out.println(memo);
		}
	}

	@Test
	public void testUpdate() {
		Memo memo = Memo.builder().mno(21L).memoText("insert or update ?...").build();
		System.out.println(memoRepository.save(memo));
		//실행결과를 보면 내부적으로 select문 수행하여 해당 @Id가 있는지 확인하고 수행하기 위한 부분이 포함되어 있다.
	}

	@Test
	public void testDelete() {
		Long mno = 10l;

		memoRepository.deleteById(mno);//없으면 예외를 발생시킨다.
		//마찬가지로 select실행 후 delete 하는 방식으로 동작한다.
	}

	@Test
	public void testPageDefault() {
		Pageable pageable = PageRequest.of(0, 10);//0-indexed
		//1페이지의 10개를 가져와라.

		Page<Memo> result = memoRepository.findAll(pageable);
		//기본적으로 목록을 가져오되, 데이터가 충분하면 total도 가져온다.

		System.out.println(result);
		//로그를 보면 limit로 10개 가져오고, count로 total을 가져온다.

		//Page<>타입은 여러 좋은 메서드들을 제공한다. (총페이지, 토탈, 현재페이지번호, 페이지당 개수, 다음페이지존재, 시작페이지여부, 목록받기 등)

	}

	@Test
	public void testSort() {
		Sort sort1 = Sort.by("mno").descending();//order by mno desc
		Sort sort2 = Sort.by("memoText").ascending();//order by memoText asc
		Sort sortAll = sort1.and(sort2);//and

		Pageable pageable = PageRequest.of(0, 10, sortAll);//정렬 조건 추가한 오버라이딩함수

		Page<Memo> result = memoRepository.findAll(pageable);

		result.get().forEach(memo -> {
			System.out.println(memo);
		});
	}

	@Test
	public void testQueryMethods() {
		List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(13l, 19l);

		for (Memo memo : list)
			System.out.println(memo);
	}

	@Test
	public void testQueryMethodsWithPageable() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

		Page<Memo> result = memoRepository.findByMnoBetween(13l, 19l, pageable);

		result.get().forEach(memo -> {
			System.out.println(memo);
		});
	}

	@Test
	public void testJpql1() {
		//memoRepository.getListDesc();

		Pageable pageable = PageRequest.of(1, 10, Sort.by("mno").descending());
		Page<Memo> result = memoRepository.getListWithQuery(0l, pageable);
		result.get().forEach(memo -> System.out.println(memo));

		//memoRepository.updateMemoText(1l,"랄랄라");

		//memoRepository.updateMemoText(new Memo(2l,"히히히"));

		//List<Object[]> result = memoRepository.getListWithQueryObject(7l);

		//memoRepository.getNativeResult();

	}

	@Test
	@Commit//rollback 기점
	@Transactional//??
	public void testDeleteQueryMethods() {
		//memoRepository.deleteMemoByMnoLessThan(5l);//하나씩 삭제됨
	}
}
