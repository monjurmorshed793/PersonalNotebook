package com.personal.notebook.repository.search;

import com.personal.notebook.domain.YearlyTask;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the YearlyTask entity.
 */
public interface YearlyTaskSearchRepository extends ElasticsearchRepository<YearlyTask, Long> {
}
