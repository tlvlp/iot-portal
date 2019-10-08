package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.User;
import com.tlvlp.iot.server.portal.services.AdminService;
import com.tlvlp.iot.server.portal.services.UserAdminException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import javax.validation.Validation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminUserEditor extends Dialog {

    public AdminUserEditor(User user, List<String> roles, AdminService adminService, Admin parent) {

        var isNewUser = false;
        if (user.getUserID() == null) {
            isNewUser = true;
        }

        setCloseOnOutsideClick(false);
        setCloseOnEsc(true);

        var userIDField = new TextField();
        userIDField.setRequired(true);
        var firstNameField = new TextField();
        firstNameField.setRequired(true);
        var lastNameField = new TextField();
        lastNameField.setRequired(true);
        var passwordField = new TextField();
        passwordField.setPlaceholder("Only for password update");
        var emailField = new TextField();
        emailField.setRequired(true);
        var activeSelector = new Select<Boolean>();
        activeSelector.setItems(true, false);
        activeSelector.setItemLabelGenerator(b -> b ? "Yes" : "No");
        activeSelector.setEmptySelectionAllowed(false);
        var roleSelector = new CheckboxGroup<String>();
        roleSelector.setItems(roles);
        roleSelector.setValue(Set.of("USER"));
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
        binder.forField(userIDField).bind(User::getUserID, isNewUser ? User::setUserID : null);
        binder.forField(passwordField).bind(User::getPassword, User::setPassword);
        binder.forField(firstNameField).bind(User::getFirstName, User::setFirstName);
        binder.forField(lastNameField).bind(User::getLastName, User::setLastName);
        binder.forField(emailField).bind(User::getEmail, User::setEmail);
        binder.forField(activeSelector).bind(User::getActive, User::setActive);
        binder.forField(roleSelector).bind(User::getRoles, User::setRoles);

        if (!isNewUser) {
            binder.readBean(user);
        }

        var saveButton = new Button("Save", e -> {
            List<String> validationProblems = List.of();
            try {
                binder.writeBeanIfValid(user);
                adminService.saveUser(user);
                parent.refreshGridData();
                close();
            } catch (UserAdminException err) {
                var notification = new Notification(err.getMessage());
                notification.setPosition(Notification.Position.MIDDLE);
                notification.setDuration(20);
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

    private List<String> getValidationProblems(User user) {
        var result = Validation.buildDefaultValidatorFactory().getValidator().validate(user);
        if (result.isEmpty()) {
            return List.of();
        }
        return result.stream()
                .map(cv -> String.format("%s: %s", cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
    }

    private HorizontalLayout getSeparator(int height) {
        var separator = new HorizontalLayout();
        separator.setHeight(String.format("%dem", height));
        return separator;
    }

}
