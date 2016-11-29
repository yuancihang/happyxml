package io.happyxml.example;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="user_buyer_get_response")
public class UserBuyerResponse {

//	@XmlElementWrapper(nillable=true)
	@XmlElement(name="user") 
	private List<User> userList = new ArrayList<>();
	
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	@XmlAccessorType(XmlAccessType.NONE)
	public static class User {
		@XmlElement(name="nick") 
	    private String nick;
		@XmlElement(name="sex") 
	    private String sex;
		@XmlElement(name="avatar") 
	    private String avatar;
	    
		public String getNick() {
			return nick;
		}
		public void setNick(String nick) {
			this.nick = nick;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getAvatar() {
			return avatar;
		}
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}
		
		@Override
		public String toString(){
			return nick + "," + sex + "," + avatar;
		}

	}
}
