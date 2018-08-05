package TaskB.Tests

import TaskB.Exercise2._
import akka.actor.{ActorSystem, Props}

object CircuitBasic {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("Exercise2")

    /*
     * WIRES
     */
    val A = system.actorOf(Props(new Wire(false)), "A")
    val B = system.actorOf(Props(new Wire(false)), "B")
    val nA_out = system.actorOf(Props(new Wire(false)), "nA_out")
    val nB_out = system.actorOf(Props(new Wire(false)), "nB_out")
    val AorB_out = system.actorOf(Props(new Wire(false)), "AorB_out")
    val AoraltB_out = system.actorOf(Props(new Wire(false)), "AoraltB_out")
    val AandB_out = system.actorOf(Props(new Wire(false)), "AandB_out")

    /*
     * COMPONENTS
     */
    val nA = system.actorOf(Props(new Inverter(A, nA_out)), "nA")
    val nB = system.actorOf(Props(new Inverter(B, nB_out)), "nB")
    val AorB = system.actorOf(Props(new Or(A, B, AorB_out)), "AorB")
    val AoraltB = system.actorOf(Props(new OrAlt(A, B, AoraltB_out)), "AoraltB")
    val AandB = system.actorOf(Props(new And(A, B, AandB_out)), "AandB")

    /*
     * PROBES
     */
    val A_probe = system.actorOf(Props(new Probe("A_probe", A)), "A_probe")
    val B_probe = system.actorOf(Props(new Probe("B_probe", B)), "B_probe")
    val nA_probe = system.actorOf(Props(new Probe("nA_probe", nA_out)), "nA_probe")
    val nB_probe = system.actorOf(Props(new Probe("nB_probe", nB_out)), "nB_probe")
    val AorB_probe = system.actorOf(Props(new Probe("AorB_probe", AorB_out)), "AorB_probe")
    val AoraltB_probe = system.actorOf(Props(new Probe("AoraltB_probe", AoraltB_out)), "AoraltB_probe")
    val AandB_probe = system.actorOf(Props(new Probe("AandB_probe", AandB_out)), "AandB_probe")

    /*
     * TEST FUNCTION
     */
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
