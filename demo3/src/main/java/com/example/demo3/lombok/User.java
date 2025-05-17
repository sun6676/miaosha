package com.example.demo3.lombok;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(exclude={"id","age"})
public class User {
    private Long id;
    private String name;
    private Integer age;
}
