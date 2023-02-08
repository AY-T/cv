Sample coding project: network speed
===================

- To run the code, download the repo and open network_speed.html in your browser. (Or just download network_speed.html and src/network_speed.js)

- Unit tests are in the same file, and are output to the JavaScript console (ctrl+shift+j in most browsers).

NOTES:

- Please note, that while I have plenty of experience with languages in the C-language famility, 
  I (re)learned JavaScript/TypeScript specifically for the assignment.

- Putting unit tests in the same file as the code being tested is likely not the standard solution 
  but felt "good enough" for this sample project.

- Unit tests do not cover everything, but cover enough to be relatively confident the program does what it should.

- No attempt is made to follow conventions like ECMAScript, as it is unclear to me which version 
  is wanted. Code will be updated to specified standard if requested.

- The random test makes a wild assumption on the whole decision space for coordinates being approx. [-1000, 1000]
  for both X and Y. This is purely hypothetical and is done to demonstrate random testing. Specification
  implies a coordinate space of [0, 100] for x and y, but this should not be assumed, and used to unnecessarely 
  limit the testing range.

- Specification behind this implementation is assumed to be proprietary and is not given here. Sorry.
