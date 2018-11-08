package io.dtchain.config;	 
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import com.novell.ldap.LDAPAttribute;  
@Configuration
@EnableCaching
public class Ldap {
	
	   private static String URL = "ldap://127.0.0.1:389/";  
	    private static String BASEDN = "dc=dtchain,dc=io";  // 根据自己情况进行修改  
	    private static String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";  
	
	    private static DirContext ctx = null;  
	    private static final Control[] connCtls = null;
	   //管理员
	    private static String root="cn=Manager,dc=dtchain,dc=io";
	    //管理员密码
	    private static String pwd="secret"; 
	    //连接OpenLDAP
	    private static void LDAP_connect() {  

	        Hashtable<String, String> env = new Hashtable<String, String>();  
	        env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);  
	        env.put(Context.PROVIDER_URL, URL + BASEDN);  
	        env.put(Context.SECURITY_AUTHENTICATION, "simple");  
	            
	       // String root = "cn=Manager,dc=dtchain,dc=io";  //根据自己情况修改  
	        env.put(Context.SECURITY_PRINCIPAL, root);   // 管理员  
	        env.put(Context.SECURITY_CREDENTIALS, pwd);  // 管理员密码  
	           
	        try {  
	            ctx = new InitialLdapContext(env, connCtls);  
	            System.out.println( "连接成功" );   
	               
	        } catch (javax.naming.AuthenticationException e) {  
	            System.out.println("连接失败：");  
	            e.printStackTrace();  
	        } catch (Exception e) {  
	            System.out.println("连接出错：");  
	            e.printStackTrace();  
	        }  
	           
	    }  
	  private void closeContext(){  
	    if (ctx != null) {  
	    try {  
	        ctx.close();  
	    }  
	    catch (NamingException e) {  
	        e.printStackTrace();  
	    }  
	  
	}  
	  }  
	  //验证用户是否存在OpenLDAP
	    public  boolean getLoginContext(String account,String password) {
	    	LDAP_connect();
			for (int i = 0; i < 5; i++) { // 验证次数
				 Hashtable<String, String> env = new Hashtable<String, String>(); 
				env.put(Context.SECURITY_AUTHENTICATION, "simple");
			
				 String root1 = "cn="+account+","+BASEDN;  //根据自己情况修改  
			        env.put(Context.SECURITY_PRINCIPAL, root1);   // 管理员  
			        env.put(Context.SECURITY_CREDENTIALS, password);  // 管理员密码  
				env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
				env.put(Context.PROVIDER_URL, URL+BASEDN);
				try {
					// 连接LDAP进行认证
					ctx = new InitialDirContext(env);
				
					System.out.println("认证成功");
					return true;
				} catch (javax.naming.AuthenticationException e) {
					System.out.println("认证失败");
					return false;
				} catch (NamingException err) {
					System.out.println("--------->>【" + account + "】用户验证失败【" + i + "】次");
				} catch (Exception e) {
					System.out.println("认证出错：");
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
	   //添加用户
	    public static boolean addUserLdap(String account, String password) {
			//boolean success = false;
			try {
				LDAP_connect();
				//ctx = LdapUtil.getLoginContext();
				BasicAttributes attrsbu = new BasicAttributes();
				BasicAttribute objclassSet = new BasicAttribute("objectclass");
				objclassSet.add("person");
				objclassSet.add("top");
				objclassSet.add("organizationalPerson");
				objclassSet.add("inetOrgPerson");
				attrsbu.put(objclassSet);
				attrsbu.put("sn", account);
				attrsbu.put("uid", account);
				attrsbu.put("userPassword", password);
			
				ctx.createSubcontext("cn=" + account , attrsbu);
				ctx.close();
				return true;
			} catch (NamingException ex) {
				try {
					if (ctx != null) {
						ctx.close();
					}
				} catch (NamingException namingException) {
					namingException.printStackTrace();
				}
				System.out.println("--------->>添加用户失败");
			}
			return false;
		}
	 // 修改密码
		public  boolean updatePasswordLdap(String account, String password) {
			boolean success = false;
			LDAP_connect();
			try {
				 LDAPAttribute attributePassword = new LDAPAttribute( "userPassword", password);
				ModificationItem[] modificationItem = new ModificationItem[1];
				modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", password));
				ctx.modifyAttributes("cn=" + account , modificationItem);
				ctx.close();
				return true;
			} catch (NamingException ex) {
				try {
					if (ctx != null) {
						ctx.close();
					}
				} catch (NamingException namingException) {
					namingException.printStackTrace();
				}
				ex.printStackTrace();
				System.out.println("--------->>修改密码失败");
			}
			return success;
		}
		public static void main(String[] args){
			
			//System.out.println(Ldap.addUserLdap("hu", "123456"));
			Ldap l=new Ldap();
			System.out.println(l.updatePasswordLdap("hu", "3333"));
			
		}
}  


