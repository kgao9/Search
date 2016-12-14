# Search
This is the solution to the lemonade jugs problem

We have two jugs of size m and n. 
There are 6 actions we can do. We can fill m; we can fill n; we can empty
m; we can empty n; we can pour m into n; we can pour n into m.

The question is, is there a finite set of moves we can do s.t. we get one
pitcher with t amount of liquid in it?

The idea is to first create the statemap - then run BFS or DFS on the states.

Issues with the code:
    First, classes are all in the file. The design reason sucks - I would
    lose points if I separated main and classes.

    Second, the algorithm is naive and it would've been easier to say:
    Oh, if we visited the state, don't add to queue/stack.
    But I had to match the prof's output, so... Yeah I know, not that good
    of a design reason... Well, actually, it could go towards satisfying
    business logic... Nah, I'm not going to justify it like that - it was 
    a bad design choice.
