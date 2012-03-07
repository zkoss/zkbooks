package org.zkoss.zkspringessentials.acl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;
import org.zkoss.zkspringessentials.beans.Person;

/**
* The simplest possible implementation of AclService interface. Uses in-memory
* collection of ACLs, providing fast and easy access to them.
*
*/
public class InMemoryAclService implements AclService {

	Map<ObjectIdentity, Acl> acls = new HashMap<ObjectIdentity, Acl>();

	@PostConstruct
	public void initializeACLs() {
		// create ACLs according to requirements of tutorial application
//		ObjectIdentity user1 = new ObjectIdentityImpl(User.class, "rod");
//		ObjectIdentity user2 = new ObjectIdentityImpl(User.class, "dianne");
//		ObjectIdentity user3 = new ObjectIdentityImpl(User.class, "scott");
//		ObjectIdentity user4 = new ObjectIdentityImpl(User.class, "peter");

		final int OBJECT_ID_FOR_ACL_TEST =1;
		ObjectIdentity user1 = new ObjectIdentityImpl(Person.class, OBJECT_ID_FOR_ACL_TEST);
		Acl acl1 = new SimpleAclImpl(user1, new LinkedList<AccessControlEntry>());
		acl1.getEntries().add(new AccessControlEntryImpl("ace1", acl1, new PrincipalSid("rod"), BasePermission.ADMINISTRATION, true, true, true));
		
		acl1.getEntries().add(new AccessControlEntryImpl("ace2", acl1, new PrincipalSid("dianne"), BasePermission.CREATE, true, true, true));
		
		acl1.getEntries().add(new AccessControlEntryImpl("ace3", acl1, new PrincipalSid("peter"), BasePermission.WRITE, true, true, true));
		
		acl1.getEntries().add(new AccessControlEntryImpl("ace4", acl1, new PrincipalSid("scott"), BasePermission.READ, true, true, true));

		acls.put(acl1.getObjectIdentity(), acl1);
		
//		Acl acl1 = new SimpleAclImpl(user1, new LinkedList<AccessControlEntry>());
//		acl1.getEntries().add(new AccessControlEntryImpl("ace1", acl1, new PrincipalSid("manager1"), BasePermission.READ, true, true, true));
//		acls.put(acl1.getObjectIdentity(), acl1);
//		Acl acl2 = new SimpleAclImpl(user2, new LinkedList<AccessControlEntry>());
//		acl2.getEntries().add(new AccessControlEntryImpl("ace2", acl2, new PrincipalSid("manager1"), BasePermission.READ, true, true, true));
//		acls.put(acl2.getObjectIdentity(), acl2);
//		Acl acl3 = new SimpleAclImpl(user3, new LinkedList<AccessControlEntry>());
//		acl3.getEntries().add(new AccessControlEntryImpl("ace3", acl3, new PrincipalSid("manager2"), BasePermission.READ, true, true, true));
//		acls.put(acl3.getObjectIdentity(), acl3);
//		Acl acl4 = new SimpleAclImpl(user4, new LinkedList<AccessControlEntry>());
//		acl4.getEntries().add(new AccessControlEntryImpl("ace4", acl4, new PrincipalSid("manager2"), BasePermission.READ, true, true, true));
//		acls.put(acl4.getObjectIdentity(), acl4);
		
		
	}

	public List<ObjectIdentity> findChildren(ObjectIdentity parentIdentity) {
		// I'm not really sure what this method should do...
		throw new UnsupportedOperationException("Not implemented");
	}

	public Acl readAclById(ObjectIdentity object, List<Sid> sids) throws NotFoundException {
		List<ObjectIdentity> paramList = new LinkedList<ObjectIdentity>();
		paramList.add(object);
		Map<ObjectIdentity, Acl> map = readAclsById(paramList, sids);
		Assert.isTrue(map.containsKey(object), "There should have been an Acl entry for ObjectIdentity " + object);

		return map.get(object);
	}

	public Acl readAclById(ObjectIdentity object) throws NotFoundException {
		return readAclById(object, null);
	}

	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects) throws NotFoundException {
		return readAclsById(objects, null);
	}

	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException {
		Map<ObjectIdentity, Acl> result = new HashMap<ObjectIdentity, Acl>();

		for (ObjectIdentity object : objects) {
			if (acls.containsKey(object)) {
				result.put(object, acls.get(object));
			} else {
				throw new NotFoundException("Unable to find ACL information for object identity '" + object.toString() + "'");
			}
		}

		return result;
	}

}
