package cantstop;

// Class data stucture for containing 3 targets numbers (2-12; -1 for invalid) 
// and steps taken in each of those target numbers.
public class TrackAdvancements {
    protected int[] target_number = { -1, -1, -1 };
    protected int[] steps_taken = { 0, 0, 0} ;

    TrackAdvancements() {

    }

    TrackAdvancements(int[] steps_given, int[] committedTracks) {
        for (int i = 0; i < 3; i++) {
            steps_taken[i] = steps_given[i];
            target_number[i] = committedTracks[i];
        }
    }

    public int[] getAllTargetNumbers() {
        return target_number;
    }

    public int getTargetNumber(int index) {
        if (index >= 0 && index <= 2) {
            return target_number[index];
        }

    public int[] getAllSteps() {
        return steps_taken;
    }

    public int getSteps(int index) {
        if (index >= 0 && index <= 2) {
            return steps_taken[index];
        }
                
        System.err.printf("Critical error. Index for getTargetNumber not between [0, 2]");
        System.exit(35);
        
        return -1;
    }
}