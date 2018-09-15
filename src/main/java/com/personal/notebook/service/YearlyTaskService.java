package com.personal.notebook.service;

import com.personal.notebook.domain.YearlyTask;
import com.personal.notebook.repository.YearlyTaskRepository;
import com.personal.notebook.repository.search.YearlyTaskSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing YearlyTask.
 */
@Service
@Transactional
public class YearlyTaskService {

    private final Logger log = LoggerFactory.getLogger(YearlyTaskService.class);

    private final YearlyTaskRepository yearlyTaskRepository;

    private final YearlyTaskSearchRepository yearlyTaskSearchRepository;

    public YearlyTaskService(YearlyTaskRepository yearlyTaskRepository, YearlyTaskSearchRepository yearlyTaskSearchRepository) {
        this.yearlyTaskRepository = yearlyTaskRepository;
        this.yearlyTaskSearchRepository = yearlyTaskSearchRepository;
    }

    /**
     * Save a yearlyTask.
     *
     * @param yearlyTask the entity to save
     * @return the persisted entity
     */
    public YearlyTask save(YearlyTask yearlyTask) {
        log.debug("Request to save YearlyTask : {}", yearlyTask);        YearlyTask result = yearlyTaskRepository.save(yearlyTask);
        yearlyTaskSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the yearlyTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<YearlyTask> findAll(Pageable pageable) {
        log.debug("Request to get all YearlyTasks");
        return yearlyTaskRepository.findAll(pageable);
    }


    /**
     * Get one yearlyTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<YearlyTask> findOne(Long id) {
        log.debug("Request to get YearlyTask : {}", id);
        return yearlyTaskRepository.findById(id);
    }

    /**
     * Delete the yearlyTask by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete YearlyTask : {}", id);
        yearlyTaskRepository.deleteById(id);
        yearlyTaskSearchRepository.deleteById(id);
    }

    /**
     * Search for the yearlyTask corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<YearlyTask> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of YearlyTasks for query {}", query);
        return yearlyTaskSearchRepository.search(queryStringQuery(query), pageable);    }
}
