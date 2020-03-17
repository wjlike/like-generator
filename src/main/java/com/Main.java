package com;

import com.like.generate.Generate;

import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * @author  like
 * 启动类
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, SQLException, URISyntaxException {
        new Generate().Create(Main.class);
    }
}
