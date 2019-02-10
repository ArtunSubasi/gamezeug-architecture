import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentationTemplate;
import com.structurizr.model.Model;
import com.structurizr.model.Tags;
import com.structurizr.view.*;

/**
 * Describes the Gamezeug Architecture using the Structurizr-Java-Client
 * and uploads it to the Gamezeug-Structurizr-Workspace.
 */
public class GamezeugArchitecture {

    private static final long WORKSPACE_ID = 42587;
    private static final String API_KEY = "b277211d-e629-4781-937d-0f1ddcd0ed4b";
    private static final String API_SECRET = System.getenv("STRUCTURIZR_API_SECRET");

    public static void main(String[] args) throws Exception {
        // a Structurizr workspace is the wrapper for a software architecture model, views and documentation
        Workspace workspace = new Workspace("Gamezeug","Gamezeug is a software development playground to try out new libraries, frameworks, methods, etc.");
        Model model = workspace.getModel();
        Persons persons = new Persons(model);
        SoftwareSystems softwareSystems = new SoftwareSystems(model);

        createRelationships(persons, softwareSystems);
        ViewSet views = createViews(workspace, softwareSystems);
        addDocumentation(workspace, softwareSystems);
        addStyling(views);
        uploadWorkspaceToStructurizr(workspace);
    }

    private static void addStyling(ViewSet views) {
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
        styles.addElementStyle(GamezeugTags.EXTERNAL_SYSTEM).background("#757575").color("#ffffff");
    }

    // add some documentation (markdown, etc)
    private static void addDocumentation(Workspace workspace, SoftwareSystems softwareSystems) {
        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
        template.addContextSection(softwareSystems.gamezeug, Format.Markdown,
                "Here is some context about the software system...\n" +
                        "\n" +
                        "![](embed:SystemContext)");
    }

    // define some views (the diagrams you would like to see)
    private static ViewSet createViews(Workspace workspace, SoftwareSystems softwareSystems) {
        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(softwareSystems.gamezeug,
                "SystemContext", "System Scope and Context of Gamezeug.");
        contextView.setPaperSize(PaperSize.A5_Landscape);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
        contextView.setEnterpriseBoundaryVisible(false);
        return views;
    }

    private static void createRelationships(Persons persons, SoftwareSystems softwareSystems) {
        persons.gamer.uses(softwareSystems.gamezeug, "plays games");
        persons.gameAdmin.uses(softwareSystems.gamezeug, "administer game registrations");
        softwareSystems.gamezeug.uses(softwareSystems.gitHub, "authenticates users using (optional)");
        softwareSystems.game.uses(softwareSystems.gamezeug, "publishes game events and receives table events");
    }

    private static void uploadWorkspaceToStructurizr(Workspace workspace) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}