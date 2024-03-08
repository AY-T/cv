package cantstop;

// Used to return simulation results. Includes TrackAdvancements array of size 3
// and additional information on successfully rolled turns.
public class Simulation_round_results extends TrackAdvancements {
    private int turns = 0;

    Simulation_round_results() {

    }

    Simulation_round_results(TrackAdvancements given_trackAdvancements, int number_of_turns) {
        this.turns = number_of_turns;
        this.steps_taken = given_trackAdvancements.getAllSteps();
        this.target_number = given_trackAdvancements.getAllTargetNumbers();
    }
}