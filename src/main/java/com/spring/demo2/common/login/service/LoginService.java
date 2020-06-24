package com.spring.demo2.common.login.service;

import com.spring.demo2.common.login.dao.LoginDao;
import com.spring.demo2.common.login.dto.LoginHst;
import com.spring.demo2.common.login.dto.LoginUserPrincipal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

  @Autowired private LoginDao loginDao;

  @Override
  public LoginUserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
    return loginDao.selectLoginInfoByLoginId(username);
  }

  public List<String> selectRoleByLoginInfoSeq(String userSeq){
    return loginDao.selectRoleByLoginInfoSeq(userSeq);
  }

  public int insertLoginHst(LoginHst userLoginHst) {
    return loginDao.insertLoginHst(userLoginHst);
  }
}
