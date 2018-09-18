package com.personal.notebook.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.personal.notebook.domain.MonthlyTask;
import com.personal.notebook.domain.enumeration.MonthType;
import com.personal.notebook.service.MonthlyTaskService;
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
 * REST controller for managing MonthlyTask.
 */
@RestController
@RequestMapping("/api")
public class MonthlyTaskResource {

    private final Logger log = LoggerFactory.getLogger(MonthlyTaskResource.class);

    private static final String ENTITY_NAME = "monthlyTask";

    private final MonthlyTaskService monthlyTaskService;

    public MonthlyTaskResource(MonthlyTaskService monthlyTaskService) {
        this.monthlyTaskService = monthlyTaskService;
    }

    /**
     * POST  /monthly-tasks : Create a new monthlyTask.
     *
     * @param monthlyTask the monthlyTask to create
     * @return the ResponseEntity with status 201 (Created) and with body the new monthlyTask, or with status 400 (Bad Request) if the monthlyTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/monthly-tasks")
    @Timed
    public ResponseEntity<MonthlyTask> createMonthlyTask(@Valid @RequestBody MonthlyTask monthlyTask) throws URISyntaxException {
        log.debug("REST request to save MonthlyTask : {}", monthlyTask);
        if (monthlyTask.getId() != null) {
            throw new BadRequestAlertException("A new monthlyTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MonthlyTask result = monthlyTaskService.save(monthlyTask);
        return ResponseEntity.created(new URI("/api/monthly-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /monthly-tasks : Updates an existing monthlyTask.
     *
     * @param monthlyTask the monthlyTask to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated monthlyTask,
     * or with status 400 (Bad Request) if the monthlyTask is not valid,
     * or with status 500 (Internal Server Error) if the monthlyTask couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/monthly-tasks")
    @Timed
    public ResponseEntity<MonthlyTask> updateMonthlyTask(@Valid @RequestBody MonthlyTask monthlyTask) throws URISyntaxException {
        log.debug("REST request to update MonthlyTask : {}", monthlyTask);
        if (monthlyTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MonthlyTask result = monthlyTaskService.save(monthlyTask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, monthlyTask.getId().toString()))
            .body(result);
    }

    /**
     * GET  /monthly-tasks : get all the monthlyTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of monthlyTasks in body
     */
    @GetMapping("/monthly-tasks")
    @Timed
    public ResponseEntity<List<MonthlyTask>> getAllMonthlyTasks(Pageable pageable) {
        log.debug("REST request to get a page of MonthlyTasks");
        Page<MonthlyTask> page = monthlyTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/monthly-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /monthly-tasks : get all the monthlyTasks by month type
     * @param monthType the month type information
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of monthlyTasks in body
     */
    @GetMapping("/monthly-tasks/month-type/{month-type}")
    @Timed
    public ResponseEntity<List<MonthlyTask>> getAllMonthlyTaskByMonthType(@PathVariable("month-type") MonthType monthType, Pageable pageable){
        log.debug("REST request to get a page of month type related MonthlyTasks");
        Page<MonthlyTask> page = monthlyTaskService.findAll(monthType, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/monthly-tasks/month-type/"+monthType);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /monthly-tasks/:id : get the "id" monthlyTask.
     *
     * @param id the id of the monthlyTask to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the monthlyTask, or with status 404 (Not Found)
     */
    @GetMapping("/monthly-tasks/{id}")
    @Timed
    public ResponseEntity<MonthlyTask> getMonthlyTask(@PathVariable Long id) {
        log.debug("REST request to get MonthlyTask : {}", id);
        Optional<MonthlyTask> monthlyTask = monthlyTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monthlyTask);
    }

    /**
     * DELETE  /monthly-tasks/:id : delete the "id" monthlyTask.
     *
     * @param id the id of the monthlyTask to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/monthly-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteMonthlyTask(@PathVariable Long id) {
        log.debug("REST request to delete MonthlyTask : {}", id);
        monthlyTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/monthly-tasks?query=:query : search for the monthlyTask corresponding
     * to the query.
     *
     * @param query the query of the monthlyTask search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/monthly-tasks")
    @Timed
    public ResponseEntity<List<MonthlyTask>> searchMonthlyTasks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MonthlyTasks for query {}", query);
        Page<MonthlyTask> page = monthlyTaskService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/monthly-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
