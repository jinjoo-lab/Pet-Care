package com.example.petcare.module.pet.entity;

public enum PetType {

    DOG("강아지"), CAT("고양이"), BIRD("새"), REPTILE("파충류"), OTHER("기타");

    private String name;

    PetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
