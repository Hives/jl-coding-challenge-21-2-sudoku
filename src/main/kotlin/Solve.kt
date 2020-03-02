fun solve(squares: List<Int>) = foo(setOf(Board(squares)))

private tailrec fun foo(boards: Set<Board>): Set<Board> {
    val newBoards = deduceAndOrGuessIncompleteBoards(boards)

    return if (newBoards.all { it.isSolution }) {
        newBoards
    } else {
        foo(newBoards)
    }
}

private fun deduceAndOrGuessIncompleteBoards(boards: Set<Board>) =
    boards.flatMap { board ->
        if (board.isSolution) {
            setOf(board)
        } else {
            deduceAndOrGuessBoards(board)
        }
    }.toSet()

private fun deduceAndOrGuessBoards(board: Board): Set<Board> {
    val newBoard = deduceUntilExhausted(board) ?: return emptySet()

    return if (newBoard.isSolution) {
        setOf(newBoard)
    } else {
        makeAGuess(newBoard)
    }
}

private tailrec fun deduceUntilExhausted(board: Board): Board? {
    val newBoard = board.deduce()

    if (newBoard.isInvalid) return null
    if (newBoard.isSolution) return newBoard
    if (newBoard == board) return newBoard

    return deduceUntilExhausted(newBoard)
}

private fun makeAGuess(board: Board): Set<Board> {
    val index = board.unsolvedSquareWithFewestPossibilities()
    val possibilities = board.getPossibilitiesFor(index)
    return possibilities.map { Board(board.squares.update(index, it)) }.toSet()
}