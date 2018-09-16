package com.personal.notebook.service;

import com.personal.notebook.domain.DailyTask;
import com.personal.notebook.repository.DailyTaskRepository;
import com.personal.notebook.repository.search.DailyTaskSearchRepository;
import com.personal.notebook.security.AuthoritiesConstants;
import com.personal.notebook.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.Principal;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DailyTask.
 */
@Service
@Transactional
public class DailyTaskService {

    private final Logger log = LoggerFactory.getLogger(DailyTaskService.class);

    private final DailyTaskRepository dailyTaskRepository;

    private final DailyTaskSearchRepository dailyTaskSearchRepository;

    public DailyTaskService(DailyTaskRepository dailyTaskRepository, DailyTaskSearchRepository dailyTaskSearchRepository) {
        this.dailyTaskRepository = dailyTaskRepository;
        this.dailyTaskSearchRepository = dailyTaskSearchRepository;
    }

    /**
     * Save a dailyTask.
     *
     * @param dailyTask the entity to save
     * @return the persisted entity
     */
    public DailyTask save(DailyTask dailyTask) {
        log.debug("Request to save DailyTask : {}", dailyTask);        DailyTask result = dailyTaskRepository.save(dailyTask);
        dailyTaskSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the dailyTasks (user will get user specific).
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DailyTask> findAll(Pageable pageable) {
        log.debug("Request to get all DailyTasks");
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            return dailyTaskRepository.findAll(pageable);
        else
            return dailyTaskRepository.findByUserIsCurrentUser(pageable);
    }


    /**
     * Get one dailyTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DailyTask> findOne(Long id) {
        log.debug("Request to get DailyTask : {}", id);
        return dailyTaskRepository.findById(id);
    }

    /**
     * Delete the dailyTask by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DailyTask : {}", id);
        dailyTaskRepository.deleteById(id);
        dailyTaskSearchRepository.deleteById(id);
    }

    /**
     * Search for the dailyTask corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DailyTask> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DailyTasks for query {}", query);
        return dailyTaskSearchRepository.search(queryStringQuery(query), pageable);    }
}
