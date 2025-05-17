package com.example.demo3.lombok;

public class Text {
    public static void main(String[] args) {
        User user = new User();
        user.setId(1L);
        user.setName("la");
        user.setAge(22);

        User user1= new User();
        user1.setId(3L);
        user1.setName("la");
        user1.setAge(22);

        System.out.println(user1.equals(user));
        System.out.println(user.getName());
    }
}
