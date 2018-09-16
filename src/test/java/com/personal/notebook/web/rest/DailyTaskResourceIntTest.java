package com.personal.notebook.web.rest;

import com.personal.notebook.PersonalNotebookApp;

import com.personal.notebook.domain.DailyTask;
import com.personal.notebook.repository.DailyTaskRepository;
import com.personal.notebook.repository.search.DailyTaskSearchRepository;
import com.personal.notebook.service.DailyTaskService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.personal.notebook.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DailyTaskResource REST controller.
 *
 * @see DailyTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersonalNotebookApp.class)
@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"}, password = "admin")
public class DailyTaskResourceIntTest {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TASK = "AAAAAAAAAA";
    private static final String UPDATED_TASK = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Autowired
    private DailyTaskRepository dailyTaskRepository;

    

    @Autowired
    private DailyTaskService dailyTaskService;

    /**
     * This repository is mocked in the com.personal.notebook.repository.search test package.
     *
     * @see com.personal.notebook.repository.search.DailyTaskSearchRepositoryMockConfiguration
     */
    @Autowired
    private DailyTaskSearchRepository mockDailyTaskSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDailyTaskMockMvc;

    private DailyTask dailyTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DailyTaskResource dailyTaskResource = new DailyTaskResource(dailyTaskService);
        this.restDailyTaskMockMvc = MockMvcBuilders.standaloneSetup(dailyTaskResource)
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
    public static DailyTask createEntity(EntityManager em) {
        DailyTask dailyTask = new DailyTask()
            .date(DEFAULT_DATE)
            .task(DEFAULT_TASK)
            .description(DEFAULT_DESCRIPTION)
            .completed(DEFAULT_COMPLETED);
        return dailyTask;
    }

