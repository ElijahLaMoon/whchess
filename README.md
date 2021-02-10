# WHCHESS

## Table of contents
1. [Preface](#preface)
2. [Code structure](#code-structure)
	1. [`domain`](#domain)
	2. [`syntax`](#syntax)
3. [Problems](#problems)
4. [Possible solutions to problems](#possible-solutions-to-problems)

## Preface

Unfortunately, I couldn't finish everything this project needs just in time. However, I hope the following explanations of existing code and ideas on how I wanted to implement missing things will convince in my ability to finish this assignment. My main issue, I suppose, is the lack of knowledge on architectural decisions.

## Code structure

```
├── build.sbt
├── data
│   ├── checkmate.txt
│   ├── sample-moves-invalid.txt
│   └── sample-moves.txt
├── lib
│   └── userinput.jar
├── project
│   └── build.properties
├── README.md
└── src
    └── main
        └── scala
            └── whchess
                ├── domain
                │   ├── Board.scala
                │   ├── Color.scala
                │   ├── Direction.scala
                │   ├── File.scala
                │   ├── Move.scala
                │   ├── Piece.scala
                │   ├── PieceType.scala
                │   ├── Player.scala
                │   ├── Rank.scala
                │   └── Square.scala
                ├── MoveParser.scala
                └── syntax
                    ├── board.scala
                    ├── piece.scala
                    └── square.scala
```

1. `data` folder contains samples of move data provided with the task
2. `lib` folder contains a single Java library provided with the task and used to read moves from files
3. `build.sbt` contains 3 depencdencies, `enumeratum`, `cats`, and `ScalaCheck` (however the last one is redundant in the current state of this project)
4. `wchess` package contains 2 subpackages, and `MoveParser`: 
	1. `MoveParser` is a custom wrapper around the `UserInputFile` class to provide myself a safe and eased access to the outer world with samples of move data
	2. Subpackages are considered in their own sections

### `domain`

This package contains almost everything I needed to represent a "little chess world". As they say "make illegal state unrepresentable", so I made this hierarchy closed in itself. Among other cool things, this way I made the compiler assist me as much as he could (particularly in pattern matching on these structures). Every abstract class or trait here contains just enumerated values and overriden `toString` method, except `Piece` and `Square` which contain some additional helper `val`s or `def`s. `Move` is the simplest structure here, which just captures two `Square`s to move between, it doesn't validate it or anything.

`Board` represent a world where `Square` and `Piece` hierarchies come together. This is a central part of this codebase. By the way, the reason to implement so many custom `toString`s is because I used Scala REPL from sbt to visually validate everything I wrote (also, I used REPL to generate the copypasta for `Square` and others).

### `syntax`

There are 2 pillars of this codebase, namely `Square` and `Piece` models, and `Board` is lying on top of them. To keep all of these models simple and "dumb", I used syntax pattern I saw in "Scala with Cats" book. So, every operation I considered to be usefull in the further development or redundant in original models, I put here. 

`piece` is the most boring among these, it just contains an extension method to retrieve initial squares for each particular `Piece`. I'm still not sure whether it should be here, I just thought `Piece`s shouldn't be aware of the existence of `Square`s directly.

`square` contains extension methods mostly to ease the proccess of retrieving the information about the situation in `Square`s around some particular one. You'll see my intensions to make use of it later.

`board` contains just two things (the other is not implemented), an ASCII rendered for the board and a method to apply moves to board. Rendered board looks like this:

```
  A B C D E F G H
8 r n b q k b n r 8
7 p p p p p p p p 7
6                 6
5                 5
4                 4
3                 3
2 P P P P P P P P 2
1 R N B Q K B N R 1
  A B C D E F G H
```

And the board with E2E4 move applied looks like this:

```
  A B C D E F G H
8 r n b q k b n r 8
7 p p p p p p p p 7
6                 6
5                 5
4         P       4
3                 3
2 P P P P   P P P 2
1 R N B Q K B N R 1
  A B C D E F G H
```

`mkMove`, which applies the moves, returns a `Writer` with the log and the board. Log is used to make the end user aware if his move was successful, otherwise why it failed. For instance, if `Player.White` attempts to make E7E5 move on the beginning of the game (when E7 is clearly occupied with black pawn), he'll find the following message in the log:

```
E7 -> E5 is invalid, Black Pawn is not yours. You're still in charge
```

`mkMove` is supposed to be used as an entry poin in game. However, it lacks certain things, which are considered in the next section.

## Problems

First of all, the project is incomplete. In the final version I would use `mkMoves` as the main entry point to apply all the moves retrieved from `MoveParser` at once. Also, it would probably involve changing the signature of this method to `Writer[List[String], List[Board]` to keep track of all the states of the board. Current one can only log messages and keep the last state of the board. I would implement this method in terms of applying `mkMove` to all the moves provided, concatenating logs, and preventing every other move following some invalid one from being applied (same kind of logic I used in line 65 in `MoveParser`).

There are problems with `mkMove` itself. This method validates every `Move` internally, and in current state of the code it doesn't fail if there're obstacles between the squares, if certain `Piece` is allowed to make such a `Move`, and if `Player` is in check. But I have an opaque idea on how would I do this, it's discussed below.

Also, this codebase is closed on itself in terms on supplying the data for moves. Right now, in order to provide other moves, you have to either change the contents of the hardcoded files, or hardcode new files.

## Possible solutions to problems

Last thing I mentioned in the previous section concerns the supplied data. I guess it could be implemented by parsing the `data` folder, gathering information about the presence (or abscence) of the files there, and then allowing the user to pick a specific file listing them as options when user starts an app calling `sbt run`.

The other thing is validation `Move`s depending on the kind of a particular `Piece`. In my opinion, this could be implemented i.e. for `Rook` as this:

1. Take a `Square` where this `Rook` is located
2. Take straight `Square`s from `nonEmptySurroundings` (probably would be a good idea to make them accessible by calling separate `val`)
3. For each `Square` in resulted set of data filter from `Square.values` only those which fall under `isOnSameRow` and `isOnSameColumn` predicates, and store them in something like `Map[Direction, Vector[Square]]`
4. Take the destination `Square` from `Move`, find the corresponding `Direction` in that `Map`, and take the corresponding `Vector[Square]` (it doesn't sound like an efficient operation right now, but I guess I could a better solution if I put some thought in it)
5. Check if there's no obstacles between `Move.from` and `Move.to` by turning `Vector[Square]` in `Vector[Option[Piece]]` via `board.current` map, and checking if all of them are empty

For `Queen` or `Bishop` I could take diagonal `nonEmptySurroundings` and stack them a couple of times on top of each other until they reach the board's edges to form a matrix of possible moves, and then apply same filtering logic as for the `Rook`.

There's a problem with defining if player is in check, and I'm not sure how to solve it yet. Probably by attaching some `Boolean` flag to the `Board` or something.

From what I can see these are the only problems in code right now.
