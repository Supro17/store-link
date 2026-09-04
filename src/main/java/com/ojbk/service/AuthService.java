package com.ojbk.service;

import com.ojbk.dto.LoginDTO;
import com.ojbk.vo.LoginVO;

public interface AuthService {

    LoginVO login(LoginDTO loginDTO);


}
