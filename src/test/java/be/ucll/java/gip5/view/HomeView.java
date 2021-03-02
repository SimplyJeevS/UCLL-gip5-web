package be.ucll.java.gip5.view;

import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

/**
 * A Designer generated component for the home-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("home-view")
@JsModule("./home-view.js")
public class HomeView extends PolymerTemplate<HomeView.HomeViewModel> {

    /**
     * Creates a new HomeView.
     */
    public HomeView() {
        // You can initialise any data required for the connected UI components here.
    }

    /**
     * This model binds properties between HomeView and home-view
     */
    public interface HomeViewModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
