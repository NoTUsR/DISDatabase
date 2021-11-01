package com.khachatryan.DISTask;

import java.util.Date;
import java.sql.SQLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main  {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		SpringApplication.run(Main.class, args);
		DB.Connect();
		DB.CreateTables();


	}

}
