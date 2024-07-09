package com.dsqd.amc.linkedmo.config;

import com.dsqd.amc.linkedmo.util.AES256Util;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class MyBatisConfig {

    private static SqlSessionFactory sqlSessionFactory;

    public static void init(String env) {
        try {
            Properties properties = new Properties();
            properties.load(Resources.getResourceAsStream("application.properties"));

            String encryptedPassword = properties.getProperty("db.password.encrypted");
            String aesKey = properties.getProperty("aes.key");
            String decryptedPassword = AES256Util.decrypt(encryptedPassword, aesKey);

            String dbUrlKey = "db.url." + env;
            String dbUrl = properties.getProperty(dbUrlKey);
            if (dbUrl == null) {
                throw new RuntimeException("Database URL not found for environment: " + env);
            }
            
            PooledDataSource dataSource = new PooledDataSource();
            dataSource.setDriver("org.mariadb.jdbc.Driver");
            dataSource.setUrl(dbUrl);
            dataSource.setUsername(properties.getProperty("db.username"));
            dataSource.setPassword(decryptedPassword);

            Reader reader = Resources.getResourceAsReader(properties.getProperty("mybatis.config-location"));
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            sqlSessionFactory.getConfiguration().setEnvironment(new org.apache.ibatis.mapping.Environment("development", new org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory(), dataSource));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
