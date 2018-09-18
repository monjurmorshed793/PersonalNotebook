package com.personal.notebook.repository;

import com.personal.notebook.domain.YearlyTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the YearlyTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface YearlyTaskRepository extends JpaRepository<YearlyTask, Long> {

    @Query("select yearly_task from YearlyTask yearly_task where yearly_task.user.login = ?#{principal.username}")
    Page<YearlyTask> findByUserIsCurrentUser(Pageable pageable);

    Page<YearlyTask> findYearlyTaskByYear(int year, Pageable pageable);

    Page<YearlyTask> findYearlyTaskByYearAndUserLogin(int year, String login, Pageable pageable);

}
