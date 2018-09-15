import { element, by, ElementFinder } from 'protractor';

export class MonthlyTaskComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-monthly-task div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getText();
    }
}

export class MonthlyTaskUpdatePage {
    pageTitle = element(by.id('jhi-monthly-task-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    monthTypeSelect = element(by.id('field_monthType'));
    taskInput = element(by.id('field_task'));
    descriptionInput = element(by.id('field_description'));
    completedInput = element(by.id('field_completed'));
    userSelect = element(by.id('field_user'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setMonthTypeSelect(monthType) {
        await this.monthTypeSelect.sendKeys(monthType);
    }

    async getMonthTypeSelect() {
        return this.monthTypeSelect.element(by.css('option:checked')).getText();
    }

    async monthTypeSelectLastOption() {
        await this.monthTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setTaskInput(task) {
        await this.taskInput.sendKeys(task);
    }

    async getTaskInput() {
        return this.taskInput.getAttribute('value');
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    getCompletedInput() {
        return this.completedInput;
    }

    async userSelectLastOption() {
        await this.userSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async userSelectOption(option) {
        await this.userSelect.sendKeys(option);
    }

    getUserSelect(): ElementFinder {
        return this.userSelect;
    }

    async getUserSelectedOption() {
        return this.userSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}
