package io.dtchain.Controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.dtchain.Service.userService;

@Controller
public class userController {

	@Resource
	private userService userservice;
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(){
		return "login";
	}
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> checkLogin(String username,String password,HttpServletRequest req){
		
		return userservice.checkLogin(username, password,req);
	}
	/*
	 * 修改密码请求
	 * 密码修改成功直接重定向到登录页面
	 * 反之重新到密码修改页面
	 */
	@RequestMapping(value="/updatePwd",method=RequestMethod.POST)
	public String updatePwd(String username,String pwd){
		
		return userservice.updatePwd(username, pwd);
	}
	/*
	 * 修改密码页面
	 */
	@RequestMapping(value="/update",method=RequestMethod.GET)
	public String update(){
	
		return "update";
	}
	/*
	 * 查询用户
	 */
	@RequestMapping(value="/queryUser",method=RequestMethod.POST)
	@ResponseBody
	public String queryUser(HttpServletRequest req){
		return userservice.queryUser(req);
	}
	/*
	 * 跳转修改密码成功界面
	 */
	@RequestMapping(value="/success",method=RequestMethod.GET)
	public String success(){
		return "success";
	}
}
