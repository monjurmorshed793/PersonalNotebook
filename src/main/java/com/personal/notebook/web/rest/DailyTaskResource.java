package com.personal.notebook.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.personal.notebook.domain.DailyTask;
import com.personal.notebook.service.DailyTaskService;
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
import javax.websocket.server.PathParam;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DailyTask.
 */
@RestController
@RequestMapping("/api")
public class DailyTaskResource {

    private final Logger log = LoggerFactory.getLogger(DailyTaskResource.class);

    private static final String ENTITY_NAME = "dailyTask";

    private final DailyTaskService dailyTaskService;

    public DailyTaskResource(DailyTaskService dailyTaskService) {
        this.dailyTaskService = dailyTaskService;
    }

    /**
     * POST  /daily-tasks : Create a new dailyTask.
     *
     * @param dailyTask the dailyTask to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dailyTask, or with status 400 (Bad Request) if the dailyTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/daily-tasks")
    @Timed
    public ResponseEntity<DailyTask> createDailyTask(@Valid @RequestBody DailyTask dailyTask) throws URISyntaxException {
        log.debug("REST request to save DailyTask : {}", dailyTask);
        if (dailyTask.getId() != null) {
            throw new BadRequestAlertException("A new dailyTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DailyTask result = dailyTaskService.save(dailyTask);
        return ResponseEntity.created(new URI("/api/daily-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /daily-tasks : Updates an existing dailyTask.
     *
     * @param dailyTask the dailyTask to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dailyTask,
     * or with status 400 (Bad Request) if the dailyTask is not valid,
     * or with status 500 (Internal Server Error) if the dailyTask couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/daily-tasks")
    @Timed
    public ResponseEntity<DailyTask> updateDailyTask(@Valid @RequestBody DailyTask dailyTask) throws URISyntaxException {
        log.debug("REST request to update DailyTask : {}", dailyTask);
        if (dailyTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DailyTask result = dailyTaskService.save(dailyTask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dailyTask.getId().toString()))
            .body(result);
    }

    /**
     * GET  /daily-tasks : get all the dailyTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dailyTasks in body
     */
    @GetMapping("/daily-tasks")
    @Timed
    public ResponseEntity<List<DailyTask>> getAllDailyTasks(Pageable pageable) {
        log.debug("REST request to get a page of DailyTasks");
        Page<DailyTask> page = dailyTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/daily-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /daily-tasks: get all the dailyTasks by date.
     * @param date the date
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dailyTasks in body
     */
    @GetMapping("/daily-tasks/date/{date}")
    @Timed
    public ResponseEntity<List<DailyTask>> getDailyTasksByDate(@PathVariable("date") String date, Pageable pageable){
        log.debug("REST request to get a page of DailyTasks by date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate convertedDate = LocalDate.parse(date, formatter);
        Page<DailyTask> page = dailyTaskService.findAll(convertedDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/daily-task/date/"+date);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /daily-tasks/:id : get the "id" dailyTask.
     *
     * @param id the id of the dailyTask to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dailyTask, or with status 404 (Not Found)
     */
    @GetMapping("/daily-tasks/{id}")
    @Timed
    public ResponseEntity<DailyTask> getDailyTask(@PathVariable Long id) {
        log.debug("REST request to get DailyTask : {}", id);
        Optional<DailyTask> dailyTask = dailyTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dailyTask);
    }

    /**
     * DELETE  /daily-tasks/:id : delete the "id" dailyTask.
     *
     * @param id the id of the dailyTask to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/daily-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteDailyTask(@PathVariable Long id) {
        log.debug("REST request to delete DailyTask : {}", id);
        dailyTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/daily-tasks?query=:query : search for the dailyTask corresponding
     * to the query.
     *
     * @param query the query of the dailyTask search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/daily-tasks")
    @Timed
    public ResponseEntity<List<DailyTask>> searchDailyTasks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DailyTasks for query {}", query);
        Page<DailyTask> page = dailyTaskService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/daily-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
