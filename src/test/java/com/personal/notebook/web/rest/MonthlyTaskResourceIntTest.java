package com.personal.notebook.web.rest;

import com.personal.notebook.PersonalNotebookApp;

import com.personal.notebook.domain.MonthlyTask;
import com.personal.notebook.repository.MonthlyTaskRepository;
import com.personal.notebook.repository.search.MonthlyTaskSearchRepository;
import com.personal.notebook.service.MonthlyTaskService;
import com.personal.notebook.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.personal.notebook.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.personal.notebook.domain.enumeration.MonthType;
/**
 * Test class for the MonthlyTaskResource REST controller.
 *
 * @see MonthlyTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersonalNotebookApp.class)
public class MonthlyTaskResourceIntTest {

    private static final MonthType DEFAULT_MONTH_TYPE = MonthType.JANUARY;
    private static final MonthType UPDATED_MONTH_TYPE = MonthType.FEBRUARY;

    private static final String DEFAULT_TASK = "AAAAAAAAAA";
    private static final String UPDATED_TASK = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Autowired
    private MonthlyTaskRepository monthlyTaskRepository;

    

    @Autowired
    private MonthlyTaskService monthlyTaskService;

    /**
     * This repository is mocked in the com.personal.notebook.repository.search test package.
     *
     * @see com.personal.notebook.repository.search.MonthlyTaskSearchRepositoryMockConfiguration
     */
    @Autowired
    private MonthlyTaskSearchRepository mockMonthlyTaskSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMonthlyTaskMockMvc;

    private MonthlyTask monthlyTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MonthlyTaskResource monthlyTaskResource = new MonthlyTaskResource(monthlyTaskService);
        this.restMonthlyTaskMockMvc = MockMvcBuilders.standaloneSetup(monthlyTaskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthlyTask createEntity(EntityManager em) {
        MonthlyTask monthlyTask = new MonthlyTask()
            .monthType(DEFAULT_MONTH_TYPE)
            .task(DEFAULT_TASK)
            .description(DEFAULT_DESCRIPTION)
            .completed(DEFAULT_COMPLETED);
        return monthlyTask;
    }

    @Before
    public void initTest() {
        monthlyTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createMonthlyTask() throws Exception {
        int databaseSizeBeforeCreate = monthlyTaskRepository.findAll().size();

        // Create the MonthlyTask
        restMonthlyTaskMockMvc.perform(post("/api/monthly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monthlyTask)))
            .andExpect(status().isCreated());

        // Validate the MonthlyTask in the database
        List<MonthlyTask> monthlyTaskList = monthlyTaskRepository.findAll();
        assertThat(monthlyTaskList).hasSize(databaseSizeBeforeCreate + 1);
        MonthlyTask testMonthlyTask = monthlyTaskList.get(monthlyTaskList.size() - 1);
        assertThat(testMonthlyTask.getMonthType()).isEqualTo(DEFAULT_MONTH_TYPE);
        assertThat(testMonthlyTask.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testMonthlyTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMonthlyTask.isCompleted()).isEqualTo(DEFAULT_COMPLETED);

        // Validate the MonthlyTask in Elasticsearch
        verify(mockMonthlyTaskSearchRepository, times(1)).save(testMonthlyTask);
    }

    @Test
    @Transactional
    public void createMonthlyTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = monthlyTaskRepository.findAll().size();

        // Create the MonthlyTask with an existing ID
        monthlyTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonthlyTaskMockMvc.perform(post("/api/monthly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monthlyTask)))
            .andExpect(status().isBadRequest());

        // Validate the MonthlyTask in the database
        List<MonthlyTask> monthlyTaskList = monthlyTaskRepository.findAll();
        assertThat(monthlyTaskList).hasSize(databaseSizeBeforeCreate);

        // Validate the MonthlyTask in Elasticsearch
        verify(mockMonthlyTaskSearchRepository, times(0)).save(monthlyTask);
    }

    @Test
    @Transactional
    public void checkTaskIsRequired() throws Exception {
        int databaseSizeBeforeTest = monthlyTaskRepository.findAll().size();
        // set the field null
        monthlyTask.setTask(null);

        // Create the MonthlyTask, which fails.

        restMonthlyTaskMockMvc.perform(post("/api/monthly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monthlyTask)))
            .andExpect(status().isBadRequest());

        List<MonthlyTask> monthlyTaskList = monthlyTaskRepository.findAll();
        assertThat(monthlyTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMonthlyTasks() throws Exception {
        // Initialize the database
        monthlyTaskRepository.saveAndFlush(monthlyTask);

        // Get all the monthlyTaskList
        restMonthlyTaskMockMvc.perform(get("/api/monthly-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthType").value(hasItem(DEFAULT_MONTH_TYPE.toString())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getMonthlyTask() throws Exception {
        // Initialize the database
        monthlyTaskRepository.saveAndFlush(monthlyTask);

        // Get the monthlyTask
        restMonthlyTaskMockMvc.perform(get("/api/monthly-tasks/{id}", monthlyTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(monthlyTask.getId().intValue()))
            .andExpect(jsonPath("$.monthType").value(DEFAULT_MONTH_TYPE.toString()))
            .andExpect(jsonPath("$.task").value(DEFAULT_TASK.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingMonthlyTask() throws Exception {
        // Get the monthlyTask
        restMonthlyTaskMockMvc.perform(get("/api/monthly-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMonthlyTask() throws Exception {
        // Initialize the database
        monthlyTaskService.save(monthlyTask);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockMonthlyTaskSearchRepository);

        int databaseSizeBeforeUpdate = monthlyTaskRepository.findAll().size();

        // Update the monthlyTask
        MonthlyTask updatedMonthlyTask = monthlyTaskRepository.findById(monthlyTask.getId()).get();
        // Disconnect from session so that the updates on updatedMonthlyTask are not directly saved in db
        em.detach(updatedMonthlyTask);
        updatedMonthlyTask
            .monthType(UPDATED_MONTH_TYPE)
            .task(UPDATED_TASK)
            .description(UPDATED_DESCRIPTION)
            .completed(UPDATED_COMPLETED);

        restMonthlyTaskMockMvc.perform(put("/api/monthly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMonthlyTask)))
            .andExpect(status().isOk());

        // Validate the MonthlyTask in the database
        List<MonthlyTask> monthlyTaskList = monthlyTaskRepository.findAll();
        assertThat(monthlyTaskList).hasSize(databaseSizeBeforeUpdate);
        MonthlyTask testMonthlyTask = monthlyTaskList.get(monthlyTaskList.size() - 1);
        assertThat(testMonthlyTask.getMonthType()).isEqualTo(UPDATED_MONTH_TYPE);
        assertThat(testMonthlyTask.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testMonthlyTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMonthlyTask.isCompleted()).isEqualTo(UPDATED_COMPLETED);

        // Validate the MonthlyTask in Elasticsearch
        verify(mockMonthlyTaskSearchRepository, times(1)).save(testMonthlyTask);
    }

    @Test
    @Transactional
    public void updateNonExistingMonthlyTask() throws Exception {
        int databaseSizeBeforeUpdate = monthlyTaskRepository.findAll().size();

        // Create the MonthlyTask

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restMonthlyTaskMockMvc.perform(put("/api/monthly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monthlyTask)))
            .andExpect(status().isBadRequest());

        // Validate the MonthlyTask in the database
        List<MonthlyTask> monthlyTaskList = monthlyTaskRepository.findAll();
        assertThat(monthlyTaskList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MonthlyTask in Elasticsearch
        verify(mockMonthlyTaskSearchRepository, times(0)).save(monthlyTask);
    }

    @Test
    @Transactional
    public void deleteMonthlyTask() throws Exception {
        // Initialize the database
        monthlyTaskService.save(monthlyTask);

        int databaseSizeBeforeDelete = monthlyTaskRepository.findAll().size();

        // Get the monthlyTask
        restMonthlyTaskMockMvc.perform(delete("/api/monthly-tasks/{id}", monthlyTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MonthlyTask> monthlyTaskList = monthlyTaskRepository.findAll();
        assertThat(monthlyTaskList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MonthlyTask in Elasticsearch
        verify(mockMonthlyTaskSearchRepository, times(1)).deleteById(monthlyTask.getId());
    }

    @Test
    @Transactional
    public void searchMonthlyTask() throws Exception {
        // Initialize the database
        monthlyTaskService.save(monthlyTask);
        when(mockMonthlyTaskSearchRepository.search(queryStringQuery("id:" + monthlyTask.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(monthlyTask), PageRequest.of(0, 1), 1));
        // Search the monthlyTask
        restMonthlyTaskMockMvc.perform(get("/api/_search/monthly-tasks?query=id:" + monthlyTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthType").value(hasItem(DEFAULT_MONTH_TYPE.toString())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonthlyTask.class);
        MonthlyTask monthlyTask1 = new MonthlyTask();
        monthlyTask1.setId(1L);
        MonthlyTask monthlyTask2 = new MonthlyTask();
        monthlyTask2.setId(monthlyTask1.getId());
        assertThat(monthlyTask1).isEqualTo(monthlyTask2);
        monthlyTask2.setId(2L);
        assertThat(monthlyTask1).isNotEqualTo(monthlyTask2);
        monthlyTask1.setId(null);
        assertThat(monthlyTask1).isNotEqualTo(monthlyTask2);
    }
}
