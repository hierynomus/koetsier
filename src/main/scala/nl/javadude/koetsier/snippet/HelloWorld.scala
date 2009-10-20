package nl.javadude.koetsier.snippet

import net.liftweb.http.{RequestVar, SHtml}
import nl.javadude.koetsier.examen._

//object antwoorden extends RequestVar[Map[String, String]]


class HelloWorld {
  def howdy = {
    val examen = (ExamenActor !? HaalExamenOp("hoofd1")).asInstanceOf[Examen]
    var antwoorden = Map[String, String]()
    <span>
      <h1>{examen.naam}</h1>
      <div> {

          examen.vragen.map { v =>
            <div><span><h2>{v.vraag}</h2></span>
            {
              v match {
                case mc : MultipleChoiceVraag => mc.antwoorden.map {
                  a => <span>{a._2.antwoord}</span>
                }
//                case _ => <span>open vraag</span>
              }
            }</div>
          }
//        val vraag <- examen.vragen
//          <span><h2>{vraag.vraag}</h2></span> {
//            vraag match {
//              case mc : MultipleChoiceVraag => <span>bla</span>
//              case _ => <span>{ SHtml.text("vraag" + vraag.id, a => antwoorden) }</span>
//            }
//          }
//        }
      }</div>
    </span>
  }
}

