# spring-security-csrf
spring-security+angularJs
- 大部分翻译自[spring security文档](http://docs.spring.io/spring-security/site/docs/4.2.0.BUILD-SNAPSHOT/reference/htmlsingle/)。
- 涉及到spring security的配置，只讲代码的配置方式，忽略xml配置方式。

要使用spring security的csrf protection，必须要以下三个步骤：

1. [使用合适的http方法](http://docs.spring.io/spring-security/site/docs/4.2.0.BUILD-SNAPSHOT/reference/htmlsingle/#csrf-use-proper-verbs)
2. [配置csrf protection](http://docs.spring.io/spring-security/site/docs/4.2.0.BUILD-SNAPSHOT/reference/htmlsingle/#csrf-configure)
3. [携带csrf token](http://docs.spring.io/spring-security/site/docs/4.2.0.BUILD-SNAPSHOT/reference/htmlsingle/#csrf-include-csrf-token)

## 使用合适的http方法

需要确保使用的是PATCH, POST, PUT, 或者DELETE方法。[使用POST替代GET传递敏感信息](https://www.w3.org/Protocols/rfc2616/rfc2616-sec15.html#sec15.1.3)，防止隐私信息泄漏。

## 配置csrf protection

一些框架通过验证用户的session处理无效的csrf token，这回导致[一些问题](http://docs.spring.io/spring-security/site/docs/4.2.0.BUILD-SNAPSHOT/reference/htmlsingle/#csrf-logout)。通过配置[AccessDeniedHandler](http://docs.spring.io/spring-security/site/docs/4.2.0.BUILD-SNAPSHOT/reference/htmlsingle/#access-denied-handler)使用不同方式处理不合法的CsrfTokenException，我们可以替换spring security crsf protection默认的http 403 access denied处理方式。

如果想要关闭默认打开的crsf protection，可以使用如下的java代码关闭：
```
@EnableWebSecurity
public class WebSecurityConfig extends
WebSecurityConfigurerAdapter {

@Override
protected void configure(HttpSecurity http) throws Exception {
	http
//关闭打开的csrf保护
	.csrf().disable();
}
}
```
## 携带csrf token
spring security官方文档中介绍了三种方式：
1. 表单提交中，使用jsp的标签可以访问后端服务器参数的方式，在表单提交中，可以轻易地将后端的csrf token，以参数名"_csrf"添加到表单参数中
2. 对于Ajax提交的JSON请求，是无法在请求参数中添加crsf token。面对这种情况，可以将crsf token添加到请求头之中 ，当然这还是需要jsp标签访问后端服务器参数
3. 而对于使用html+AngularJS 的同学，angularJs提供了一种[机制](https://docs.angularjs.org/api/ng/service/$http#cross-site-request-forgery-xsrf-protection)处理XSRF。当提交XHR requests时，$http 从cookie中读取参数名为XSRF-TOKEN的token，然后将它以X-XSRF-TOKEN为参数名设置到HTTP header。 由于只有域名内的js能够读取相应网站的cookie，服务器可以来自js的请求是在域名内的。而对于服务器端的spring security来说，CookieCsrfTokenRepository默认会将csrf token以名XSRF-TOKEN写入cookie
，然后接收请求时从名为X-XSRF-TOKEN的header或者名为_csrf的http请求参数读取csrf token。但是CookieCsrfTokenRepository写如的cookie默认具有cookieHttpOnly属性，前端js是不能操作它的，这就需要在spring security的配置中将 cookieHttpOnly属性设为false：
```
.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
```
除此之外，使用angularJs的同学就不用添加任何代码到前端或者后端了。
