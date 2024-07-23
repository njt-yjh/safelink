package com.dsqd.amc.linkedmo.util;

public class MapperInterfaceGenerator {
    private String className = "MyClass";

    public String generateMapperInterface() {
        StringBuilder interfaceBuilder = new StringBuilder();

        // 패키지 및 인터페이스 선언
        interfaceBuilder.append("import org.apache.ibatis.annotations.Mapper;\n");
        interfaceBuilder.append("import org.apache.ibatis.annotations.Param;\n");
        interfaceBuilder.append("import java.util.List;\n\n");

        interfaceBuilder.append("@Mapper\n");
        interfaceBuilder.append("public interface ").append(className).append("Mapper {\n\n");

        // 메서드 선언
        interfaceBuilder.append("    ").append(className).append(" get")
            .append(className).append("ById(@Param(\"id\") int id);\n\n");
        
        interfaceBuilder.append("    List<").append(className).append("> get")
            .append(className).append("ByData(").append(className).append(" data);\n\n");
        
        interfaceBuilder.append("    List<").append(className).append("> get")
            .append(className).append("All();\n\n");
        
        interfaceBuilder.append("    void insert").append(className).append("(")
            .append(className).append(" data);\n\n");
        
        interfaceBuilder.append("    void update").append(className).append("(")
            .append(className).append(" data);\n\n");
        
        interfaceBuilder.append("    void delete").append(className).append("(@Param(\"id\") int id);\n\n");

        // 인터페이스 끝
        interfaceBuilder.append("}\n");

        return interfaceBuilder.toString();
    }

    public static void main(String[] args) {
        MapperInterfaceGenerator generator = new MapperInterfaceGenerator();
        String interfaceSource = generator.generateMapperInterface();
        System.out.println(interfaceSource);
    }
}

