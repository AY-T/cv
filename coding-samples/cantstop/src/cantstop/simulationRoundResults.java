package cantstop;

// Used to return simulation results. Includes TrackAdvancements array of size 3
// and additional information on successfully rolled turns.
public class simulationRoundResults extends trackAdvancements {
    private int turns = 0;

    simulationRoundResults() {

    }

    simulationRoundResults(trackAdvancements givenTrackAdvancements, int numberOfTurns) {
        this.turns = numberOfTurns;
        this.stepsTaken = givenTrackAdvancements.getAllSteps();
        this.targetNumber = givenTrackAdvancements.getAllTargetNumbers();
    }
}