package mt.um.edu

class SingletonSolver {

  def solve(sudoku: Sudoku): Sudoku = {

    def hypothesis(r: Int, c: Int): Set[Int] = {
      Set(1, 2, 3, 4, 5, 6, 7, 8, 9).diff(
        sudoku.column(c).union(
          sudoku.row(r).union(
            sudoku.block((c / 3) + (r / 3) * 3)
          )
        )
      )
    }

    def allHypothesis(): List[(Int, Int, Set[Int])] = {
      def rowLoop(row: Int, theGrid: List[List[Int]]): List[(Int, Int, Set[Int])] = theGrid match {
        case Nil => Nil
        case r :: rs => {
          def colLoop(col: Int, theRow: List[Int]): List[(Int, Int, Set[Int])] = theRow match {
            case Nil => Nil
            case v :: vs => v match {
              case 0 => (row, col, hypothesis(row, col)) :: colLoop(col + 1, vs)
              case n => colLoop(col + 1, vs)
            }
          }
          colLoop(0, r) ++ rowLoop(row + 1, rs)
        }
      }
      rowLoop(0, sudoku.grid)
    }

    def complete(): Boolean =
      allHypothesis().filter(_._3.nonEmpty).size == 0 // True if all hypotheses are of cardinality 0

    def step(): Sudoku = {
      val hypsSubset = allHypothesis().filter(_._3.size == 1) // filters for cardinality 1

      def applyHyps(grid: List[List[Int]], hyps: List[(Int, Int, Set[Int])]): List[List[Int]] = hyps match {
        case Nil => grid
        case h :: hs => {
          val updatedRow = grid(h._1).updated(h._2, h._3.head) // update column
          applyHyps(grid.updated(h._1, updatedRow), hs) // update row
        }
      }
      new Sudoku(applyHyps(sudoku.grid, hypsSubset))
    }

    if (complete()) sudoku else solve(step())
  }
}
