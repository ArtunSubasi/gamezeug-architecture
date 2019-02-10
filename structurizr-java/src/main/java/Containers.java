import com.structurizr.model.Container;
import com.structurizr.model.Location;
import com.structurizr.model.SoftwareSystem;

public class Containers {

    Container portal;

    Container adminFrontend;
    Container adminBackend;
    Container adminDatabase;

    Container authServer;
    Container userDatabase;

    Container tablesFrontend;
    Container tablesBackend;
    Container tablesDatabase;

    Container chatFrontend;
    Container chatBackend;
    Container chatDatabase;

    Container messageBus;

    Containers(SoftwareSystem system) {
        portal = container(system, GamezeugTags.SPA, "Portal",
                "Provides a web UI to integrate the tables SPA, the chat SPA and external games", "Vue.js");

        adminFrontend = container(system, GamezeugTags.SPA, "Admin Frontend",
                "Single page application for administrative tasks", "Vue.js");
        adminBackend = container(system, GamezeugTags.BACKEND, "Admin Backend",
                "Backend for administrative tasks", "Go");
        adminDatabase = container(system, GamezeugTags.DATABASE, "Admin Database",
                "Contains game registrations and configurations", "MongoDB");

        authServer = container(system, GamezeugTags.BACKEND, "Auth Server",
                "Authenticates and authorizes users using an interal user database or other identity providers." +
                        "Includes a simple server-side rendered UI.", "Spring Boot, Thymeleaf, Kotlin");
        userDatabase = container(system, GamezeugTags.DATABASE, "User Database",
                "Contains the users, their roles and credentials", "PostgreSQL");

        tablesFrontend = container(system, GamezeugTags.SPA, "Tables Frontend",
                "Single page application for table and game status management", "Angular, Typescript");
        tablesBackend = container(system, GamezeugTags.BACKEND, "Tables Backend",
                "Backend for for table and game status management", "Spring Boot, Kotlin");
        tablesDatabase = container(system, GamezeugTags.DATABASE, "Tables Database",
                "Contains current tables, gamers and game status", "MySQL");

        chatFrontend = container(system, GamezeugTags.SPA, "Chat Frontend",
                "Single page application for chatting in the lobby, at the tables or in private", "Vue.js");
        chatBackend = container(system, GamezeugTags.BACKEND, "Chat Backend",
                "Backend for chatting in the lobby, at the tables or in private", "Python");
        chatDatabase = container(system, GamezeugTags.DATABASE, "Chat Database",
                "Contains the chat channels and messages", "Redis");

        messageBus = container(system, GamezeugTags.MESSAGE_BUS, "Message Bus",
                "Loosely couples the Gamezeug microservices and external games using a publish and subscribe pattern",
                "RabbitMQ");
    }

    private Container container(SoftwareSystem system, String tag, String name, String desciption, String technology) {
        Container container = system.addContainer(name, desciption, technology);
        container.addTags(tag);
        return container;
    }

}