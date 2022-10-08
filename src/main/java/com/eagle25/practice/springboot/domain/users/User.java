package com.eagle25.practice.springboot.domain.users;

import com.eagle25.practice.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    /*
    @Enumerated(EnumType.STRING)

    JPA로 데이터베이스에 Enum값을 저장할 때 어떤 형태로 저장할지 결정한다.
    기본적으로는 int로 Enum값을 저장하도록 되어 있다.
    int로 enum값을 DB에 저장할 경우, 어떤 값인지 알 수 없으므로 string 형태로 저장될 수 있도록 선언한다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role)
    {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
