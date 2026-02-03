package com.example.jpa.domain.user.repository;

import com.example.jpa.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
        Member findMemberByPhone(String phone);

        List<Member> findByAge(int age);

        Member findByNameAndAddress(String name, String address);

        List<Member> findByAgeGreaterThanEqual(int age);

        List<Member> findByAddressLike(String address);

        List<Member> findByAddressOrderByAgeAsc(String address);

        @Query("select m from Member m where m.address like concat('%', :address, '%') order by m.age asc")
        List<Member> findByAddressAgeAsc(@Param("address") String address);

        @Query("select m from Member m where m.age >= :age order by m.age desc")
        List<Member> findByAgeOrderByDesc(@Param("age") int age);
}
