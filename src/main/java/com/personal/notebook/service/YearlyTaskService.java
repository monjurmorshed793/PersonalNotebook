package com.personal.notebook.service;

import com.personal.notebook.domain.YearlyTask;
import com.personal.notebook.repository.UserRepository;
import com.personal.notebook.repository.YearlyTaskRepository;
import com.personal.notebook.repository.search.YearlyTaskSearchRepository;
import com.personal.notebook.security.AuthoritiesConstants;
import com.personal.notebook.security.SecurityUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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

    private final UserRepository userRepository;


    public YearlyTaskService(YearlyTaskRepository yearlyTaskRepository, YearlyTaskSearchRepository yearlyTaskSearchRepository, UserRepository userRepository) {
        this.yearlyTaskRepository = yearlyTaskRepository;
        this.yearlyTaskSearchRepository = yearlyTaskSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a yearlyTask.
     *
     * @param yearlyTask the entity to save
     * @return the persisted entity
     */
    public YearlyTask save(YearlyTask yearlyTask) {
        yearlyTask.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get());
        log.debug("Request to save YearlyTask : {}", yearlyTask);
        YearlyTask result = yearlyTaskRepository.save(yearlyTask);
        yearlyTaskSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the yearlyTasks (user will get user specific).
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<YearlyTask> findAll(Pageable pageable) {
        log.debug("Request to get all YearlyTasks");
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            return yearlyTaskRepository.findAll(pageable);
        else
            return yearlyTaskRepository.findByUserIsCurrentUser(pageable);
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
        QueryBuilder queryBuilder = new NativeSearchQueryBuilder()
            .withQuery(matchPhrasePrefixQuery("task",query).slop(1))
            .withQuery(matchPhraseQuery("description", query).slop(1))
            .withQuery(matchPhraseQuery("user.login", SecurityUtils.getCurrentUserLogin().get()))
            .build().getQuery();
        return yearlyTaskSearchRepository.search(queryBuilder, pageable);    }
}
