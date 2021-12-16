# zzhSpringboot

#### 介绍
通用框架
springboot+maven+mybatis-plus+mysql+redis+swagger2

mysql存储ip的方式：
SELECT INET_ATON('127.0.0.1')
SELECT INET_NTOA(2130706433)

20211215 使用国密SM2+redis添加自定义token进行登录校验，权限管理基本完成
1、自定义登录，CustomizeAuthenticationSuccessHandler不需要了，只用在自己的登录接口中处理登录成功后的逻辑
2、WebSecurityConfig中http.addFilterBefore，将自定义过滤器替换原本的登录成功
3、不需要校验登录token的API请求直接在yml中permit里添加，否则每次请求都走解密校验会有性能损耗
4、yml中permit里配置了API白名单后，数据库中权限里不要再配该API，否则权限过滤会被拦截，因为没有登录

20211204  springboot+security+maven+mybatis-plus+mysql+swagger2已完成，下一步使用redis+RSA添加自定义token进行登录校验
初步登录+校验已经完成，记录一下需要注意的几点地方：
1、WebSecurityConfig中要加.and().csrf().disable()，否则post请求会失败，因为默认开启了csrf跨域拦截
2、security中登录校验密码是password，注意小写。我是自定义表结构pass_word，驼式转换后是passWord，所以在自定义UserDetail类中返回password的时候return user.getPassWord()
3、自定义表中没有账号过期、锁定等相关的配置，所以UserDetail中的isAccountNonExpired()、isAccountNonLocked()、isCredentialsNonExpired()、isEnabled()全部改为true。如果后期需要添加锁定相关的话，可以直接return 条件。


