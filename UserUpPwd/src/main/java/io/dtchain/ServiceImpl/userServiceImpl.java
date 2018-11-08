package io.dtchain.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import io.dtchain.Service.userService;
import io.dtchain.config.Ldap;
@Service("userService")
public class userServiceImpl implements userService{
	@Resource
	private Ldap ldap;
	@Override
	public Map<String,Object> checkLogin(String username, String password,HttpServletRequest req) {
		HttpSession session=req.getSession();
		Map<String,Object> map=new HashMap<String,Object>();
		//从OpenLDAP进行身份验证
		if(username!=""&&password!=""&&ldap.getLoginContext(username, password)){
			session.setAttribute("username", username);
			//return "redirect:update";
			
			map.put("state", 1);
			return map;
		}else{
			
			map.put("state", 0);
			
			//return "forward:login";
			return map;
		}
		
	}
	@Override
	public String updatePwd(String username, String pwd) {
		//从OpenLDAP进行密码修改  true---修改成功      false---修改失败
		if(ldap.updatePasswordLdap(username, pwd)){
			return "redirect:success";
		}else{
			return "redirect:update";
		}
		
	}
	@Override
	public String queryUser(HttpServletRequest req) {
		HttpSession session=req.getSession();
		
		return (String)session.getAttribute("username");
	}

}
