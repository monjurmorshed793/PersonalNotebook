package com.personal.notebook.repository.search;

import com.personal.notebook.domain.MonthlyTask;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MonthlyTask entity.
 */
public interface MonthlyTaskSearchRepository extends ElasticsearchRepository<MonthlyTask, Long> {
}
