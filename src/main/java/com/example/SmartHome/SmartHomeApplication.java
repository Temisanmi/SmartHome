package com.example.SmartHome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartHomeApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmartHomeApplication.class, args);
	}
}

// & "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U smarthome -d smart_home -h localhost
// \dt  to see columns on table

// SELECT id, username, email, role FROM app_user ORDER BY username;   checking current users with account
// SELECT * FROM home;   to see created homes

// DELETE FROM app_user WHERE id = < >;

//if the user you want to delete already has a home
// UPDATE home SET user_id = NULL WHERE user_id = < >;


















