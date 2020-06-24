package com.spring.demo2.common.login.dao;

import com.spring.demo2.common.login.dto.LoginUserPrincipal;
import com.spring.demo2.common.login.dto.LoginHst;
import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {

  @Autowired @Qualifier("mainSqlSession")
  private SqlSessionTemplate session;

  public LoginUserPrincipal selectLoginInfoByLoginId(String userId) {
    return session.selectOne("loginMapper.selectLoginInfoByLoginId", userId);
  }

  public List<String> selectRoleByLoginInfoSeq(String userSeq){
    return session.selectList("loginMapper.selectRoleByLoginInfoSeq", userSeq);
  }

  public int insertLoginHst(LoginHst userLoginHst) {
    return session.insert("loginMapper.insertLoginHst", userLoginHst);
  }
}
