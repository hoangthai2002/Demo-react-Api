package vn.demo.jobhunter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class RestLoginDTO {
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    // lớp lồng nhau (inner class) là một lớp được khai báo bên trong một lớp khác.
    // Lớp bên ngoài được gọi là lớp "bao" (outer class) hoặc lớp "cấp cao nhất".
    public static class UserLogin {
        private long id;
        private String email;
        private String name;

    }

}
