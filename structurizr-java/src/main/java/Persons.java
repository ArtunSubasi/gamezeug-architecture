import com.structurizr.model.Model;
import com.structurizr.model.Person;

public class Persons {

    Person gamer;
    Person gameAdmin;

    Persons(Model model) {
        gamer = model.addPerson("Gamer", "A gamer person who likes to play games");
        gameAdmin = model.addPerson("Game Admin", "A person who can administer games in Gamezeug");
    }

}