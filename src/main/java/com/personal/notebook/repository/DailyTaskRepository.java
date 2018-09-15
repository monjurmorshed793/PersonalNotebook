package com.personal.notebook.repository;

import com.personal.notebook.domain.DailyTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the DailyTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {

    @Query("select daily_task from DailyTask daily_task where daily_task.user.login = ?#{principal.username}")
    List<DailyTask> findByUserIsCurrentUser();

}
