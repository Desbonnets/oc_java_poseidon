package org.oc.poseidon.repositories;

import org.oc.poseidon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

}
