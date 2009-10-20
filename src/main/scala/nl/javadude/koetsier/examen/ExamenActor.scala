package nl.javadude.koetsier.examen

import actors.Actor
import actors.Actor._
import xml.{NodeSeq, Node, Elem, XML}

/**
 * Created by IntelliJ IDEA.
 * User: jeroen
 * Date: Oct 17, 2009
 * Time: 4:13:17 PM
 * To change this template use File | Settings | File Templates.
 */

case class HaalExamenOp(naam : String)
case class BeoordeelExamen(naam : String, antwoorden : List[String])

class Boek(val bladzijde : Int, val regel : Int)
class Antwoord(val id : String, val score : Int, val antwoord : String)
class Vraag(val id : Int, val vraag : String, val afbeelding : String, val boek : Boek)
class MultipleChoiceVraag(override val id : Int, override val vraag : String, override val afbeelding : String, val antwoorden : Map[String, Antwoord], override val boek : Boek) extends Vraag(id, vraag, afbeelding, boek)
class OpenVraag(override val id : Int, override val vraag : String, override val afbeelding : String, override val boek : Boek) extends Vraag(id, vraag, afbeelding, boek)
class Examen(val naam : String, val vragen : List[MultipleChoiceVraag])
class Hoofdstuk(val naam : String, val vragen : List[Vraag])
class Score(val score : Int, val foutBeantwoord : List[Vraag])

object Examen {
  def parse (examen : Elem) = {
    val vragen = (examen \\ "vraag").map(Vraag.parseMC(_))
    new Examen(examen.child.toList.head.text, vragen.toList)
  }
}

object Vraag {
  def parseMC(vraag : Node) : MultipleChoiceVraag = {
    val id = (vraag \ "@id").text.toInt
    val vraagStelling = (vraag \ "vraagstelling").text
    val afbeelding = (vraag \ "afbeelding").text
    val boek = new Boek((vraag \ "pagina").text.toInt, (vraag \ "regel").text.toInt)
//    if (vraag \ "@nrant" != NodeSeq.Empty) {
      // MultipleChoiceVraag
      val antwoorden = (vraag \ "antwoord").map(Antwoord.parse(_)).foldLeft(Map[String, Antwoord]()) {(m, a) => m(a.id) = a}
      new MultipleChoiceVraag(id, vraagStelling, afbeelding, antwoorden, boek)
//    } else {
//      // OpenVraag
//      new OpenVraag(id, vraagStelling, afbeelding, boek)
//    }
  }
}

object Antwoord {
  def parse(antwoord : Node) : Antwoord = {
    val id = (antwoord \ "@id").text
    val score = (antwoord \ "@score").text.toInt
    val text = antwoord.text
    new Antwoord(id, score, text)
  }
}

object ExamenActor extends Actor {
  var examens = Map[String, Examen]() 
  def act = {
    loop {
      react {
        case HaalExamenOp(naam) =>
          if (!examens.contains(naam)) {
            val examen = XML.loadFile(naam + ".xml")
            examens += (naam -> Examen.parse(examen))
          }
          reply(examens.get(naam).get)
        case BeoordeelExamen(naam, antwoorden) =>
          val examen = examens.get(naam).getOrElse(error("Kon examen niet vinden"))
          var fouten : List[Vraag] = Nil
          val score = examen.vragen.zip(antwoorden).map(t => t._1 match {
            case mc : MultipleChoiceVraag => mc.antwoorden.get(t._2) match {
              case a : Some[Antwoord] => a.get.score match {
                case 0 => fouten += mc
                  0
                case 1 => 1
                case _ => error("Geen juiste score van vraag " + mc)
              }
              case None => 0
            }
//            case _ => 0
          }).reduceLeft(_+_)
          println(score)
          reply(new Score(score, fouten))
        case _ => throw new RuntimeException("Begrijp niet wat je wil doen.")

      }
    }
  }

  start
}