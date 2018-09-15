package com.personal.notebook.service;

import com.personal.notebook.domain.MonthlyTask;
import com.personal.notebook.repository.MonthlyTaskRepository;
import com.personal.notebook.repository.search.MonthlyTaskSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MonthlyTask.
 */
@Service
@Transactional
public class MonthlyTaskService {

    private final Logger log = LoggerFactory.getLogger(MonthlyTaskService.class);

    private final MonthlyTaskRepository monthlyTaskRepository;

    private final MonthlyTaskSearchRepository monthlyTaskSearchRepository;

    public MonthlyTaskService(MonthlyTaskRepository monthlyTaskRepository, MonthlyTaskSearchRepository monthlyTaskSearchRepository) {
        this.monthlyTaskRepository = monthlyTaskRepository;
        this.monthlyTaskSearchRepository = monthlyTaskSearchRepository;
    }

    /**
     * Save a monthlyTask.
     *
     * @param monthlyTask the entity to save
     * @return the persisted entity
     */
    public MonthlyTask save(MonthlyTask monthlyTask) {
        log.debug("Request to save MonthlyTask : {}", monthlyTask);        MonthlyTask result = monthlyTaskRepository.save(monthlyTask);
        monthlyTaskSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the monthlyTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MonthlyTask> findAll(Pageable pageable) {
        log.debug("Request to get all MonthlyTasks");
        return monthlyTaskRepository.findAll(pageable);
    }


    /**
     * Get one monthlyTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MonthlyTask> findOne(Long id) {
        log.debug("Request to get MonthlyTask : {}", id);
        return monthlyTaskRepository.findById(id);
    }

    /**
     * Delete the monthlyTask by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MonthlyTask : {}", id);
        monthlyTaskRepository.deleteById(id);
        monthlyTaskSearchRepository.deleteById(id);
    }

    /**
     * Search for the monthlyTask corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MonthlyTask> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MonthlyTasks for query {}", query);
        return monthlyTaskSearchRepository.search(queryStringQuery(query), pageable);    }
}
