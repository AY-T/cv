package cantstop;

import java.util.Random;

/* 
 * Class for simulating board game "Can't Stop" for determening optimal strategy.
 */
public class simulateCantStop {
    // Track length for dice total #             NA  NA   2   3   4   5   6   7   8   9  10  11  12
    private static final int track_lengths[] = {  0,  0,  3,  5,  7,  9, 11, 13, 11,  9,  7,  5,  3 };

    // Option to force stop of rolling after a steps on a track have reached or exceeded track length.
    private static final boolean allow_stop_by_track_end = true;


    /** Throws a 6-sided die.
      * @return int
      */
    private static int throwDie() {
            Random random_generator = new Random();
    
        return (random_generator.nextInt(6) + 1);
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


    /** Calculates the number of matches of a single dice_combination_pair for all commited tracks.
      * 
      * @param dice_combination_pair[2]: sum of two d6 dice, between 2-12.
      * @param committedTracks[3]: unique values between 2-12 signifying chosen tracks to advance.
      * @return int (1 or 2)
      */
    private static int numberOfMatches(int[] dice_combination_pair, int[] committedTracks) {
        int matches = 0;
        for (int pair = 0; pair < 2; pair++) {
            for (int track = 0; track < 3; track++) {
                if (dice_combination_pair[pair] == committedTracks[track]) {
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
        int max_matches = 0; 
        int result[] = { -1, -1};

        for (int combination = 0; combination < 3; combination++) {
            int matches = numberOfMatches(diceCombinations[combination], committedTracks);
            if (matches > max_matches) {
                // NOTE: These could be written with fewer lines of code, but would make code harder to understand.
                max_matches = matches;
                if (matches == 1) {
                    result[0] = combination;
                }
                else if (matches == 2) {
                    result[0] = combination;
                    result[1] = combination;
                }
            /* TODO: Randomize choice with equal number of matches (matches == max_matches).
            else if {
            }
            */
            }

        }

        return result;
    }


    /** Returns index of commited track with given value (2-12).
      * Should not be called without already commited tracks.
      * @param combined_dice_value: sum of two dice (2-12) searched for in committedTracks[].
      * @param committedTracks[3]: unique values between 2-12 signifying chosen tracks to advance.
      * @return Matching track index (0-2) or error (-1).
      */
    private static int getIndexOfCommitedTrack(int combined_dice_value, int[] committedTracks) {

        for (int track_index = 0; track_index < 3; track_index++) {
            if (committedTracks[track_index] == combined_dice_value) {
                return track_index;
            }
        }
        
        // Sanity check. Should never happen. 
        System.err.printf("Critical error. Cannot give track index for %d.%n", combined_dice_value);
        System.exit(10);
        
        return -1;
    }
    

    /** Checks if any of the commited tracks is at its end (which is a good reason to stop playing).
      * @param committedTracks[3]: unique values between 2-12 signifying chosen tracks to advance.
      * @param steps_taken[3]: steps taken in above tracks, with shared indexes.
      * @return bool
      */
    private static boolean checkIfAnyTrackIsFinished(int[] committedTracks, int[] steps_taken) {
        for (int i = 0; i < 3; i++) {
            if (steps_taken[i] >= track_lengths[committedTracks[i]]) {
                return true;
            }
        }
        
        return false;
    }


    /** Simulate committed tracks until no matches are thrown
      * or one track is finished (if allow_stop_by_track_end == TRUE).
      * @param committedTracks[3]: unique values between 2-12 signifying chosen tracks to advance.
      * @return simulationRoundResults
      */
    private static simulationRoundResults simulateFixedTracksUntilBust(int[] committedTracks) { 

        int round = 0;
        int[] steps_taken = { 0, 0, 0 };
        
        // Main loop. Thow dice until you go bust. 
        // NOTE: 1000 repeats should never happen. Upper limit prevents infinite loops.
        final int max_rounds = 1000;

        for (round = 0; round < max_rounds; round++) {

            int[][] diceCombinations = getGiceCombinations();

            int tracks_to_advance[] = whichCombinationsMaximizeStepsTaken(diceCombinations, committedTracks);

            // Case: No steps can be taken. Go bust and return results.
            if (tracks_to_advance[0] == -1) {

                trackAdvancements advancement_results = new trackAdvancements(steps_taken, committedTracks);
                
                simulationRoundResults results = new simulationRoundResults(advancement_results, round);

                return results;
            }
            // Case: A maximum of one step can be taken. (Choice was random if several)
            else if ((tracks_to_advance[0] != -1) && (tracks_to_advance[1] == -1)) {
                int advance_index1 = getIndexOfCommitedTrack(committedTracks[tracks_to_advance[0]], committedTracks);
                steps_taken[advance_index1] += 1;

                if (allow_stop_by_track_end) {

                    if ( checkIfAnyTrackIsFinished(committedTracks, steps_taken) ) {
                        // TODO: Move to method that packs the results
                        trackAdvancements advancement_results = new trackAdvancements(steps_taken, committedTracks);
                        simulationRoundResults results = new simulationRoundResults(advancement_results, round);
                    
                        return results;
                    }
                }

            }
            // Case: Two steps can be taken.
            else if ((tracks_to_advance[0] != -1) && (tracks_to_advance[1] != -1)) {
                int advance_index1 = getIndexOfCommitedTrack(committedTracks[tracks_to_advance[0]], committedTracks);
                int advance_index2 = getIndexOfCommitedTrack(committedTracks[tracks_to_advance[0]], committedTracks);
                
                steps_taken[advance_index1] += 1;
                steps_taken[advance_index2] += 1;

                if (allow_stop_by_track_end) {
                    if ( checkIfAnyTrackIsFinished(committedTracks, steps_taken) ) {
                        // Move to method that packs the results
                        trackAdvancements advancement_results = new trackAdvancements(steps_taken, committedTracks);
                        simulationRoundResults results = new simulationRoundResults(advancement_results, round);

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
        trackAdvancements advancement_results = new trackAdvancements(steps_taken, committedTracks);
        simulationRoundResults results = new simulationRoundResults(advancement_results, round);

        System.err.println("Error. Ran for " + max_rounds + " repeats. This should not happen.");
        System.exit(35);

        return results;
    }


    /** Simulated playing of board game "Can't stop" with preselected tracks. Main method for class.
      */
    public static void simulateTracks() {
        
        final int repeats = 5;

        // Array which will include all results for simulations of different dice combinations.
        // Will be used to mine for information. E.g. if I want to advance sum "3", which
        // other two dice accompanying it will produce the most advances for "3"?
        simulationRoundResults[][][] combined_results;

        // Go though all dice combinations. Only one of each combination should be possible. (i.e. 2/3/4 to 10/11/12)
        // TODO: Temporarely limited to dice combination { 6, 7, 8 }.
        for (int track1 = 6; track1 <= 6; track1++ ) {
            for (int track2 = track1 + 1; track2 <= 7; track2++ ) {
                for (int track3 = track2 + 1; track3 <= 8; track3++ ) {

                    simulationRoundResults[] results_for_current_tracks = new simulationRoundResults[repeats]; 

                    for (int round = 0; round < repeats; round++) {
                        int[] used_tracks = { track1, track2, track3 };
                        // int[] used_tracks = { 12 , 6, 7 };
                        simulationRoundResults simulation_result = simulateFixedTracksUntilBust(used_tracks);
                        /* TODO: It is possible to overshoot a track by 1 step, if both dice match. 
                         *       Compare results to track lengths and fix if necessary. 
                         */

                        results_for_current_tracks[round] = simulation_result;

                    }

                    int total_steps[] = { 0, 0, 0 };
                    
                    for (int round = 0; round < repeats; round++) {
                        total_steps[0] += results_for_current_tracks[round].steps_taken[0];
                        total_steps[1] += results_for_current_tracks[round].steps_taken[1];
                        total_steps[2] += results_for_current_tracks[round].steps_taken[2];                       
                    }

                    for (int i = 0; i < 3; i++) {
                        System.out.print("Steps for " + results_for_current_tracks[0].getTargetNumber(i) + ": " + total_steps[i] + " in " + repeats +" repeats. "); 
                        final double average_steps_per_around = (float)(total_steps[i]/repeats);
                        System.out.printf("Avg. %.1f steps/round. ", average_steps_per_around);
                        final double steps_as_percentage_of_whole_track = average_steps_per_around / (track_lengths[results_for_current_tracks[0].getTargetNumber(i)]) * 100;
                        System.out.printf("(%.1f %% of track)%n", steps_as_percentage_of_whole_track);
                    }

                }
            }
        }
    } // End of simulateTracks()
} // End of class SimulateCantStop
