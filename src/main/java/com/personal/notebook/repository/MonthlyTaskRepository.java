package com.personal.notebook.repository;

import com.personal.notebook.domain.MonthlyTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the MonthlyTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonthlyTaskRepository extends JpaRepository<MonthlyTask, Long> {

    @Query("select monthly_task from MonthlyTask monthly_task where monthly_task.user.login = ?#{principal.username}")
    Page<MonthlyTask> findByUserIsCurrentUser(Pageable pageable);

}
