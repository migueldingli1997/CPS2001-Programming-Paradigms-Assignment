package recfun

/**
  * Created by Miguel on 17/11/2016.
  */
object Testing {

  def main(args: Array[String]): Unit = {

    def p  = Main.pascal(_,_)
    def f  = Main.fib(_)
    def ff = Main.fastFib(_)
    def b  = Main.balance(_)
    def cc = Main.countChange(_,_)

    println("pascal(0,0) = " + p(0,0))
    println("pascal(1,4) = " + p(1,4))
    println("pascal(2,3) = " + p(2,3))
    println()
    println("      " + p(0,0))
    println("     " + p(0,1) + " " + p(1,1))
    println("    " + p(0,2) + " " + p(1,2) + " " + p(2,2))
    println("   " + p(0,3) + " " + p(1,3) + " " + p(2,3) + " " + p(3,3))
    println("  " + p(0,4) + " " + p(1,4) + " " + p(2,4) + " " + p(3,4) + " " + p(4,4))
    println("" + p(0,5) + " " + p(1,5) + " " + p(2,5) + " " + p(3,5) + " " + p(4,5) + " " + p(5,5))
    println()
    println("fib(0)  = " + f(0))
    println("fib(1)  = " + f(1))
    println("fib(2)  = " + f(2))
    println("fib(10) = " + f(10))
    println("fib(30) = " + f(30))
    println("fib(40) = " + f(40))
    println()
    println("fastFib(0)  = " + ff(0))
    println("fastFib(1)  = " + ff(1))
    println("fastFib(2)  = " + ff(2))
    println("fastFib(30) = " + ff(30))
    println("fastFib(40) = " + ff(40))
    println("fastFib(50) = " + ff(50))
    println("fastFib(83) = " + ff(83))
    println()
    println("balance(\"\".toList)                                 = " + b("".toList))
    println("balance(\") ( ) (\".toList)                          = " + b(") ( ) (".toList))
    println("balance(\"(if (zero) (x + 1) else (x * 3))\".toList) = " + b("(if (zero) (x + 1) else (x * 3))".toList))
    println("balance(\"(a * (b + b) * c)\".toList)                = " + b("(a * (b + b) * c)".toList))
    println("balance(\"(if (weekend) :) else :(\".toList)         = " + b("(if (weekend) :) else :(".toList))
    println("balance(\"(()))(\".toList)                           = " + b("(()))(".toList))
    println()
    println("countChange(0,List(1,2))                          = " + cc(0,List(1,2)))
    println("countChange(0,List())                             = " + cc(0,List()))
    println("countChange(1,List(1,2))                          = " + cc(1,List(1,2)))
    println("countChange(1,List())                             = " + cc(1,List()))
    println("countChange(2,List(1,2))                          = " + cc(2,List(1,2)))
    println("countChange(3,List(1,2))                          = " + cc(3,List(1,2)))
    println("countChange(4,List(1,2))                          = " + cc(4,List(1,2)))
    println("countChange(300,List(5,10,20,50,100,200,500))     = " + cc(300,List(5,10,20,50,100,200,500)))
    println("countChange(301,List(5,10,20,50,100,200,500))     = " + cc(301,List(5,10,20,50,100,200,500)))
    println("countChange(300,List(500,5,50,100,20,200,10))     = " + cc(300,List(500,5,50,100,20,200,10)))
    println("countChange(300,List(1,2,5,10,20,50,100,200,500)) = " + cc(300,List(1,2,5,10,20,50,100,200,500)))
    println("countChange(300,List(500,200,100,50,20,10,5,2,1)) = " + cc(300,List(500,200,100,50,20,10,5,2,1)))
  }
}
