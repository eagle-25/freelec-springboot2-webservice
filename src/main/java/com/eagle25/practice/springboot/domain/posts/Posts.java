package com.eagle25.practice.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor // 기본 생성자 자동 추가. (public Posts() {}와 같은 효과임.)
@Entity // 테이블과 링크될 클래스를 나타냄.
public class Posts {
    @Id // 해당 테이블의 PK를 나타냄.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pk의 생성 규칙을 나타냄.
    private long id;

    // 클래스 내 모든 필드는 @Column을 선언하지 않더라도 칼럼이 됨.
    // 기본 값 외에 다른값을 지정하고자 하는 경우, @Column에서 설정을 구성하면됨.
    @Column(length =  500, nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    private String author;

    @Builder // 클래스의 빌더 패턴 클래스 생성.
    public Posts(String title, String content, String author)
    {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    /*
    Entity class에서는 절대로 Setter를 생성하지 않는다.
        - 이유
            1. 클래스의 인스턴스 값들이 언제 어디서 변해야 하는지 코드상으로 명확하게 구분할 수 없기 때문이다.
            2. 1번 이유로 인해 차후 기능 변경시 복잡해 진다.

        - 예시
            * 잘못된 사용 *

                Public class Order {
                    public void setStatus(boolean status) {
                        this.status = status;
                    }
                }

                public void 주문취소에서의_이벤트 () {
                    order.setStatus(false);
                }

            * 올바른 사용 *

                Public class Order {
                    public void cancelOrder() {
                        this.status = false;
                    }
                }

                public void 주문취소에서의_이벤트 () {
                    order.cancelOrder();
                }

        Setter가 없다면 Insert는 어떻게 수행하죠?

        기본 구조: 생성자를 통해 최종값을 채훈 후 DB에 삽입해야 한다.

        Tip: @Builder를 통해 제공되는 builder class를 사용하자.
        이유
            1. new Example(b, a)가 있다고 가정한다. a와 b의 위치를 변경해도 코드를 실행하기 전 까지는 문제를 찾을 수 없다.

            public Example(string a, string b) {
                this.a = a;
                this.b = b;
            }

            new Example(b, a); -> 인스턴스에 들어가는 값의 위치가 바뀐다.


            2. builder를 사용하면 어느 필드에 어떤 값을 채워야 할지 명확하게 인지할 수 있다.

            Example.builder()
                .a(a)
                .b(b)
                .build()

    Reference: p.92 ~ 94

     */
}
