package com.ays.backend.base;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(value = "test")
public abstract class BaseRepositoryTest {

}
