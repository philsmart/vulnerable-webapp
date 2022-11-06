package uk.ac.jisc.cybersec.rce;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

/**
 * Simple ldap repository facade.
 */
@Service
public class SimpleLdapRepository {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleLdapRepository.class);

	@Autowired
	private LdapTemplate ldapTemplate;
	
	@PostConstruct
	public void debuggingInit() {
		getAllJavaNamingReferences().forEach(p -> log.info("Ldap result: {}",p));
	}

	/**
	 * Retrieves all the javaNamingReference in the directory.
	 * 
	 * @return list of person names
	 */
	public List<String> getAllJavaNamingReferences() {
		return ldapTemplate.search(query().where("objectclass").is("javaNamingReference"), new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("cn").get();
			}
		});
	}

}
