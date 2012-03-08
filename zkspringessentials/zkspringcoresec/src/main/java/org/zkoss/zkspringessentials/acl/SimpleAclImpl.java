package org.zkoss.zkspringessentials.acl;

import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import java.util.List;

/**
 * Very simple implementation of Acl interface, based on
 * org.springframework.security.acls.domain.AclImpl source (mainly the isGranted
 * method code). This implementation neither use owner concept nor parent
 * concept.
 */
public class SimpleAclImpl implements Acl {

	private static final long serialVersionUID = 1L;
	private ObjectIdentity oi;
	private List<AccessControlEntry> aces;
	private transient AuditLogger auditLogger = new ConsoleAuditLogger();

	public SimpleAclImpl(ObjectIdentity oi, List<AccessControlEntry> aces) {
		this.oi = oi;
		this.aces = aces;
	}

	public List<AccessControlEntry> getEntries() {
		return aces;
	}

	public ObjectIdentity getObjectIdentity() {
		return oi;
	}

	public Sid getOwner() {
		return null; // owner concept is optional, we don't use it
	}

	public Acl getParentAcl() {
		return null; // we don't use inheritance
	}

	public boolean isEntriesInheriting() {
		return false; // we don't use inheritance
	}

	public boolean isGranted(List<Permission> permission, List<Sid> sids, boolean administrativeMode) throws NotFoundException {

		AccessControlEntry firstRejection = null;

		for (int i = 0; i < permission.size(); i++) {
			for (int x = 0; x < sids.size(); x++) {
				// Attempt to find exact match for this permission mask and SID
				boolean scanNextSid = true;

				for (int j = 0; j < aces.size(); j++) {
					AccessControlEntry ace = (AccessControlEntry) aces.get(j);

					if ((ace.getPermission().getMask() == permission.get(i).getMask()) && ace.getSid().equals(sids.get(x))) {
						// Found a matching ACE, so its authorization decision
						// will prevail
						if (ace.isGranting()) {
							// Success
							if (!administrativeMode) {
								auditLogger.logIfNeeded(true, ace);
							}

							return true;
						} else {
							// Failure for this permission, so stop search
							// We will see if they have a different permission
							// (this permission is 100% rejected for this SID)
							if (firstRejection == null) {
								// Store first rejection for auditing reasons
								firstRejection = ace;
							}

							scanNextSid = false; // helps break the loop

							break; // exit "aces" loop
						}
					}
				}

				if (!scanNextSid) {
					break; // exit SID for loop (now try next permission)
				}
			}
		}

		if (firstRejection != null) {
			// We found an ACE to reject the request at this point, as no
			// other ACEs were found that granted a different permission
			if (!administrativeMode) {
				auditLogger.logIfNeeded(false, firstRejection);
			}

			return false;
		}

		throw new NotFoundException("Unable to locate a matching ACE for passed permissions and SIDs");
	}

	public boolean isSidLoaded(List<Sid> sids) {
		// we use in-memory structure, not external DB, so all entries are
		// always loaded
		// (if I correctly understand meaning of this method)
		return true;
	}
}