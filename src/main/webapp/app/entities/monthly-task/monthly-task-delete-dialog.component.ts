import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMonthlyTask } from 'app/shared/model/monthly-task.model';
import { MonthlyTaskService } from './monthly-task.service';

@Component({
    selector: 'jhi-monthly-task-delete-dialog',
    templateUrl: './monthly-task-delete-dialog.component.html'
})
export class MonthlyTaskDeleteDialogComponent {
    monthlyTask: IMonthlyTask;

    constructor(
        private monthlyTaskService: MonthlyTaskService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.monthlyTaskService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'monthlyTaskListModification',
                content: 'Deleted an monthlyTask'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-monthly-task-delete-popup',
    template: ''
})
export class MonthlyTaskDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ monthlyTask }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MonthlyTaskDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.monthlyTask = monthlyTask;
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
