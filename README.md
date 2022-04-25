# WebAppWithSpringSecurityAndJwt
Backend of web application with Spring Security. Application checks if user with inserted credentials exist in database, gives him jwt token if authentication was successful, also checks jwt token from user while authorization.

To start the application you have to tune application.property file:

![Снимок экрана 2022-04-25 в 17 40 10](https://user-images.githubusercontent.com/102177550/165112384-ab215946-a599-40fc-815f-20301adbf80f.png)

Choose server port you want for app and write it as server.port value.

Also specify database properties like url, username and password.

jwt.token.secret value is used like secret word for creating a jwt tokens.

jwt.token.expired value is a validity time for jwt token in milliseconds.
