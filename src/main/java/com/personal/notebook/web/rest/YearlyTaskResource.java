package com.personal.notebook.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.personal.notebook.domain.YearlyTask;
import com.personal.notebook.service.YearlyTaskService;
import com.personal.notebook.web.rest.errors.BadRequestAlertException;
import com.personal.notebook.web.rest.util.HeaderUtil;
import com.personal.notebook.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing YearlyTask.
 */
@RestController
@RequestMapping("/api")
public class YearlyTaskResource {

    private final Logger log = LoggerFactory.getLogger(YearlyTaskResource.class);

    private static final String ENTITY_NAME = "yearlyTask";

    private final YearlyTaskService yearlyTaskService;

    public YearlyTaskResource(YearlyTaskService yearlyTaskService) {
        this.yearlyTaskService = yearlyTaskService;
    }

    /**
     * POST  /yearly-tasks : Create a new yearlyTask.
     *
     * @param yearlyTask the yearlyTask to create
     * @return the ResponseEntity with status 201 (Created) and with body the new yearlyTask, or with status 400 (Bad Request) if the yearlyTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/yearly-tasks")
    @Timed
    public ResponseEntity<YearlyTask> createYearlyTask(@Valid @RequestBody YearlyTask yearlyTask) throws URISyntaxException {
        log.debug("REST request to save YearlyTask : {}", yearlyTask);
        if (yearlyTask.getId() != null) {
            throw new BadRequestAlertException("A new yearlyTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        YearlyTask result = yearlyTaskService.save(yearlyTask);
        return ResponseEntity.created(new URI("/api/yearly-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /yearly-tasks : Updates an existing yearlyTask.
     *
     * @param yearlyTask the yearlyTask to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated yearlyTask,
     * or with status 400 (Bad Request) if the yearlyTask is not valid,
     * or with status 500 (Internal Server Error) if the yearlyTask couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/yearly-tasks")
    @Timed
    public ResponseEntity<YearlyTask> updateYearlyTask(@Valid @RequestBody YearlyTask yearlyTask) throws URISyntaxException {
        log.debug("REST request to update YearlyTask : {}", yearlyTask);
        if (yearlyTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        YearlyTask result = yearlyTaskService.save(yearlyTask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, yearlyTask.getId().toString()))
            .body(result);
    }

    /**
     * GET  /yearly-tasks : get all the yearlyTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of yearlyTasks in body
     */
    @GetMapping("/yearly-tasks")
    @Timed
    public ResponseEntity<List<YearlyTask>> getAllYearlyTasks(Pageable pageable) {
        log.debug("REST request to get a page of YearlyTasks");
        Page<YearlyTask> page = yearlyTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/yearly-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /yearly-tasks/:id : get the "id" yearlyTask.
     *
     * @param id the id of the yearlyTask to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the yearlyTask, or with status 404 (Not Found)
     */
    @GetMapping("/yearly-tasks/{id}")
    @Timed
    public ResponseEntity<YearlyTask> getYearlyTask(@PathVariable Long id) {
        log.debug("REST request to get YearlyTask : {}", id);
        Optional<YearlyTask> yearlyTask = yearlyTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(yearlyTask);
    }

    /**
     * DELETE  /yearly-tasks/:id : delete the "id" yearlyTask.
     *
     * @param id the id of the yearlyTask to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/yearly-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteYearlyTask(@PathVariable Long id) {
        log.debug("REST request to delete YearlyTask : {}", id);
        yearlyTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/yearly-tasks?query=:query : search for the yearlyTask corresponding
     * to the query.
     *
     * @param query the query of the yearlyTask search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/yearly-tasks")
    @Timed
    public ResponseEntity<List<YearlyTask>> searchYearlyTasks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of YearlyTasks for query {}", query);
        Page<YearlyTask> page = yearlyTaskService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/yearly-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
