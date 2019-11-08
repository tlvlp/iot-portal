package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.User;
import com.tlvlp.iot.server.portal.services.UserAdminException;
import com.tlvlp.iot.server.portal.services.UserAdminService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.util.List;

public class AdminUserEditor extends Dialog {

    public AdminUserEditor(User user, List<String> roles, UserAdminService adminService, Admin parent) {

        var isUserNew = user.getUserID() == null;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(true);

        var userIDField = new TextField();
        userIDField.setRequired(true);
        var firstNameField = new TextField();
        firstNameField.setRequired(true);
        var lastNameField = new TextField();
        lastNameField.setRequired(true);
        var passwordField = new PasswordField();
        passwordField.setPlaceholder("only for updates!");
        var emailField = new TextField();
        emailField.setRequired(true);
        var activeSelector = new Select<Boolean>();
        activeSelector.setItems(true, false);
        activeSelector.setItemLabelGenerator(b -> b ? "Yes" : "No");
        activeSelector.setEmptySelectionAllowed(false);
        var roleSelector = new CheckboxGroup<String>();
        roleSelector.setItems(roles);
        roleSelector.setRequired(true);

        var form = new FormLayout();
        form.addFormItem(userIDField, "User ID");
        form.addFormItem(passwordField, "Password");
        form.addFormItem(firstNameField, "First Name");
        form.addFormItem(lastNameField, "Last Name");
        form.addFormItem(emailField, "Email");
        form.addFormItem(activeSelector, "Active");
        form.addFormItem(roleSelector, "Roles");

        var binder = new Binder<User>();
        binder.forField(userIDField)
                .withValidator(new StringLengthValidator("User ID should be between 1-20 characters", 1, 20))
                .bind(User::getUserID, isUserNew ? User::setUserID : null);
        binder.forField(passwordField)
                .withValidator(value -> isPasswordFieldContentValid(value, isUserNew),
                        "New or modified password should be between 8-30 characters!")
                .bind(User::getPassword, User::setPassword);
        binder.forField(firstNameField)
                .withValidator(new StringLengthValidator("First name should be between 1-20 characters", 1, 20))
                .bind(User::getFirstName, User::setFirstName);
        binder.forField(lastNameField)
                .withValidator(new StringLengthValidator("Last name should be between 1-20 characters", 1, 20))
                .bind(User::getLastName, User::setLastName);
        binder.forField(emailField)
                .withValidator(new EmailValidator("Email should be a valid email address"))
                .bind(User::getEmail, User::setEmail);
        binder.forField(activeSelector)
                .bind(User::getActive, User::setActive);
        binder.forField(roleSelector)
                .withValidator(selectedRoles ->
                                !selectedRoles.isEmpty() && !selectedRoles.contains("BACKEND"),
                        "Selected roles cannot be empty and cannot contain the BACKEND role!")
                .bind(User::getRoles, User::setRoles);

        binder.readBean(user);

        var saveButton = new Button("Save", e -> {
            try {
                var isInputValid = binder.validate();
                if (isInputValid.isOk()) {
                    binder.writeBeanIfValid(user);
                    adminService.saveUser(user);
                    parent.refreshGridData();
                    close();
                }
            } catch (UserAdminException err) {
                showNotification(err.getMessage());
            }
        });

        var cancelButton = new Button("Cancel", e -> close());

        var buttons = new HorizontalLayout(saveButton, cancelButton);

        add(
                form,
                getSeparator(2),
                buttons,
                getSeparator(2));
    }

    private boolean isPasswordFieldContentValid(String password, Boolean isUserNew) {
        if (isUserNew) {
            return password != null && password.length() >= 8 && password.length() <= 30;
        } else if (password != null && !password.isEmpty()) {
            return password.length() >= 8 && password.length() <= 30;
        }
        return true;
    }


    private HorizontalLayout getSeparator(int height) {
        var separator = new HorizontalLayout();
        separator.setHeight(String.format("%dem", height));
        return separator;
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }
}
