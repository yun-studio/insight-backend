package com.yunstudio.insight.global.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutRepository extends CrudRepository<Logout, String> {

}
