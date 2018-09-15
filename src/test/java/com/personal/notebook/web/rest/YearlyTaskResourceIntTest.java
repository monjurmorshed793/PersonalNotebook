package com.personal.notebook.web.rest;

import com.personal.notebook.PersonalNotebookApp;

import com.personal.notebook.domain.YearlyTask;
import com.personal.notebook.repository.YearlyTaskRepository;
import com.personal.notebook.repository.search.YearlyTaskSearchRepository;
import com.personal.notebook.service.YearlyTaskService;
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

/**
 * Test class for the YearlyTaskResource REST controller.
 *
 * @see YearlyTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersonalNotebookApp.class)
public class YearlyTaskResourceIntTest {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String DEFAULT_TASK = "AAAAAAAAAA";
    private static final String UPDATED_TASK = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Autowired
    private YearlyTaskRepository yearlyTaskRepository;

    

    @Autowired
    private YearlyTaskService yearlyTaskService;

    /**
     * This repository is mocked in the com.personal.notebook.repository.search test package.
     *
     * @see com.personal.notebook.repository.search.YearlyTaskSearchRepositoryMockConfiguration
     */
    @Autowired
    private YearlyTaskSearchRepository mockYearlyTaskSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restYearlyTaskMockMvc;

    private YearlyTask yearlyTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final YearlyTaskResource yearlyTaskResource = new YearlyTaskResource(yearlyTaskService);
        this.restYearlyTaskMockMvc = MockMvcBuilders.standaloneSetup(yearlyTaskResource)
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
    public static YearlyTask createEntity(EntityManager em) {
        YearlyTask yearlyTask = new YearlyTask()
            .year(DEFAULT_YEAR)
            .task(DEFAULT_TASK)
            .description(DEFAULT_DESCRIPTION)
            .completed(DEFAULT_COMPLETED);
        return yearlyTask;
    }

    @Before
    public void initTest() {
        yearlyTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createYearlyTask() throws Exception {
        int databaseSizeBeforeCreate = yearlyTaskRepository.findAll().size();

        // Create the YearlyTask
        restYearlyTaskMockMvc.perform(post("/api/yearly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(yearlyTask)))
            .andExpect(status().isCreated());

        // Validate the YearlyTask in the database
        List<YearlyTask> yearlyTaskList = yearlyTaskRepository.findAll();
        assertThat(yearlyTaskList).hasSize(databaseSizeBeforeCreate + 1);
        YearlyTask testYearlyTask = yearlyTaskList.get(yearlyTaskList.size() - 1);
        assertThat(testYearlyTask.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testYearlyTask.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testYearlyTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testYearlyTask.isCompleted()).isEqualTo(DEFAULT_COMPLETED);

        // Validate the YearlyTask in Elasticsearch
        verify(mockYearlyTaskSearchRepository, times(1)).save(testYearlyTask);
    }

    @Test
    @Transactional
    public void createYearlyTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = yearlyTaskRepository.findAll().size();

        // Create the YearlyTask with an existing ID
        yearlyTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restYearlyTaskMockMvc.perform(post("/api/yearly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(yearlyTask)))
            .andExpect(status().isBadRequest());

        // Validate the YearlyTask in the database
        List<YearlyTask> yearlyTaskList = yearlyTaskRepository.findAll();
        assertThat(yearlyTaskList).hasSize(databaseSizeBeforeCreate);

        // Validate the YearlyTask in Elasticsearch
        verify(mockYearlyTaskSearchRepository, times(0)).save(yearlyTask);
    }

    @Test
    @Transactional
    public void checkTaskIsRequired() throws Exception {
        int databaseSizeBeforeTest = yearlyTaskRepository.findAll().size();
        // set the field null
        yearlyTask.setTask(null);

        // Create the YearlyTask, which fails.

        restYearlyTaskMockMvc.perform(post("/api/yearly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(yearlyTask)))
            .andExpect(status().isBadRequest());

        List<YearlyTask> yearlyTaskList = yearlyTaskRepository.findAll();
        assertThat(yearlyTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllYearlyTasks() throws Exception {
        // Initialize the database
        yearlyTaskRepository.saveAndFlush(yearlyTask);

        // Get all the yearlyTaskList
        restYearlyTaskMockMvc.perform(get("/api/yearly-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(yearlyTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getYearlyTask() throws Exception {
        // Initialize the database
        yearlyTaskRepository.saveAndFlush(yearlyTask);

        // Get the yearlyTask
        restYearlyTaskMockMvc.perform(get("/api/yearly-tasks/{id}", yearlyTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(yearlyTask.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.task").value(DEFAULT_TASK.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingYearlyTask() throws Exception {
        // Get the yearlyTask
        restYearlyTaskMockMvc.perform(get("/api/yearly-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateYearlyTask() throws Exception {
        // Initialize the database
        yearlyTaskService.save(yearlyTask);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockYearlyTaskSearchRepository);

        int databaseSizeBeforeUpdate = yearlyTaskRepository.findAll().size();

        // Update the yearlyTask
        YearlyTask updatedYearlyTask = yearlyTaskRepository.findById(yearlyTask.getId()).get();
        // Disconnect from session so that the updates on updatedYearlyTask are not directly saved in db
        em.detach(updatedYearlyTask);
        updatedYearlyTask
            .year(UPDATED_YEAR)
            .task(UPDATED_TASK)
            .description(UPDATED_DESCRIPTION)
            .completed(UPDATED_COMPLETED);

        restYearlyTaskMockMvc.perform(put("/api/yearly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedYearlyTask)))
            .andExpect(status().isOk());

        // Validate the YearlyTask in the database
        List<YearlyTask> yearlyTaskList = yearlyTaskRepository.findAll();
        assertThat(yearlyTaskList).hasSize(databaseSizeBeforeUpdate);
        YearlyTask testYearlyTask = yearlyTaskList.get(yearlyTaskList.size() - 1);
        assertThat(testYearlyTask.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testYearlyTask.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testYearlyTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testYearlyTask.isCompleted()).isEqualTo(UPDATED_COMPLETED);

        // Validate the YearlyTask in Elasticsearch
        verify(mockYearlyTaskSearchRepository, times(1)).save(testYearlyTask);
    }

    @Test
    @Transactional
    public void updateNonExistingYearlyTask() throws Exception {
        int databaseSizeBeforeUpdate = yearlyTaskRepository.findAll().size();

        // Create the YearlyTask

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restYearlyTaskMockMvc.perform(put("/api/yearly-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(yearlyTask)))
            .andExpect(status().isBadRequest());

        // Validate the YearlyTask in the database
        List<YearlyTask> yearlyTaskList = yearlyTaskRepository.findAll();
        assertThat(yearlyTaskList).hasSize(databaseSizeBeforeUpdate);

        // Validate the YearlyTask in Elasticsearch
        verify(mockYearlyTaskSearchRepository, times(0)).save(yearlyTask);
    }

    @Test
    @Transactional
    public void deleteYearlyTask() throws Exception {
        // Initialize the database
        yearlyTaskService.save(yearlyTask);

        int databaseSizeBeforeDelete = yearlyTaskRepository.findAll().size();

        // Get the yearlyTask
        restYearlyTaskMockMvc.perform(delete("/api/yearly-tasks/{id}", yearlyTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<YearlyTask> yearlyTaskList = yearlyTaskRepository.findAll();
        assertThat(yearlyTaskList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the YearlyTask in Elasticsearch
        verify(mockYearlyTaskSearchRepository, times(1)).deleteById(yearlyTask.getId());
    }

    @Test
    @Transactional
    public void searchYearlyTask() throws Exception {
        // Initialize the database
        yearlyTaskService.save(yearlyTask);
        when(mockYearlyTaskSearchRepository.search(queryStringQuery("id:" + yearlyTask.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(yearlyTask), PageRequest.of(0, 1), 1));
        // Search the yearlyTask
        restYearlyTaskMockMvc.perform(get("/api/_search/yearly-tasks?query=id:" + yearlyTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(yearlyTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(YearlyTask.class);
        YearlyTask yearlyTask1 = new YearlyTask();
        yearlyTask1.setId(1L);
        YearlyTask yearlyTask2 = new YearlyTask();
        yearlyTask2.setId(yearlyTask1.getId());
        assertThat(yearlyTask1).isEqualTo(yearlyTask2);
        yearlyTask2.setId(2L);
        assertThat(yearlyTask1).isNotEqualTo(yearlyTask2);
        yearlyTask1.setId(null);
        assertThat(yearlyTask1).isNotEqualTo(yearlyTask2);
    }
}
