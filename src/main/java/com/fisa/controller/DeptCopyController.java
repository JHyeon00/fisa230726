package com.fisa.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fisa.exception.DeptNotFoundException;
import com.fisa.model.dao.DeptCopyRepository;
import com.fisa.model.domain.entity.DeptCopy;

@RestController
public class DeptCopyController {

	//DAO를 멤버로 선언 및 자동 초기화
	@Autowired
	private DeptCopyRepository dao;
	
	//모든 검색
	//http://localhost/guestbook/deptall
	@GetMapping("deptall")
	public Iterable<DeptCopy> getDeptAll() {
		
		System.out.println(dao.findAll());
		
		return dao.findAll();
	}
	
	//특정 부서 번호로 검색
	//http://localhost/guestbook/deptone?deptno=10  존재
	
	//http://localhost/guestbook/deptone?deptno=100 존재x
	
	/* 
	 * Optional AP는 객체를 보유하게 되는 객체 컨테이너
	 * 데이터가 있으면 get()으로 데이터 활용
	 * 없으면 get() 사용 금지, 에러 발생
	 * Optional 장점 : 간결한 코드로 예외 처리 가능
	 */
	@GetMapping("/deptone")
	public DeptCopy getDept(int deptno) throws Exception {
		//id값으로 spring data jpa의 메소드가 검색해서 반환
//		System.out.println(dao.findById(deptno).get());	//데이터 없을 경우 실행 예외 발생
		Optional<DeptCopy> dept = dao.findById(deptno);
		System.out.println(dept); //Optional.empty
		
		/* 부서번호 존재 : DeptCopy(deptno = 10, dname = ACCOUNTING, loc = NEW YORK)
		 * 부서번호 미존재 : 데이터 없음
		 * 개발자 관점에서 확인용 코드로 간주
		 */
		dept.ifPresentOrElse(System.out::println, () -> System.out.println("데이터 없음"));
		
		//client에게 상태 보고 로직
		//데이터가 없을 땐 error.jsp 등으로 일괄 메세지 위임 즉 예외 발생을 유도
		dept.orElseThrow(Exception::new);	//데이터 null인 경우 예외 생성
		
		//검색된 데이터 반환
		return dept.get();	//예외 발생이 안 된 경우에만 실행 즉 데이터가 있을 경우에만 get()
	}
	
	//? 특정 부서 번호로 삭제
	/* 존재할 경우 삭제 성공
	* 미존재하는 부서 번호로 삭제 시도 시 삭제 실패 - 상황도 client에게 전달
	* 	- 예외 처리로 처리
	*/
	//http://localhost/deptdelete?deptno=10
	//http://localhost/deptdelete?deptno=100
	@GetMapping("deptdelete")
	public String deleteDept(int deptno) throws DeptNotFoundException {
		
		dao.findById(deptno).orElseThrow(DeptNotFoundException::new); //로직 중지
		dao.deleteById(deptno);	//존재할 경우 실행되는 라인
		
		return "delete 성공";
	}
		
	
	//예외 전장 처리 메소드
	@ExceptionHandler
	public String exHandler(Exception e) {
		
			e.printStackTrace();
			
			return "요청 시 입력 데이터 재확인 부탁드립니다.";
	}
	
}
