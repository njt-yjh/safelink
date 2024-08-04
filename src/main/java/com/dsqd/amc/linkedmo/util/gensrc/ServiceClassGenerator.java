package com.dsqd.amc.linkedmo.util.gensrc;

public class ServiceClassGenerator {
    private String className = "MyClass";

    public String generateServiceClass() {
        StringBuilder classBuilder = new StringBuilder();

        // 패키지 및 import 문
        classBuilder.append("import org.apache.ibatis.session.SqlSession;\n");
        classBuilder.append("import org.apache.ibatis.session.SqlSessionFactory;\n");
        classBuilder.append("import java.util.List;\n\n");

        // 클래스 선언
        classBuilder.append("public class ").append(className).append("Service {\n\n");

        // SqlSessionFactory 변수 선언
        classBuilder.append("    private SqlSessionFactory sqlSessionFactory;\n\n");

        // 생성자
        classBuilder.append("    public ").append(className).append("Service() {\n");
        classBuilder.append("        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();\n");
        classBuilder.append("    }\n\n");

        // insert 메서드
        classBuilder.append("    public void insert").append(className).append("(").append(className).append(" data) {\n");
        classBuilder.append("        try (SqlSession session = sqlSessionFactory.openSession()) {\n");
        classBuilder.append("            ").append(className).append("Mapper mapper = session.getMapper(").append(className).append("Mapper.class);\n");
        classBuilder.append("            mapper.insert").append(className).append(" (data);\n");
        classBuilder.append("            session.commit();\n");
        classBuilder.append("        }\n");
        classBuilder.append("    }\n\n");

        // update 메서드
        classBuilder.append("    public void update").append(className).append("(").append(className).append(" data) {\n");
        classBuilder.append("        try (SqlSession session = sqlSessionFactory.openSession()) {\n");
        classBuilder.append("            ").append(className).append("Mapper mapper = session.getMapper(").append(className).append("Mapper.class);\n");
        classBuilder.append("            mapper.update").append(className).append(" (data);\n");
        classBuilder.append("            session.commit();\n");
        classBuilder.append("        }\n");
        classBuilder.append("    }\n\n");

        // getMyClassById 메서드
        classBuilder.append("    public ").append(className).append(" get").append(className).append("ById(int id) {\n");
        classBuilder.append("        try (SqlSession session = sqlSessionFactory.openSession()) {\n");
        classBuilder.append("            ").append(className).append("Mapper mapper = session.getMapper(").append(className).append("Mapper.class);\n");
        classBuilder.append("            return mapper.get").append(className).append("ById(id);\n");
        classBuilder.append("        }\n");
        classBuilder.append("    }\n\n");

        // getMyClassByData 메서드
        classBuilder.append("    public List<").append(className).append("> get").append(className).append("ByData(").append(className).append(" data) {\n");
        classBuilder.append("        try (SqlSession session = sqlSessionFactory.openSession()) {\n");
        classBuilder.append("            ").append(className).append("Mapper mapper = session.getMapper(").append(className).append("Mapper.class);\n");
        classBuilder.append("            return mapper.get").append(className).append("ByData(data);\n");
        classBuilder.append("        }\n");
        classBuilder.append("    }\n\n");

        // getMyClassAll 메서드
        classBuilder.append("    public List<").append(className).append("> get").append(className).append("All() {\n");
        classBuilder.append("        try (SqlSession session = sqlSessionFactory.openSession()) {\n");
        classBuilder.append("            ").append(className).append("Mapper mapper = session.getMapper(").append(className).append("Mapper.class);\n");
        classBuilder.append("            return mapper.get").append(className).append("All();\n");
        classBuilder.append("        }\n");
        classBuilder.append("    }\n\n");

        // delete 메서드
        classBuilder.append("    public void delete").append(className).append("(int id) {\n");
        classBuilder.append("        try (SqlSession session = sqlSessionFactory.openSession()) {\n");
        classBuilder.append("            ").append(className).append("Mapper mapper = session.getMapper(").append(className).append("Mapper.class);\n");
        classBuilder.append("            mapper.delete").append(className).append("(id);\n");
        classBuilder.append("            session.commit();\n");
        classBuilder.append("        }\n");
        classBuilder.append("    }\n\n");

        // 클래스 끝
        classBuilder.append("}\n");

        return classBuilder.toString();
    }

    public static void main(String[] args) {
        ServiceClassGenerator generator = new ServiceClassGenerator();
        String classSource = generator.generateServiceClass();
        System.out.println(classSource);
    }
}

