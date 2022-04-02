package hello.servlet.basic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter //롬복을 이용해서 겟,셋을 이렇게 설정할 수 있다
public class HelloData {

    private String username;
    private int age;


}

