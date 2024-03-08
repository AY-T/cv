package cantstop;

import java.util.Random;

/* 
 * Class for simulating board game "Can't Stop" for determening optimal strategy.
 */
public class simulateCantStop {
    // Track length for dice total #             NA  NA   2   3   4   5   6   7   8   9  10  11  12
    private static final int TRACK_LENGTHS[] = {  0,  0,  3,  5,  7,  9, 11, 13, 11,  9,  7,  5,  3 };

    // Option to force stop of rolling after a steps on a track have reached or exceeded track length.
    private static final boolean ALLOW_STOP_BY_TRACK_END = true;


    /** Throws a 6-sided die.
      * @return int
      */
    private static int throwDie() {
            Random randomGenerator = new Random();
    
        return (randomGenerator.nextInt(6) + 1);
    }


    /** Gets all possible combinations for 4 thrown dice
      * @return int[3][2] of all possible combinations.
      */
    private static int[][] getGiceCombinations() {
        int[] dice = { throwDie(), throwDie(), throwDie(), throwDie()};

        // NOTE: There are only 3 possible dice combinations when combining 4 dice.
        int[][] diceCombinations = { { (dice[0] + dice[1]), (dice[2] + dice[3])}, 
                                     { (dice[0] + dice[2]), (dice[1] + dice[3])}, 
                                     { (dice[0] + dice[3]), (dice[1] + dice[2])}};
        return diceCombinations;
    }


    /** Calculates the number of matches of a single diceCombinationPair for all commited tracks.
      * 
      * @param diceCombinationPair[2]: sum of two d6 dice, between 2-12.
      * @param committedTracks[3]: unique values between 2-12 signifying chosen tracks to advance.
      * @return int (1 or 2)
      */
    private static int numberOfMatches(int[] diceCombinationPair, int[] committedTracks) {
        int matches = 0;
        for (int pair = 0; pair < 2; pair++) {
            for (int track = 0; track < 3; track++) {
                if (diceCombinationPair[pair] == committedTracks[track]) {
                    matches++;
                }
            }
        }
        return matches;
    }


    /** Finds out which of given diceCombinations gives most advancements (0, 1 or 2 steps).
      * 
      * TODO: Choice isn't random, but skewed towards earlier numbers in the list. Bug.
      * 
      * @param diceCombinations[3][2]: All possible combinations of pairs with thrown 4 dice.
      * @param committedTracks[3]: unique values between 2-12 signifying chosen tracks to advance.
      * @return int[]
      */  
    private static int[] whichCombinationsMaximizeStepsTaken(int[][] diceCombinations, int[] committedTracks) {
        int maxMatches = 0; 
        int result[] = { -1, -1};

        for (int combination = 0; combination < 3; combination++) {
            int matches = numberOfMatches(diceCombinations[combination], committedTracks);
            if (matches > maxMatches) {
                // NOTE: These could be written with fewer lines of code, but would make code harder to understand.
                maxMatches = matches;
                if (matches == 1) {
                    result[0] = combination;
                }
                else if (matches == 2) {
                    result[0] = combination;
                    result[1] = combination;
                }
            /* TODO: Randomize choice with equal number of matches (matches == maxMatches).
            else if {
            }
            */
            }

        }

        return result;
    }


    /** Returns index of commited track with given value (2-12).
      * Should not be called without already commited tracks.
      * @param combinedDiceValue: sum of two dice (2-12) searched for in committedTracks[].
      * @param committedTracks[3]: unique values between 2-12 signifying chosen tracks to advance.
      * @return Matching track index (0-2) or error (-1).
      */
    private static int getIndexOfCommitedTrack(int combinedDiceValue, int[] committedTracks) {

        for (int trackIndex = 0; trackIndex < 3; trackIndex++) {
            if (committedTracks[trackIndex] == combinedDiceValue) {
                return trackIndex;
            }
        }
        
        // Sanity check. Should never happen. 
        System.err.printf("Critical error. Cannot give track index for %d.%n", combinedDiceValue);
        System.exit(10);
        
        return -1;
    }
    

    /** Checks if any of the commited tracks is at its end (which is a good reason to stop playing).
      * @param committedTracks[3]: unique values between 2-12 signifying chosen tracks to advance.
      * @param stepsTaken[3]: steps taken in above tracks, with shared indexes.
      * @return bool
      */
    private static boolean checkIfAnyTrackIsFinished(int[] committedTracks, int[] stepsTaken) {
        for (int i = 0; i < 3; i++) {
            if (stepsTaken[i] >= TRACK_LENGTHS[committedTracks[i]]) {
                return true;
            }
        }
        
        return false;
    }


