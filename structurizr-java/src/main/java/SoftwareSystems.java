import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

class SoftwareSystems {

    SoftwareSystem gamezeug;
    SoftwareSystem gitHub;
    SoftwareSystem game;

    SoftwareSystems(Model model) {
        blackBox(model);
        externalSystems(model);
    }

    private void externalSystems(Model model) {
        gitHub = externalSystem(model, "GitHub", "A hosting service for version control using Git");
        game = externalSystem(model, "Game", "An open source web-based game which is registered in Gamezeug");
    }

    private SoftwareSystem externalSystem(Model model, String name, String desciption) {
        SoftwareSystem system = model.addSoftwareSystem(Location.External, name, desciption);
        system.addTags(GamezeugTags.EXTERNAL_SYSTEM);
        return system;
    }

    private void blackBox(Model model) {
        gamezeug = model.addSoftwareSystem(Location.Internal, "Gamezeug", "The second best open source gaming platform");
        gamezeug.addTags(GamezeugTags.SYSTEM_IN_SCOPE);
    }

}