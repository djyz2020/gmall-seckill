package com.gmall.seckill.po;

import lombok.*;

import java.util.Date;

@Data
public class User {
	private int id;

	private String userName;

	private String phone;

	private String password;

	private String salt;

	private String head;

	private int loginCount;

	private Date registerDate;

	private Date lastLoginDate;
}
