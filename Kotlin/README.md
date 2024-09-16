##This contains the Kotlin implementation of the GildedRose-Refactoring-Kata forked from [here](https://github.com/emilybache/GildedRose-Refactoring-Kata)

##Implementations notes:
- As I couldn't install the test framework to my machine and didn't want to spend too much time on it, 
I used the "golden output" as a resource in GildedRoseTest and implemented a small logic to validate expected output 
against actual output
- As requested in the challenge I couldn't modify the signature of some functions, so I couldn't refactor as much as I 
would've liked (GildedRose to become immutable)
- The new updated logic for ConjuredProcessor is not used (intentionally, since the tests are not updated)