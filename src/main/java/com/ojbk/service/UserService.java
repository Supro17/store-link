package com.ojbk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.UserDTO;
import com.ojbk.entity.User;
import com.ojbk.vo.UserVO;

public interface UserService extends IService<User> {

    public PageResult<UserVO> userPage(PageDTO dto);

    public UserVO userCreate(UserDTO dto);


    public UserVO userUpdate(UserDTO dto);

    public Result userDelete(Long id);

    public UserVO selectUserById(Long id);




}