    /** Simulate committed tracks until no matches are thrown
      * or one track is finished (if ALLOW_STOP_BY_TRACK_END == TRUE).
      * @param committedTracks[3]: unique values between 2-12 signifying chosen tracks to advance.
      * @return simulationRoundResults
      */
    private static simulationRoundResults simulateFixedTracksUntilBust(int[] committedTracks) { 

        int round = 0;
        int[] stepsTaken = { 0, 0, 0 };
        
        // Main loop. Thow dice until you go bust. 
        // NOTE: 1000 repeats should never happen. Upper limit prevents infinite loops.
        final int MAX_ROUNDS = 1000;

        for (round = 0; round < MAX_ROUNDS; round++) {

            int[][] diceCombinations = getGiceCombinations();

            int tracksToAdvance[] = whichCombinationsMaximizeStepsTaken(diceCombinations, committedTracks);

            // Case: No steps can be taken. Go bust and return results.
            if (tracksToAdvance[0] == -1) {

                trackAdvancements advancementResults = new trackAdvancements(stepsTaken, committedTracks);
                
                simulationRoundResults results = new simulationRoundResults(advancementResults, round);

                return results;
            }
            // Case: A maximum of one step can be taken. (Choice was random if several)
            else if ((tracksToAdvance[0] != -1) && (tracksToAdvance[1] == -1)) {
                int advanceIndex1 = getIndexOfCommitedTrack(committedTracks[tracksToAdvance[0]], committedTracks);
                stepsTaken[advanceIndex1] += 1;

                if (ALLOW_STOP_BY_TRACK_END) {

                    if ( checkIfAnyTrackIsFinished(committedTracks, stepsTaken) ) {
                        // TODO: Move to method that packs the results
                        trackAdvancements advancementResults = new trackAdvancements(stepsTaken, committedTracks);
                        simulationRoundResults results = new simulationRoundResults(advancementResults, round);
                    
                        return results;
                    }
                }

            }
            // Case: Two steps can be taken.
            else if ((tracksToAdvance[0] != -1) && (tracksToAdvance[1] != -1)) {
                int advanceIndex1 = getIndexOfCommitedTrack(committedTracks[tracksToAdvance[0]], committedTracks);
                int advanceIndex2 = getIndexOfCommitedTrack(committedTracks[tracksToAdvance[0]], committedTracks);
                
                stepsTaken[advanceIndex1] += 1;
                stepsTaken[advanceIndex2] += 1;

                if (ALLOW_STOP_BY_TRACK_END) {
                    if ( checkIfAnyTrackIsFinished(committedTracks, stepsTaken) ) {
                        // Move to method that packs the results
                        trackAdvancements advancementResults = new trackAdvancements(stepsTaken, committedTracks);
                        simulationRoundResults results = new simulationRoundResults(advancementResults, round);

                        return results;
                    }
                }

            }
            // Sanity check. This should never happen.
            else { 
                System.err.println("Critical error. Out of cases.");
                System.exit(15);
            }
        }

        // Move to method that packs the results
        trackAdvancements advancementResults = new trackAdvancements(stepsTaken, committedTracks);
        simulationRoundResults results = new simulationRoundResults(advancementResults, round);

        System.err.println("Error. Ran for " + MAX_ROUNDS + " repeats. This should not happen.");
        System.exit(35);

        return results;
    }


    /** Simulated playing of board game "Can't stop" with preselected tracks. Main method for class.
      */
    public static void simulateTracks() {
        
        final int REPEATS = 5;

        // Array which will include all results for simulations of different dice combinations.
        // Will be used to mine for information. E.g. if I want to advance sum "3", which
        // other two dice accompanying it will produce the most advances for "3"?
        simulationRoundResults[][][] combinedResults;

        // Go though all dice combinations. Only one of each combination should be possible. (i.e. 2/3/4 to 10/11/12)
        // TODO: Temporarely limited to dice combination { 6, 7, 8 }.
        for (int track1 = 6; track1 <= 6; track1++ ) {
            for (int track2 = track1 + 1; track2 <= 7; track2++ ) {
                for (int track3 = track2 + 1; track3 <= 8; track3++ ) {

                    simulationRoundResults[] resultsForCurrentTracks = new simulationRoundResults[REPEATS]; 

                    for (int round = 0; round < REPEATS; round++) {
                        int[] usedTracks = { track1, track2, track3 };
                        // int[] usedTracks = { 12 , 6, 7 };
                        simulationRoundResults simulationResult = simulateFixedTracksUntilBust(usedTracks);
                        /* TODO: It is possible to overshoot a track by 1 step, if both dice match. 
                         *       Compare results to track lengths and fix if necessary. 
                         */

                        resultsForCurrentTracks[round] = simulationResult;

                    }

                    int totalSteps[] = { 0, 0, 0 };
                    
                    for (int round = 0; round < REPEATS; round++) {
                        totalSteps[0] += resultsForCurrentTracks[round].getSteps(0);
                        totalSteps[1] += resultsForCurrentTracks[round].getSteps(1);
                        totalSteps[2] += resultsForCurrentTracks[round].getSteps(2);                       
                    }

                    for (int i = 0; i < 3; i++) {
                        System.out.print("Steps for " + resultsForCurrentTracks[0].getTargetNumber(i) + ": " + totalSteps[i] + " in " + REPEATS +" repeats. "); 
                        double averageStepsPerRound = (float)(totalSteps[i]/REPEATS);
                        System.out.printf("Avg. %.1f steps/round. ", averageStepsPerRound);
                        double stepsAsPercentageOfWholeTrack = averageStepsPerRound / (TRACK_LENGTHS[resultsForCurrentTracks[0].getTargetNumber(i)]) * 100;
                        System.out.printf("(%.1f %% of track)%n", stepsAsPercentageOfWholeTrack);
                    }

                }
            }
        }
    } // End of simulateTracks()
} // End of class SimulateCantStop
