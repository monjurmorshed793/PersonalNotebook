import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DailyTaskComponentsPage, DailyTaskUpdatePage } from './daily-task.page-object';

describe('DailyTask e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let dailyTaskUpdatePage: DailyTaskUpdatePage;
    let dailyTaskComponentsPage: DailyTaskComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load DailyTasks', async () => {
        await navBarPage.goToEntity('daily-task');
        dailyTaskComponentsPage = new DailyTaskComponentsPage();
        expect(await dailyTaskComponentsPage.getTitle()).toMatch(/Daily Tasks/);
    });

    it('should load create DailyTask page', async () => {
        await dailyTaskComponentsPage.clickOnCreateButton();
        dailyTaskUpdatePage = new DailyTaskUpdatePage();
        expect(await dailyTaskUpdatePage.getPageTitle()).toMatch(/Create or edit a Daily Task/);
        await dailyTaskUpdatePage.cancel();
    });

    it('should create and save DailyTasks', async () => {
        await dailyTaskComponentsPage.clickOnCreateButton();
        await dailyTaskUpdatePage.setDateInput('2000-12-31');
        expect(await dailyTaskUpdatePage.getDateInput()).toMatch('2000-12-31');
        await dailyTaskUpdatePage.setTaskInput('task');
        expect(await dailyTaskUpdatePage.getTaskInput()).toMatch('task');
        await dailyTaskUpdatePage.setDescriptionInput('description');
        expect(await dailyTaskUpdatePage.getDescriptionInput()).toMatch('description');
        const selectedCompleted = dailyTaskUpdatePage.getCompletedInput();
        if (await selectedCompleted.isSelected()) {
            await dailyTaskUpdatePage.getCompletedInput().click();
            expect(await dailyTaskUpdatePage.getCompletedInput().isSelected()).toBeFalsy();
        } else {
            await dailyTaskUpdatePage.getCompletedInput().click();
            expect(await dailyTaskUpdatePage.getCompletedInput().isSelected()).toBeTruthy();
        }
        await dailyTaskUpdatePage.userSelectLastOption();
        await dailyTaskUpdatePage.save();
        expect(await dailyTaskUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
