package com.example.back_end.service.interfac;

import com.example.back_end.dto.LoginRequest;
import com.example.back_end.dto.Response;
import com.example.back_end.entity.User;

public interface IUserService {
    Response register(User user);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response deleteUser(Long userId);
    Response getUserById(Long userId);
    Response getMyInfo(String email);
}
