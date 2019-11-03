package com.tlvlp.iot.server.portal.security;

import com.tlvlp.iot.server.portal.MainView;
import com.tlvlp.iot.server.portal.views.LoginView;
import com.tlvlp.iot.server.portal.views.UnitList;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> {
			final UI ui = uiEvent.getUI();
			ui.addBeforeLeaveListener(this::beforeLeave);
		});
	}

	private void beforeLeave(BeforeLeaveEvent event) {
		var isUserAuthorizedForTarget = SecurityUtils.isAccessGranted(event.getNavigationTarget());
		var isUserLoggedIn = SecurityUtils.isUserLoggedIn();
		var isPageAccessedDirectly = !event.hasRerouteTarget();
		if(!isUserAuthorizedForTarget) {
			if(isUserLoggedIn) {
				event.postpone();
				showNotification("Access denied!");
				if(isPageAccessedDirectly) {
					event.rerouteTo(MainView.class);
				}
			} else {
				event.rerouteTo(LoginView.class);
			}
		}
	}

	private void showNotification(String message) {
		var error = new Dialog();
		error.add(new Label(message));
		error.setCloseOnEsc(true);
		error.setCloseOnOutsideClick(true);
		error.open();
	}

}