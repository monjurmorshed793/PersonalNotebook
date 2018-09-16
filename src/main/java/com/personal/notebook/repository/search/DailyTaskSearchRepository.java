package com.personal.notebook.repository.search;

import com.personal.notebook.domain.DailyTask;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DailyTask entity.
 */
public interface DailyTaskSearchRepository extends ElasticsearchRepository<DailyTask, Long> {
}
