package com.dsqd.amc.linkedmo.util.gensrc;

import java.util.Arrays;

public class ModelClassGenerator {
    private String className = "MyClass";
    private String[] variables = {"String name", "int num"};

    public String generateClass() {
        StringBuilder classBuilder = new StringBuilder();
        
        // 패키지 및 클래스 선언
        classBuilder.append("public class ").append(className).append(" {\n\n");
        
        // 변수 선언
        Arrays.stream(variables).forEach(var -> classBuilder.append("    private ").append(var).append(";\n"));
        classBuilder.append("\n");

        // 생성자
        classBuilder.append("    public ").append(className).append("() {}\n\n");

        // Getter와 Setter 생성
        Arrays.stream(variables).forEach(var -> {
            String[] parts = var.split(" ");
            String type = parts[0];
            String name = parts[1];
            String camelCaseName = Character.toUpperCase(name.charAt(0)) + name.substring(1);

            // Setter
            classBuilder.append("    public void set").append(camelCaseName)
                .append("(").append(type).append(" ").append(name).append(") {\n")
                .append("        this.").append(name).append(" = ").append(name).append(";\n")
                .append("    }\n\n");

            // Getter
            classBuilder.append("    public ").append(type).append(" get").append(camelCaseName).append("() {\n")
                .append("        return this.").append(name).append(";\n")
                .append("    }\n\n");
        });

        // Builder 클래스 생성
        classBuilder.append("    public static class Builder {\n");
        Arrays.stream(variables).forEach(var -> {
            String[] parts = var.split(" ");
            String type = parts[0];
            String name = parts[1];
            classBuilder.append("        private ").append(type).append(" ").append(name).append(";\n");
        });
        classBuilder.append("\n");
        
        // Builder 생성자
        String firstVar = variables[0];
        String[] firstParts = firstVar.split(" ");
        String firstType = firstParts[0];
        String firstName = firstParts[1];
        classBuilder.append("        public Builder(").append(firstType).append(" ").append(firstName).append(") {\n")
            .append("            this.").append(firstName).append(" = ").append(firstName).append(";\n")
            .append("        }\n\n");

        // Builder 메서드
        Arrays.stream(variables).skip(1).forEach(var -> {
            String[] parts = var.split(" ");
            String type = parts[0];
            String name = parts[1];
            classBuilder.append("        public Builder ").append(name).append("(").append(type).append(" ").append(name).append(") {\n")
                .append("            this.").append(name).append(" = ").append(name).append(";\n")
                .append("            return this;\n")
                .append("        }\n\n");
        });

        // Builder 클래스 끝
        classBuilder.append("    }\n\n");

        // 클래스 생성자 (Builder를 사용하는)
        classBuilder.append("    public ").append(className).append("(Builder builder) {\n");
        Arrays.stream(variables).forEach(var -> {
            String name = var.split(" ")[1];
            classBuilder.append("        this.").append(name).append(" = builder.").append(name).append(";\n");
        });
        classBuilder.append("    }\n");

        // 클래스 끝
        classBuilder.append("}\n");
        
        return classBuilder.toString();
    }

    public static void main(String[] args) {
        ModelClassGenerator generator = new ModelClassGenerator();
        String classSource = generator.generateClass();
        System.out.println(classSource);
    }
}

