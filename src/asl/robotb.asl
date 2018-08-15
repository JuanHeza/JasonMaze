/*
Very simple vaccum cleaner agent in a world that has n x m locations.

Perceptions:
. dirty: the current location (x,y) is dirty
.
Actions:
. suck: clean the current location
. move: move the robot randomly to one of its neighbor locations

Author: Romeo Sanchez
.*/

+dirty(X,Y): at(robotb,X,Y) <- suck(X,Y).

+clean(X,Y): at(robotb,X,Y) <- move.





