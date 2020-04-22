package com.leansecurity.web.controller;

import com.leansecurity.dto.User;
import com.leansecurity.dto.UserQueryCondition;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjiantao
 * @date 2020/4/21 11:07
 */
@RestController
public class UserController {

    @GetMapping("/user")
    public List<User> query(UserQueryCondition condition,
                            @PageableDefault(page = 2, size = 17, sort = "username,asc") Pageable pageable) {
//        System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));
//
//        System.out.println(pageable.getPageSize());
//        System.out.println(pageable.getPageNumber());
//        System.out.println(pageable.getSort());

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        return users;
    }

    //@JsonView(User.UserDetailView.class)
    @GetMapping("/{id:\\d+}")
    public User getInfo(@PathVariable String id) {
//		throw new RuntimeException("user not exist");
        User user = new User();
        user.setUsername("tom");
        return user;
    }
}
