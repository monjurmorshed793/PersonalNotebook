import { element, by, ElementFinder } from 'protractor';

export class YearlyTaskComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-yearly-task div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async getTitle() {
        return this.title.getText();
    }
}

export class YearlyTaskUpdatePage {
    pageTitle = element(by.id('jhi-yearly-task-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    yearInput = element(by.id('field_year'));
    taskInput = element(by.id('field_task'));
    descriptionInput = element(by.id('field_description'));
    completedInput = element(by.id('field_completed'));
    userSelect = element(by.id('field_user'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setYearInput(year) {
        await this.yearInput.sendKeys(year);
    }

    async getYearInput() {
        return this.yearInput.getAttribute('value');
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
