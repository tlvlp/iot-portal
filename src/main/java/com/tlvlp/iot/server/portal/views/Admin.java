package com.tlvlp.iot.server.portal.views;

import com.tlvlp.iot.server.portal.entities.User;
import com.tlvlp.iot.server.portal.services.UserAdminException;
import com.tlvlp.iot.server.portal.services.UserAdminService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Set;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("tlvlp IoT Portal - Admin")
@Secured("ROLE_ADMIN")
public class Admin extends VerticalLayout implements AfterNavigationObserver {

    private UserAdminService adminService;
    private Grid<User> grid;
    private List<String> roles;

    public Admin(UserAdminService adminService) {
        this.adminService = adminService;
        grid = new Grid<>();
        grid.addColumn(User::getUserID).setHeader("ID")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(User::getFirstName).setHeader("First Name")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(User::getLastName).setHeader("Last Name")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(User::getEmail).setHeader("Email")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(user -> user.getActive() ? "Yes" : "No").setHeader("Active")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(User::getRoles).setHeader("Roles")
                .setFlexGrow(10)
                .setSortable(true);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::getUserDetails));

        var addUserButton = new Button("Add user", event ->
                new AdminUserEditor(getNewDefaultUser(), roles, adminService, this).open());

        add(addUserButton, grid);
    }

    private User getNewDefaultUser() {
        return new User()
                .setRoles(Set.of("USER"))
                .setActive(true);
    }

    private HorizontalLayout getUserDetails(User selectedUser) {
        if (selectedUser.getRoles().contains("BACKEND")) {
            return new HorizontalLayout(new Label("Backend profiles cannot be modified!"));
        }
        var deleteButton = new Button("Delete", event -> {
            try {
                adminService.deleteUser(selectedUser);
                refreshGridData();
            } catch (UserAdminException e) {
                showNotification(e.getMessage());
            }
        });
        var editButton = new Button("Edit", event ->
                new AdminUserEditor(selectedUser, roles, adminService, this).open());
        return new HorizontalLayout(editButton, deleteButton);
    }

    public void refreshGridData() {
        try {
            grid.setItems(adminService.getAllUsers());
            roles = adminService.getRoles();
        } catch (UserAdminException e) {
            showNotification(e.getMessage());
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        refreshGridData();
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }

}
