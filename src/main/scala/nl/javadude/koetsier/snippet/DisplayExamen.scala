package nl.javadude.koetsier.snippet

import nl.javadude.koetsier.examen.{ExamenActor, HaalExamenOp, Examen}
import _root_.scala.xml.{NodeSeq,Text,Elem}
import _root_.net.liftweb.util.{Box,Full,Empty,Helpers,Log}
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.http.{S,SHtml}

/**
 * Created by IntelliJ IDEA.
 * User: jeroen
 * Date: Oct 20, 2009
 * Time: 7:57:09 PM
 * To change this template use File | Settings | File Templates.
 */

class DisplayExamen {

  def display(xhtml: NodeSeq): NodeSeq = {
    val examen = (ExamenActor !? HaalExamenOp("hoofd1")).asInstanceOf[Examen]

    bind("e", xhtml,
      "naam" -> Text(examen.naam),
      "vragen" -> examen.vragen.flatMap ( v =>
        bind ("v", chooseTemplate("vraag", "list", xhtml),
          "vraag" -> Text(v.vraag),
          "antwoorden" -> SHtml.select(v.antwoorden.map(a => (a._1, a._2.antwoord)).toSeq, Box[String](None), { a => println(v.id + " -> " + a)}))),
      "submit" -> SHtml.submit("Beantwoord", () => println("Form submit")))
  }
}