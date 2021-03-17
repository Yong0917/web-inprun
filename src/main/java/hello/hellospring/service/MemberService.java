package hello.hellospring.service;


import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    @Autowired		//연결시켜줘야되는데 Repository랑 연결시켜주는거  DI
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    // 회원가입
    public Long join(Member member) {
        //같으 이름 회원 x
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }


    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())		//Optional<Member> result을 반환
                .ifPresent(m -> {							//extract method할떄 단축히 alt shift m
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                });
    }

    //전체 회원 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }



    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

}
