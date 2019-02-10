import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;

public class SoftwareSystems {

    SoftwareSystem gamezeug;
    SoftwareSystem gitHub;
    SoftwareSystem game;

    SoftwareSystems(Model model) {
        gamezeug = model.addSoftwareSystem(Location.Internal, "Gamezeug", "The second best open source gaming platform");

        gitHub = model.addSoftwareSystem(Location.External, "GitHub", "A hosting service for version control using Git");
        gitHub.addTags(GamezeugTags.EXTERNAL_SYSTEM);

        game = model.addSoftwareSystem(Location.External, "Game", "An open source web-based game which is registered in Gamezeug");
        game.addTags(GamezeugTags.EXTERNAL_SYSTEM);
    }

}