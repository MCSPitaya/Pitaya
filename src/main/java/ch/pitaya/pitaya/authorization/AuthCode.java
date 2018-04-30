package ch.pitaya.pitaya.authorization;

public enum AuthCode {

	// user & firm management
	USER_CREATE, USER_CHANGE_PASSWORD, FIRM_MODIFY, USER_CHANGE_ROLES,

	// case management
	CASE_READ, CASE_CREATE(CASE_READ), CASE_READ_FILES(CASE_READ),

	// file management
	FILE_READ, FILE_CREATE(FILE_READ), FILE_EDIT(FILE_READ),

	// profiles
	BASIC_USER(CASE_READ, FILE_READ),

	// admin profiles
	USER_ADMIN(USER_CREATE, USER_CHANGE_PASSWORD, FIRM_MODIFY, USER_CHANGE_ROLES), //
	CASE_ADMIN(CASE_CREATE), //
	FILE_ADMIN(FILE_CREATE, FILE_EDIT), //
	ADMIN(USER_ADMIN, CASE_ADMIN, FILE_ADMIN),;

	// enum internals
	public final AuthCode[] parents;

	AuthCode(AuthCode... parents) {
		this.parents = parents;
	}

}
