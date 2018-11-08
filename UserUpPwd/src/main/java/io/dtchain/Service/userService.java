package io.dtchain.Service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

public interface userService {
	public Map<String,Object> checkLogin(String username,String password,HttpServletRequest req);
	public String updatePwd(String username,String pwd);
	public String queryUser(HttpServletRequest req);
}
