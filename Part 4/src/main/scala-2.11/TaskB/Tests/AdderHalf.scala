package TaskB.Tests

import TaskB.Exercise2._
import akka.actor.{ActorSystem, Props}

object AdderHalf {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("Exercise2")

    val A = system.actorOf(Props(new Wire(false)), "A")
    val B = system.actorOf(Props(new Wire(false)), "B")

    val sum_out = system.actorOf(Props(new Wire(false)), "sum_out")
    val carry_out = system.actorOf(Props(new Wire(false)), "carry_out")

    val halfAdder = system.actorOf(Props(new HalfAdder(A, B, sum_out, carry_out)), "halfAdder")

    val a_probe = system.actorOf(Props(new Probe("a_probe", A)), "a_probe")
    val b_probe = system.actorOf(Props(new Probe("b_probe", B)), "b_probe")
    val sum_probe = system.actorOf(Props(new Probe("sum_probe", sum_out)), "sum_probe")
    val carry_probe = system.actorOf(Props(new Probe("carry_probe", carry_out)), "carry_probe")

    def test(testDetails: String, testValues: (Boolean, Boolean)) = {
      println("\n" + testDetails)
      A ! testValues._1
      B ! testValues._2
      Thread.sleep(3000) // enough time for test to end
    }

    Thread.sleep(2000) // enough time for actors to be created
    test("Test 1 (false, false)", (false, false))
    test("Test 2 (false, true)",  (false, true))
    test("Test 3 (true, false)",  (true, false))
    test("Test 4 (true, true)",   (true, true))

    system.terminate()
  }
}
