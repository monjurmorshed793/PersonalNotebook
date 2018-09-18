package com.personal.notebook.repository;

import com.personal.notebook.domain.DailyTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the DailyTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {

    @Query("select daily_task from DailyTask daily_task where daily_task.user.login = ?#{principal.username}")
    Page<DailyTask> findByUserIsCurrentUser(Pageable pageable);

    Page<DailyTask> findDailyTaskByDate(LocalDate localDate, Pageable pageable);

    Page<DailyTask> findDailyTaskByDateAndUserLogin(LocalDate localDate, String login, Pageable pageable);

}
