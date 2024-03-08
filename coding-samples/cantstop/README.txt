Simulation of board game "Can't Stop"  
=====================================

- Simple program to find optimal strategy for dice-based boardgame "Can't stop".

- Overview of "Can't Stop" can be found @ https://boardgamegeek.com/boardgame/41/cant-stop

- Learn game rules in a few minutes @ 
  https://boardgamegeek.com/video/55559/cant-stop/game-boy-geek-dice-tower-reviews-cant-stop

- If you want to try the game yourself - or learn interactively, go to:
  https://boardgamearena.com/gamepanel?game=cantstop

- Dice games are interesting, because when using more than one die, 
  probabilies create an additional and surpricingly complex layer of extra strategy. 

- As an example, when calculating the sum of two 6-sided dice, the probability 
  distrubution is heavily skewed toward the middle numbers (e.g. 6, 7 and 8), 
  while the numbers on the edges of the range, e.g. 2, 12, only have a single valid combination.

  SUMS OF 2 6-SIDED DICE

            DIE 1
       1  2  3  4  5  6
     ------------------
 D  1| 2  3  4  5  6  7
 I  2| 3  4  5  6  7  8
 E  3| 4  5  6  7  8  9
    4| 5  6  7  8  9 10
 2  5| 6  7  8  9 10 11
    6| 7  8  9 10 11 12

- As this game uses four 6-sided dice that can be combined into two sets of two 
  6-sided dice (as above), the probabilies become exceedingly more complex.

- Solving "Can't stop" mathematically is not attempted here. Instead, 
  this program simulates different preset dice combination and prints 
  out expected number of successful turns for different combinations.
 
- The following simplifications are made as opposed to a real game of "Can't stop": 
   - Valid two-dice combination (2-12) are preset before the simulation, instead of being chosen in-game.
   - Every simulation is on a clean game board without any advancements (despite combinations being preset).

- Even with these simplifications, an optimal strategy should be derivable from results.

Future improvements:
   - Implement analysis of statistics.
   - Currently allows overshooting on tracks (by 1). Not a huge difference but should be fixed.
   - Save some memory, e.g. change some INTs to SHORTs/BYTEs
   - Make sure comments match current spec
   - Write some tests
       - Code was tested during writing, but should have dedicated tests (ran out of time).
   - Use different advance strategies: 
       - Advance the number already furthest ahead
       - Advance the number furthest behind
   - Cleaner outputs
   - Should the game start "on turn 3" with 1 advancements each?
   - Change this README to an md file.

- This program was made partly as a programming demo, partially due to my own curiosity as for
  the optimal stretegies in Can't Stop.

- Amount of work used for this project? Not measured, but maybe 2 working days?

- @Author: Antti Y-T, etunimi piste sukunimi toimialueella iki piste fi













