package cantstop;

// Class data stucture for containing 3 targets numbers (2-12; -1 for invalid) 
// and steps taken in each of those target numbers.
public class trackAdvancements {
    protected int[] targetNumber = { -1, -1, -1 };
    protected int[] stepsTaken = { 0, 0, 0} ;

    trackAdvancements() {

    }

    trackAdvancements(int[] steps_given, int[] committedTracks) {
        for (int i = 0; i < 3; i++) {
            stepsTaken[i] = steps_given[i];
            targetNumber[i] = committedTracks[i];
        }
    }

    public int[] getAllTargetNumbers() {
        return targetNumber;
    }

    public int getTargetNumber(int index) {
        if (index >= 0 && index <= 2) {
            return targetNumber[index];
        }
        return -1;
    }

    public int[] getAllSteps() {
        return stepsTaken;
    }

    public int getSteps(int index) {
        if (index >= 0 && index <= 2) {
            return stepsTaken[index];
        }
                
        System.err.printf("Critical error. Index for getTargetNumber not between [0, 2]");
        System.exit(35);
        
        return -1;
    }
}