package com.ojbk.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ojbk.entity.User;
import com.ojbk.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class PasswordMigrator implements CommandLineRunner {


    @Resource
    private UserMapper userMapper;


    @Override
    public void run(String... args) throws Exception {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(User::getPassword);

        List<User> users = userMapper.selectList(wrapper);

        int migrate = 0;
        for (User user :users){
            String password = user.getPassword();

            if (password != null &&  !password.startsWith("$2a$") && !password.startsWith("$2b$")){
                String hash = BCrypt.hashpw(password, BCrypt.gensalt());
                LambdaUpdateWrapper<User> wrapper1 = new LambdaUpdateWrapper<>();
                wrapper1.eq(User::getUserId,user.getUserId()).set(User::getPassword, hash);
                userMapper.update(null,wrapper1);
                migrate++;
            }
        }
        if (migrate>0){
            log.info("密码迁徙完成，共迁徙{}条明文密码为Bcrypt哈希",migrate);
        }


    }
}
