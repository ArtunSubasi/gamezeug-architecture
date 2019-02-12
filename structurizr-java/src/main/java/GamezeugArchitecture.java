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
        Containers gamezeugContainers = new Containers(softwareSystems.gamezeug);
        new DeploymentNodes(model, gamezeugContainers);

        createRelationships(persons, softwareSystems, gamezeugContainers);
        ViewSet views = createViews(workspace, softwareSystems);
        addDocumentation(workspace, softwareSystems);
        addStyling(views);
        uploadWorkspaceToStructurizr(workspace);
    }

    private static void addStyling(ViewSet views) {
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.PERSON).background(Color.PERSON).color(Color.FONT).shape(Shape.Person);
        styles.addElementStyle(GamezeugTags.EXTERNAL_SYSTEM).background(Color.EXTERNAL).color(Color.FONT);
        styles.addElementStyle(GamezeugTags.DATABASE).background(Color.INTERNAL).color(Color.FONT).shape(Shape.Cylinder);
        styles.addElementStyle(GamezeugTags.MESSAGE_BUS).background(Color.INTERNAL).color(Color.FONT).width(2900).shape(Shape.Pipe);
        styles.addElementStyle(GamezeugTags.SPA).background(Color.INTERNAL).color(Color.FONT).shape(Shape.WebBrowser);
        styles.addElementStyle(GamezeugTags.BACKEND).background(Color.INTERNAL).color(Color.FONT);

        styles.addElementStyle(GamezeugTags.SYSTEM_IN_SCOPE)
                .background(Color.INTERNAL).color(Color.FONT).shape(Shape.Box).width(ElementStyle.DEFAULT_WIDTH);
    }

    // add some documentation (markdown, etc)
    private static void addDocumentation(Workspace workspace, SoftwareSystems softwareSystems) {
        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
        template.addContextSection(softwareSystems.gamezeug, Format.Markdown,
                "Here is some context about the software system...\n" +
                        "\n" +
                        "![](embed:SystemContext)");
        template.addContainersSection(softwareSystems.gamezeug, Format.Markdown,
                "This sections shows the containers within the Gamezeug system including " +
                        "single page apps, backend servers, databases and important infrastructure elements.\n" +
                        "\n" +
                        "![](embed:ContainerView)");
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

        ContainerView containerView = views.createContainerView(softwareSystems.gamezeug,
                "ContainerView", "Building Block View");
        containerView.setPaperSize(PaperSize.A3_Landscape);
        containerView.addAllContainersAndInfluencers();

        DeploymentView deploymentView = views.createDeploymentView(softwareSystems.gamezeug,
                "DeploymentView", "Production Deployment View");
        deploymentView.setPaperSize(PaperSize.A3_Landscape);
        deploymentView.addAllDeploymentNodes();
        deploymentView.setEnvironment("Production");

        return views;
    }

    private static void createRelationships(Persons persons, SoftwareSystems softwareSystems, Containers gamezeugContainers) {
        persons.gamer.uses(softwareSystems.gamezeug, "plays games");
        persons.gameAdmin.uses(softwareSystems.gamezeug, "administer game registrations");
        softwareSystems.gamezeug.uses(softwareSystems.gitHub, "authenticates users using (optional)");
        softwareSystems.game.uses(softwareSystems.gamezeug, "publishes game events and receives table events");

        persons.gamer.uses(gamezeugContainers.reverseProxy, "plays games and chats using");
        persons.gameAdmin.uses(gamezeugContainers.reverseProxy, "administrates games using");
        gamezeugContainers.reverseProxy.uses(gamezeugContainers.portal, "reads HTML template from");

        gamezeugContainers.authServer.uses(softwareSystems.gitHub, "authenticates users using (optional)");
        gamezeugContainers.authServer.uses(gamezeugContainers.userDatabase, "writes to / reads from");

        gamezeugContainers.portal.uses(gamezeugContainers.authServer, "includes login form");
        gamezeugContainers.portal.uses(gamezeugContainers.adminFrontend, "includes admin fragment");
        gamezeugContainers.portal.uses(gamezeugContainers.tablesFrontend, "includes tables fragment");
        gamezeugContainers.portal.uses(gamezeugContainers.chatFrontend, "includes chat fragment");
        gamezeugContainers.portal.uses(softwareSystems.game, "includes external game fragment");

        gamezeugContainers.adminFrontend.uses(gamezeugContainers.adminBackend, "uses");
        gamezeugContainers.tablesFrontend.uses(gamezeugContainers.tablesBackend, "uses");
        gamezeugContainers.chatFrontend.uses(gamezeugContainers.chatBackend, "uses");

        gamezeugContainers.adminBackend.uses(gamezeugContainers.adminDatabase, "writes to / reads from");
        gamezeugContainers.tablesBackend.uses(gamezeugContainers.tablesDatabase, "writes to / reads from");
        gamezeugContainers.chatBackend.uses(gamezeugContainers.chatDatabase, "writes to / reads from");

        gamezeugContainers.adminBackend.uses(gamezeugContainers.messageBus, "publishes game registrations");
        gamezeugContainers.tablesBackend.uses(gamezeugContainers.messageBus,
                "publishes table events, subscribes to game registrations and external game events");
        gamezeugContainers.chatBackend.uses(gamezeugContainers.messageBus,
                "subscribes to table events and external game events");
        softwareSystems.game.uses(gamezeugContainers.messageBus, "publishes game events, subscribes to table events");
    }

    private static void uploadWorkspaceToStructurizr(Workspace workspace) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}