package com.personal.notebook.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DailyTaskSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DailyTaskSearchRepositoryMockConfiguration {

    @MockBean
    private DailyTaskSearchRepository mockDailyTaskSearchRepository;

}
