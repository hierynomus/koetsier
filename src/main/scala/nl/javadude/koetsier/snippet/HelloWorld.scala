package nl.javadude.koetsier.snippet

import nl.javadude.koetsier.examen.{MultipleChoiceVraag, Examen, ExamenActor, HaalExamenOp}
import net.liftweb.http.{RequestVar, SHtml}

object antwoorden extends RequestVar[Map[String, String]]


class HelloWorld {
  def howdy = {
    val examen = (ExamenActor !? HaalExamenOp("hoofd1")).asInstanceOf[Examen]

    <span>
      <h1>{examen.naam}</h1>
      <div> {
        for {
          val vraag <- examen.vragen
          <span><h2>{vraag.vraag}</h2></span> {
            vraag match {
              case mc : MultipleChoiceVraag =>
              case _ => <span>{ SHtml.text("vraag" + vraag.id, a => antwoorden)
            }
          }
        } 
      }</div>
    </span>
  }
}

