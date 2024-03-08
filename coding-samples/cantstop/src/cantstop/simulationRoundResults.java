package cantstop;

// Used to return simulation results. Includes TrackAdvancements array of size 3
// and additional information on successfully rolled turns.
public class simulationRoundResults extends trackAdvancements {
    private int turns = 0;

    simulationRoundResults() {

    }

    simulationRoundResults(trackAdvancements given_trackAdvancements, int number_of_turns) {
        this.turns = number_of_turns;
        this.steps_taken = given_trackAdvancements.getAllSteps();
        this.target_number = given_trackAdvancements.getAllTargetNumbers();
    }
}