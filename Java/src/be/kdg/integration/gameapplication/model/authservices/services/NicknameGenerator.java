package be.kdg.integration.gameapplication.model.authservices.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates random usernames by combining adjectives and nouns.
 * <p>Example output: {@code "CrazyOutlaw"}, {@code "SneakyRobot"}.</p>
 * <p>
 * Used to assign a default username to a player upon registration.
 * </p>
 */

public class NicknameGenerator{
    private Random random = new Random();
    private ArrayList<String> adjectives = new ArrayList<>(List.of(
            "Crazy", "Impossible", "Absolute", "Mad", "Wild", "Weird", "Silly", "Sleepy",
            "Brave", "Loud", "Bouncy", "Shiny", "Sneaky", "Fuzzy", "Grumpy", "Happy",
            "Mighty", "Nerdy", "Lucky", "Epic", "Rusty", "Tricky", "Rapid", "Tiny",
            "Rough", "Swift", "Odd", "Noisy", "Dizzy", "Jolly", "Glitchy", "Chill",
            "Snappy", "Hilarious", "Wobbly", "Zany", "Proud", "Curious", "Sharp", "Cheeky"
    ));

    private ArrayList<String> nouns = new ArrayList<>(List.of(
            "Outlaw", "Builder", "Genius", "Goblin", "Wizard", "Machine", "Bandit", "Rocket",
            "Penguin", "Wizard", "Ninja", "Tiger", "Hammer", "Wizard", "Cactus", "Monkey",
            "Captain", "Rhino", "Wizard", "Raccoon", "Robot", "Samurai", "Goose", "Shark",
            "Pioneer", "Hunter", "Monkey", "Cowboy", "Beast", "Wizard", "Pirate", "Bouncer",
            "Maverick", "Jester", "Forge", "Fighter", "Drifter", "Gremlin", "Phoenix", "Titan"
    ));
    
    public String generateNickname(){
        return adjectives.get(random.nextInt(0, adjectives.size()))+nouns.get(random.nextInt(0, nouns.size()));
    }
}
