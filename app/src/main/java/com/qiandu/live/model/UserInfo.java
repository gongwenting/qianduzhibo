package com.qiandu.live.model;

import com.qiandu.live.http.IDontObfuscate;

/**
 * @description: 用户信息
 * @author: Andruby
 * @time: 2016/10/31 18:07
 */
public class UserInfo extends IDontObfuscate {
		public int regok;
		public int smsok;

		public String userid;
		public String nickname;
		public String avatar;
		public int isatten;
		public String username;
		//	public String headPicSmall;
		public String sigId;
		public String sdkAppId;
		public String sdkAccountType;
		public String gender;
		public String desc;

		public UserInfo() {
		}

		public UserInfo(String userid, String nickname, String avatar, String gender ,String desc) {
			this.userid = userid;
			this.nickname = nickname;
			this.avatar = avatar;
			this.gender = gender;
			this.desc=desc;
		}

	public UserInfo(String userid, String nickname, String avatar) {
		super();
	}
}
