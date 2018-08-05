package TaskB.Tests

import TaskB.Exercise2._
import akka.actor.{ActorSystem, Props}

object CircuitComplex {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("Exercise2")

    val A = system.actorOf(Props(new Wire(false)), "A")
    val B = system.actorOf(Props(new Wire(false)), "B")
    val C = system.actorOf(Props(new Wire(false)), "C")

    val nA = system.actorOf(Props(new Wire(false)), "nA")
    val nB = system.actorOf(Props(new Wire(false)), "nB")
    val nC = system.actorOf(Props(new Wire(false)), "nC")
    val nA_comp = system.actorOf(Props(new Inverter(A, nA)), "nA_comp")
    val nB_comp = system.actorOf(Props(new Inverter(B, nB)), "nB_comp")
    val nC_comp = system.actorOf(Props(new Inverter(C, nC)), "nC_comp")

    val nAnC = system.actorOf(Props(new Wire(false)), "nAnC")
    val D = system.actorOf(Props(new Wire(false)), "D")
    val AnB = system.actorOf(Props(new Wire(false)), "AnB")
    val AnBD = system.actorOf(Props(new Wire(false)), "AnBD")
    val nAnC_comp = system.actorOf(Props(new And(nA, nC, nAnC)), "nAnC_comp")
    val D_comp = system.actorOf(Props(new Inverter(nAnC, D)), "D_comp")
    val AnB_comp = system.actorOf(Props(new And(A, nB, AnB)), "AnB_comp")
    val AnBD_comp = system.actorOf(Props(new And(AnB, D, AnBD)), "AnBD_comp")

    val AB = system.actorOf(Props(new Wire(false)), "AB")
    val ABC = system.actorOf(Props(new Wire(false)), "ABC")
    val AB_comp = system.actorOf(Props(new And(A, B, AB)), "AB_comp")
    val ABC_comp = system.actorOf(Props(new And(AB, C, ABC)), "ABC_comp")

    val Out = system.actorOf(Props(new Wire(false)), "Out")
    val Out_comp = system.actorOf(Props(new Or(AnBD, ABC, Out)), "Out_comp")

    var A_probe = system.actorOf(Props(new Probe("A", A)), "A_probe")
    var B_probe = system.actorOf(Props(new Probe("B", B)), "B_probe")
    var C_probe = system.actorOf(Props(new Probe("C", C)), "C_probe")
    var Out_probe = system.actorOf(Props(new Probe("Out", Out)), "Out_probe")

    def test(testDetails: String, testValues: (Boolean, Boolean, Boolean)) = {
      println("\n" + testDetails)
      A ! testValues._1
      B ! testValues._2
      C ! testValues._3
      Thread.sleep(3000) // enough time for test to end
    }

    Thread.sleep(2000) // enough time for actors to be created
    test("Test 1 (false, false, false)", (false, false, false))
    test("Test 2 (false, false, true)",  (false, false, true))
    test("Test 3 (false, true, false)",  (false, true, false))
    test("Test 4 (false, true, true)",   (false, true, true))
    test("Test 1 (true, false, false)",  (true, false, false))
    test("Test 2 (true, false, true)",   (true, false, true))
    test("Test 3 (true, true, false)",   (true, true, false))
    test("Test 4 (true, true, true)",    (true, true, true))

    system.terminate()
  }
}
