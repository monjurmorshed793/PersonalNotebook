import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDailyTask } from 'app/shared/model/daily-task.model';
import { DailyTaskService } from './daily-task.service';

@Component({
    selector: 'jhi-daily-task-delete-dialog',
    templateUrl: './daily-task-delete-dialog.component.html'
})
export class DailyTaskDeleteDialogComponent {
    dailyTask: IDailyTask;

    constructor(private dailyTaskService: DailyTaskService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dailyTaskService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'dailyTaskListModification',
                content: 'Deleted an dailyTask'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-daily-task-delete-popup',
    template: ''
})
export class DailyTaskDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dailyTask }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DailyTaskDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.dailyTask = dailyTask;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
