package org.easy.ecm.directory.syn;

import java.util.List;

import org.easy.ecm.directory.domain.DirectoryUser;

public interface UserSynchronization {

	List<DirectoryUser> getUsers();
}
