package src.leaderboard;

import java.util.Optional;

public record MatchEntry(Optional<Integer> id, String mapName, String playerName, int goldLeft, int mapHash) {
}
