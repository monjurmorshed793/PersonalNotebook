package com.personal.notebook.repository;

import com.personal.notebook.domain.YearlyTask;
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
    List<YearlyTask> findByUserIsCurrentUser();

}
