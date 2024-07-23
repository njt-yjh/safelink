package com.dsqd.amc.linkedmo.model;

public class MyClass {

    private String name;
    private int num;

    public MyClass() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }

    public static class Builder {
        private String name;
        private int num;

        public Builder(String name) {
            this.name = name;
        }

        public Builder num(int num) {
            this.num = num;
            return this;
        }

    }

    public MyClass(Builder builder) {
        this.name = builder.name;
        this.num = builder.num;
    }
}
