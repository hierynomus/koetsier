package nl.javadude.koetsier

import examen._
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test

/**
 * Created by IntelliJ IDEA.
 * User: jeroen
 * Date: Oct 17, 2009
 * Time: 4:32:19 PM
 * To change this template use File | Settings | File Templates.
 */

case class DummyMessage(naam : String)
class XmlTest extends AssertionsForJUnit {

//  @Test def examenActorShouldNotUnderstandMessage {
//    intercept [RuntimeException] {
//      ExamenActor !! DummyMessage("test")
//    }
//  }

  @Test def examenActorShouldReadAnExamen {
    val examen : Examen = (ExamenActor !? HaalExamenOp("hoofd1")).asInstanceOf[Examen]
    assert(examen != null)
    println(examen)
    examen.vragen.foreach(v => println(v.vraag))
  }

  @Test def examenActorShouldBeoordeelExamen {
    val examen : Examen = (ExamenActor !? HaalExamenOp("hoofd1")).asInstanceOf[Examen]
    val nrVragen = examen.vragen.size
    val score = (ExamenActor !? BeoordeelExamen("hoofd1", List.make(nrVragen, "a"))).asInstanceOf[Score]
    assert(score.score == nrVragen)

    val foutScore = (ExamenActor !? BeoordeelExamen("hoofd1", List.make(nrVragen, "b"))).asInstanceOf[Score]
    assert(foutScore.score == 0)
    assert(foutScore.foutBeantwoord.size == nrVragen)
  }
}