    @Before
    public void initTest() {
        dailyTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createDailyTask() throws Exception {
        int databaseSizeBeforeCreate = dailyTaskRepository.findAll().size();

        // Create the DailyTask
        restDailyTaskMockMvc.perform(post("/api/daily-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dailyTask)))
            .andExpect(status().isCreated());

        // Validate the DailyTask in the database
        List<DailyTask> dailyTaskList = dailyTaskRepository.findAll();
        assertThat(dailyTaskList).hasSize(databaseSizeBeforeCreate + 1);
        DailyTask testDailyTask = dailyTaskList.get(dailyTaskList.size() - 1);
        assertThat(testDailyTask.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDailyTask.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testDailyTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDailyTask.isCompleted()).isEqualTo(DEFAULT_COMPLETED);

        // Validate the DailyTask in Elasticsearch
        verify(mockDailyTaskSearchRepository, times(1)).save(testDailyTask);
    }

    @Test
    @Transactional
    public void createDailyTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dailyTaskRepository.findAll().size();

        // Create the DailyTask with an existing ID
        dailyTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDailyTaskMockMvc.perform(post("/api/daily-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dailyTask)))
            .andExpect(status().isBadRequest());

        // Validate the DailyTask in the database
        List<DailyTask> dailyTaskList = dailyTaskRepository.findAll();
        assertThat(dailyTaskList).hasSize(databaseSizeBeforeCreate);

        // Validate the DailyTask in Elasticsearch
        verify(mockDailyTaskSearchRepository, times(0)).save(dailyTask);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dailyTaskRepository.findAll().size();
        // set the field null
        dailyTask.setDate(null);

        // Create the DailyTask, which fails.

        restDailyTaskMockMvc.perform(post("/api/daily-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dailyTask)))
            .andExpect(status().isBadRequest());

        List<DailyTask> dailyTaskList = dailyTaskRepository.findAll();
        assertThat(dailyTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaskIsRequired() throws Exception {
        int databaseSizeBeforeTest = dailyTaskRepository.findAll().size();
        // set the field null
        dailyTask.setTask(null);

        // Create the DailyTask, which fails.

        restDailyTaskMockMvc.perform(post("/api/daily-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dailyTask)))
            .andExpect(status().isBadRequest());

        List<DailyTask> dailyTaskList = dailyTaskRepository.findAll();
        assertThat(dailyTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDailyTasks() throws Exception {
        // Initialize the database
        dailyTaskRepository.saveAndFlush(dailyTask);

        // Get all the dailyTaskList
        restDailyTaskMockMvc.perform(get("/api/daily-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getDailyTask() throws Exception {
        // Initialize the database
        dailyTaskRepository.saveAndFlush(dailyTask);

        // Get the dailyTask
        restDailyTaskMockMvc.perform(get("/api/daily-tasks/{id}", dailyTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dailyTask.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.task").value(DEFAULT_TASK.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingDailyTask() throws Exception {
        // Get the dailyTask
        restDailyTaskMockMvc.perform(get("/api/daily-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDailyTask() throws Exception {
        // Initialize the database
        dailyTaskService.save(dailyTask);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDailyTaskSearchRepository);

        int databaseSizeBeforeUpdate = dailyTaskRepository.findAll().size();

        // Update the dailyTask
        DailyTask updatedDailyTask = dailyTaskRepository.findById(dailyTask.getId()).get();
        // Disconnect from session so that the updates on updatedDailyTask are not directly saved in db
        em.detach(updatedDailyTask);
        updatedDailyTask
            .date(UPDATED_DATE)
            .task(UPDATED_TASK)
            .description(UPDATED_DESCRIPTION)
            .completed(UPDATED_COMPLETED);

        restDailyTaskMockMvc.perform(put("/api/daily-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDailyTask)))
            .andExpect(status().isOk());

        // Validate the DailyTask in the database
        List<DailyTask> dailyTaskList = dailyTaskRepository.findAll();
        assertThat(dailyTaskList).hasSize(databaseSizeBeforeUpdate);
        DailyTask testDailyTask = dailyTaskList.get(dailyTaskList.size() - 1);
        assertThat(testDailyTask.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDailyTask.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testDailyTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDailyTask.isCompleted()).isEqualTo(UPDATED_COMPLETED);

        // Validate the DailyTask in Elasticsearch
        verify(mockDailyTaskSearchRepository, times(1)).save(testDailyTask);
    }

    @Test
    @Transactional
    public void updateNonExistingDailyTask() throws Exception {
        int databaseSizeBeforeUpdate = dailyTaskRepository.findAll().size();

        // Create the DailyTask

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restDailyTaskMockMvc.perform(put("/api/daily-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dailyTask)))
            .andExpect(status().isBadRequest());

        // Validate the DailyTask in the database
        List<DailyTask> dailyTaskList = dailyTaskRepository.findAll();
        assertThat(dailyTaskList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DailyTask in Elasticsearch
        verify(mockDailyTaskSearchRepository, times(0)).save(dailyTask);
    }

    @Test
    @Transactional
    public void deleteDailyTask() throws Exception {
        // Initialize the database
        dailyTaskService.save(dailyTask);

        int databaseSizeBeforeDelete = dailyTaskRepository.findAll().size();

        // Get the dailyTask
        restDailyTaskMockMvc.perform(delete("/api/daily-tasks/{id}", dailyTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DailyTask> dailyTaskList = dailyTaskRepository.findAll();
        assertThat(dailyTaskList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DailyTask in Elasticsearch
        verify(mockDailyTaskSearchRepository, times(1)).deleteById(dailyTask.getId());
    }

    @Test
    @Transactional
    public void searchDailyTask() throws Exception {
        // Initialize the database
        dailyTaskService.save(dailyTask);
        when(mockDailyTaskSearchRepository.search(queryStringQuery("id:" + dailyTask.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dailyTask), PageRequest.of(0, 1), 1));
        // Search the dailyTask
        restDailyTaskMockMvc.perform(get("/api/_search/daily-tasks?query=id:" + dailyTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyTask.class);
        DailyTask dailyTask1 = new DailyTask();
        dailyTask1.setId(1L);
        DailyTask dailyTask2 = new DailyTask();
        dailyTask2.setId(dailyTask1.getId());
        assertThat(dailyTask1).isEqualTo(dailyTask2);
        dailyTask2.setId(2L);
        assertThat(dailyTask1).isNotEqualTo(dailyTask2);
        dailyTask1.setId(null);
        assertThat(dailyTask1).isNotEqualTo(dailyTask2);
    }
}
