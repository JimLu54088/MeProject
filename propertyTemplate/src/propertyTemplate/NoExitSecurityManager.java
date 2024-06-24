package propertyTemplate;

public class NoExitSecurityManager extends SecurityManager {
	@Override
	public void checkPermission(java.security.Permission perm) {
		// allow anything
	}

	@Override
	public void checkPermission(java.security.Permission perm, Object context) {
		// allow anything
	}

	@Override
	public void checkExit(int status) {
		super.checkExit(status);
		throw new SecurityException("System.exit attempted with status: " + status);
	}
}
